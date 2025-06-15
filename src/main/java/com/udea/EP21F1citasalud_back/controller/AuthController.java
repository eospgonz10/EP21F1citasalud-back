package com.udea.EP21F1citasalud_back.controller;

import com.udea.EP21F1citasalud_back.DTO.AuthRequest;
import com.udea.EP21F1citasalud_back.DTO.AuthResponse;
import com.udea.EP21F1citasalud_back.DTO.TwoFactorRequest;
import com.udea.EP21F1citasalud_back.entity.Estado;
import com.udea.EP21F1citasalud_back.entity.Permiso;
import com.udea.EP21F1citasalud_back.entity.RegistroAcceso;
import com.udea.EP21F1citasalud_back.entity.Usuario;
import com.udea.EP21F1citasalud_back.repository.UsuarioRepository;
import com.udea.EP21F1citasalud_back.repository.RegistroAccesoRepository;
import com.udea.EP21F1citasalud_back.security.JwtTokenProvider;
import com.udea.EP21F1citasalud_back.security.TwoFactorAuthManager;
import com.udea.EP21F1citasalud_back.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@Tag(name = "Autenticación", description = "API para autenticación de usuarios")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RegistroAccesoRepository registroAccesoRepository;

    @Autowired
    private TwoFactorAuthManager twoFactorAuthManager;

    @Value("${2fa.expiration.minutes}")
    private int twoFactorExpirationMinutes;

    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión (primer paso, requiere 2FA)",
        description = "Valida credenciales y envía código 2FA. El usuario debe luego llamar a /auth/verify-2fa.",
        tags = {"Autenticación"}
    )
    public ResponseEntity<?> loginPrimerPaso(@RequestBody AuthRequest loginRequest, HttpServletRequest request) {
        String estado = "OK";
        String codigoError = null;
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (usuario == null) {
            estado = "USUARIO_NO_ENCONTRADO";
            codigoError = "NO_USER";
            registrarAcceso(null, request, estado, codigoError);
            return ResponseEntity.status(401).body(new AuthResponse("Credenciales inválidas", "ERROR"));
        }
        // Validar estado del usuario
        if (usuario.getEstado() == null || usuario.getEstado().getIdEstado() == null) {
            return ResponseEntity.status(401).body(new AuthResponse("El usuario no tiene estado definido", "ERROR"));
        }
        if (usuario.getEstado().getIdEstado() == 3) { // 3 = SUSPENDIDO
            return ResponseEntity.status(403).body(new AuthResponse("El usuario está suspendido. Contacte al administrador.", "SUSPENDED"));
        }
        if (usuario.getEstado().getIdEstado() != 1) { // 1 = ACTIVO
            return ResponseEntity.status(401).body(new AuthResponse("El usuario no está activo", "INACTIVE"));
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()));
            String code = String.format("%06d", new java.util.Random().nextInt(999999));
            twoFactorAuthManager.storeCode(loginRequest.getEmail(), code, twoFactorExpirationMinutes);
            System.out.println("[2FA] Código para " + loginRequest.getEmail() + ": " + code);
            estado = "2FA_ENVIADO";
            registrarAcceso(usuario, request, estado, null);
            return ResponseEntity.ok(new AuthResponse(
                "Código de verificación enviado. Verifique su SMS/correo (simulado en consola).", 
                "2FA_REQUIRED"
            ));
        } catch (Exception ex) {
            // Manejo de intentos fallidos por logs SOLO del día actual
            java.time.LocalDateTime inicioDia = java.time.LocalDate.now().atStartOfDay();
            long intentos = registroAccesoRepository.countByUsuarioAndEstadoAndFechaHoraAfter(
                usuario, "CREDENCIALES_INVALIDAS", inicioDia);
            if (intentos + 1 >= 3) {
                // Cambiar estado a suspendido (3)
                if (usuario.getEstado() == null || usuario.getEstado().getIdEstado() != 3) {
                    Estado estadoSuspendido = new Estado();
                    estadoSuspendido.setIdEstado(3);
                    usuario.setEstado(estadoSuspendido);
                    usuarioRepository.save(usuario);
                }
            }
            estado = "CREDENCIALES_INVALIDAS";
            codigoError = ex.getMessage();
            registrarAcceso(usuario, request, estado, codigoError);
            if (usuario.getEstado() != null && usuario.getEstado().getIdEstado() == 3) {
                return ResponseEntity.status(403).body(new AuthResponse(
                    "El usuario ha sido suspendido por múltiples intentos fallidos. Contacte al administrador.", 
                    "SUSPENDED"
                ));
            }
            return ResponseEntity.status(401).body(new AuthResponse("Credenciales inválidas", "ERROR"));
        }
    }

    @PostMapping("/verify-2fa")
    @Operation(
        summary = "Verificar código 2FA y obtener JWT",
        description = "Valida el código 2FA y retorna el JWT si es correcto.",
        tags = {"Autenticación"}
    )
    public ResponseEntity<?> verificar2FA(@RequestBody TwoFactorRequest twoFactorRequest, HttpServletRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(twoFactorRequest.getEmail()).orElse(null);
        String estado;
        String codigoError = null;
        if (usuario == null) {
            estado = "USUARIO_NO_ENCONTRADO";
            codigoError = "NO_USER";
            registrarAcceso(null, request, estado, codigoError);
            return ResponseEntity.status(401).body(new AuthResponse("Usuario no encontrado", "ERROR"));
        }
        boolean valido = twoFactorAuthManager.verifyCode(twoFactorRequest.getEmail(), twoFactorRequest.getCode());
        if (!valido) {
            estado = "2FA_INVALIDO";
            codigoError = "INVALIDO_O_EXPIRADO";
            registrarAcceso(usuario, request, estado, codigoError);
            return ResponseEntity.status(401).body(new AuthResponse("Código 2FA inválido o expirado", "ERROR"));
        }
        // Construir UserDetailsImpl y Authentication manualmente
        UserDetailsImpl userDetails = UserDetailsImpl.build(usuario);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateJwtToken(authentication);
        usuario.setUltimoAcceso(LocalDate.now());
        usuarioRepository.save(usuario);
        String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombreRol() : "SIN_ROL";
        List<String> permisos = usuario.getRol() != null ?
            usuario.getRol().getPermisos().stream().map(Permiso::getNombrePermiso).collect(Collectors.toList()) : List.of();
        estado = "LOGIN_OK";
        registrarAcceso(usuario, request, estado, null);
        return ResponseEntity.ok(new AuthResponse(
            jwt,
            userDetails.getId(),
            userDetails.getNombre(),
            userDetails.getApellido(),
            userDetails.getEmail(),
            rolNombre,
            permisos
        ));
    }

    private void registrarAcceso(Usuario usuario, HttpServletRequest request, String estado, String codigoError) {
        if (usuario == null) {
            // No guardar registro si no hay usuario válido
            return;
        }
        RegistroAcceso log = new RegistroAcceso();
        log.setUsuario(usuario);
        log.setFechaHora(LocalDateTime.now());
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) ip = request.getRemoteAddr();
        log.setDireccionIp(ip);
        log.setEstado(estado);
        log.setCodigoError(codigoError);
        registroAccesoRepository.save(log);
    }
}

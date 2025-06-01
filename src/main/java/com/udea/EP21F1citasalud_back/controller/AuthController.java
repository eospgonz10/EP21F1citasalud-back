package com.udea.EP21F1citasalud_back.controller;

import com.udea.EP21F1citasalud_back.DTO.AuthRequest;
import com.udea.EP21F1citasalud_back.DTO.AuthResponse;
import com.udea.EP21F1citasalud_back.entity.Permiso;
import com.udea.EP21F1citasalud_back.entity.Usuario;
import com.udea.EP21F1citasalud_back.repository.UsuarioRepository;
import com.udea.EP21F1citasalud_back.security.JwtTokenProvider;
import com.udea.EP21F1citasalud_back.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
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

    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión", 
        description = "Permite a un usuario iniciar sesión y obtener un token JWT que deberá usar en las demás solicitudes",
        tags = {"Autenticación"}
    )
    @ApiResponse(responseCode = "200", description = "Autenticación exitosa - El token JWT debe incluirse en el encabezado Authorization como 'Bearer {token}'",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponse.class)))
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        // Actualizar la fecha de último acceso
        usuarioRepository.findById(userDetails.getId())
                .ifPresent(usuario -> {
                    usuario.setUltimoAcceso(LocalDate.now());
                    usuarioRepository.save(usuario);
                });
        
        // Obtener el usuario para acceder al rol y permisos
        Usuario usuario = usuarioRepository.findById(userDetails.getId()).orElse(null);
        String rolNombre = usuario != null && usuario.getRol() != null ? usuario.getRol().getNombreRol() : "SIN_ROL";
        
        // Obtener permisos
        List<String> permisos = usuario != null && usuario.getRol() != null ?
                usuario.getRol().getPermisos().stream()
                        .map(Permiso::getNombrePermiso)
                        .collect(Collectors.toList()) : List.of();
        
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
}

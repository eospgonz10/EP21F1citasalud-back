package com.udea.EP21F1citasalud_back.controller;

import com.udea.EP21F1citasalud_back.DTO.UsuarioDTO;
import com.udea.EP21F1citasalud_back.entity.ActividadUsuario;
import com.udea.EP21F1citasalud_back.entity.Estado;
import com.udea.EP21F1citasalud_back.entity.Usuario;
import com.udea.EP21F1citasalud_back.repository.ActividadUsuarioRepository;
import com.udea.EP21F1citasalud_back.repository.UsuarioRepository;
import com.udea.EP21F1citasalud_back.security.UserDetailsImpl;
import com.udea.EP21F1citasalud_back.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
@Tag(name = "Usuario", description = "API para la gestión de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    @Autowired
    private ActividadUsuarioRepository actividadUsuarioRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema
     * @return Lista de usuarios
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('VISUALIZAR_TODOS_USUARIOS')")
    @Operation(summary = "Obtener todos los usuarios", description = "Retorna una lista de todos los usuarios registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioDTO.class)))
    public ResponseEntity<List<UsuarioDTO>> getAllUsers() {
        List<UsuarioDTO> usuarios = usuarioService.getAllUsers();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtiene un usuario por su ID
     * @param id ID del usuario a buscar
     * @return Usuario encontrado o status 404 si no existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("authentication.principal.id == #id or hasRole('ADMINISTRADOR') or hasAuthority('VISUALIZAR_TODOS_USUARIOS')")
    @Operation(summary = "Obtener usuario por ID", description = "Retorna un usuario según el ID proporcionado. Solo el propio usuario o un administrador pueden acceder a estos datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "No autorizado para ver este usuario",
                    content = @Content)
    })
    public ResponseEntity<UsuarioDTO> getUserById(
            @Parameter(description = "ID del usuario a buscar", required = true)
            @PathVariable Long id) {
        return usuarioService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo usuario
     * @param usuarioDTO Datos del usuario a crear
     * @return Usuario creado con status 201
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('CREAR_USUARIO')")
    @Operation(summary = "Crear un nuevo usuario", description = "Registra un nuevo usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos",
                    content = @Content)
    })
    public ResponseEntity<UsuarioDTO> createUser(
            @Parameter(description = "Datos del usuario a crear", required = true)
            @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO nuevoUsuario = usuarioService.createUser(usuarioDTO);
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Usuario usuarioAccion = usuarioRepository.findById(userDetails.getId()).orElse(null);
        if (usuarioAccion == null) {
            System.err.println("[LOG ACTIVIDAD] usuarioAccion es null, no se registra actividad");
        } else {
            ActividadUsuario actividad = new ActividadUsuario();
            actividad.setUsuario(usuarioAccion);
            actividad.setTipoActividad("CREAR_USUARIO");
            actividad.setDescripcion("Creación de usuario con email: " + nuevoUsuario.getEmail());
            actividad.setFechaHora(LocalDateTime.now());
            // No se agregan detalles adicionales en creación
            actividad.setDetalleAdiccionales("Usuario creado");
            try {
                actividadUsuarioRepository.save(actividad);
                System.out.println("[LOG ACTIVIDAD] Actividad guardada correctamente");
            } catch (Exception e) {
                System.err.println("[LOG ACTIVIDAD] Error guardando actividad: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    /**
     * Actualiza un usuario existente
     * @param id ID del usuario a actualizar
     * @param usuarioDTO Nuevos datos del usuario
     * @return Usuario actualizado o status 404 si no existe
     */
    @PutMapping("/{id}")
    @PreAuthorize("authentication.principal.id == #id or hasRole('ADMINISTRADOR')")
    @Operation(summary = "Actualizar un usuario", description = "Actualiza los datos de un usuario existente según su ID. Solo el propio usuario o un administrador pueden realizar esta operación.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "No autorizado para actualizar este usuario",
                    content = @Content)
    })
    public ResponseEntity<UsuarioDTO> updateUser(
            @Parameter(description = "ID del usuario a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nuevos datos del usuario", required = true)
            @RequestBody UsuarioDTO usuarioDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean esAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
        // Si el usuario NO es admin y está actualizando su propio usuario, no puede cambiar el rol
        if (!esAdmin && userDetails.getId().equals(id)) {
            usuarioDTO.setRolId(null); // Ignorar cualquier cambio de rol
        }
        Usuario usuarioAntes = usuarioRepository.findById(id).orElse(null);
        ResponseEntity<UsuarioDTO> response = usuarioService.updateUser(id, usuarioDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        if (response.getStatusCode().is2xxSuccessful()) {
            Usuario usuarioAccion = usuarioRepository.findById(userDetails.getId()).orElse(null);
            if (usuarioAccion == null) {
                System.err.println("[LOG ACTIVIDAD] usuarioAccion es null, no se registra actividad");
            } else {
                ActividadUsuario actividad = new ActividadUsuario();
                actividad.setUsuario(usuarioAccion);
                actividad.setTipoActividad("ACTUALIZAR_USUARIO");
                actividad.setDescripcion("Actualización de usuario con ID: " + id);
                actividad.setFechaHora(LocalDateTime.now());
                // Solo mensaje genérico, sin detalles específicos
                actividad.setDetalleAdiccionales("Se actualizó usuario");
                try {
                    actividadUsuarioRepository.save(actividad);
                    System.out.println("[LOG ACTIVIDAD] Actividad guardada correctamente");
                } catch (Exception e) {
                    System.err.println("[LOG ACTIVIDAD] Error guardando actividad: " + e.getMessage());
                }
            }
        }
        return response;
    }

    /**
     * Activar un usuario por su correo electrónico
     * @param email El correo electrónico del usuario a activar
     * @return Mensaje de éxito o error
     */
    @PostMapping("/activar-usuario")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Activar usuario por correo", description = "Permite a un administrador activar un usuario por su email")
    public ResponseEntity<?> activarUsuarioPorEmail(@RequestParam String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
        if (usuario.getEstado() == null || usuario.getEstado().getIdEstado() != 3) {
            return ResponseEntity.status(400).body("El usuario no está bloqueado/suspendido");
        }
        Estado estadoActivo = new Estado();
        estadoActivo.setIdEstado(1);
        usuario.setEstado(estadoActivo);
        usuarioRepository.save(usuario);

        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Usuario usuarioAccion = usuarioRepository.findById(userDetails.getId()).orElse(null);
            if (usuarioAccion != null) {
                ActividadUsuario actividad = new ActividadUsuario();
                actividad.setUsuario(usuarioAccion);
                actividad.setTipoActividad("DESBLOQUEO_USUARIO");
                actividad.setDescripcion("Desbloqueo de usuario con email: " + usuario.getEmail());
                actividad.setFechaHora(LocalDateTime.now());
                actividad.setDetalleAdiccionales("Usuario desbloqueado por admin");
                actividadUsuarioRepository.save(actividad);
            }
        } catch (Exception e) {
            System.err.println("[LOG ACTIVIDAD] Error guardando actividad de desbloqueo: " + e.getMessage());
        }

        return ResponseEntity.ok("Usuario activado correctamente");
    }

    private boolean equalsOrNull(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }
}
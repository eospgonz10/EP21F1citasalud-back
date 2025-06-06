package com.udea.EP21F1citasalud_back.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.udea.EP21F1citasalud_back.DTO.UsuarioDTO;
import com.udea.EP21F1citasalud_back.entity.ActividadUsuario;
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

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

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
        Usuario usuarioAntes = usuarioRepository.findById(id).orElse(null);
        ResponseEntity<UsuarioDTO> response = usuarioService.updateUser(id, usuarioDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        if (response.getStatusCode().is2xxSuccessful()) {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

    // Utilidad para comparar y obtener solo los cambios
    private HashMap<String, Object> obtenerCambios(Usuario antes, UsuarioDTO despues) {
        HashMap<String, Object> cambios = new HashMap<>();
        if (antes == null || despues == null) return cambios;
        if (!equalsOrNull(antes.getNombre(), despues.getNombre())) cambios.put("nombre", "'"+antes.getNombre()+"' → '"+despues.getNombre()+"'");
        if (!equalsOrNull(antes.getApellido(), despues.getApellido())) cambios.put("apellido", "'"+antes.getApellido()+"' → '"+despues.getApellido()+"'");
        if (!equalsOrNull(antes.getEmail(), despues.getEmail())) cambios.put("email", "'"+antes.getEmail()+"' → '"+despues.getEmail()+"'");
        if (!equalsOrNull(antes.getDocumento(), despues.getDocumento())) cambios.put("documento", "'"+antes.getDocumento()+"' → '"+despues.getDocumento()+"'");
        if (!equalsOrNull(antes.getTelefono(), despues.getTelefono())) cambios.put("telefono", "'"+antes.getTelefono()+"' → '"+despues.getTelefono()+"'");
        if (antes.getRol() != null && despues.getRolId() != null && !antes.getRol().getRolId().equals(despues.getRolId())) cambios.put("rolId", "'"+antes.getRol().getRolId()+"' → '"+despues.getRolId()+"'");
        if (antes.getEstado() != null && despues.getEstado() != null && !antes.getEstado().getNombreEstado().equals(despues.getEstado())) cambios.put("estado", "'"+antes.getEstado().getNombreEstado()+"' → '"+despues.getEstado()+"'");
        if (antes.getTipoDocumento() != null && despues.getTipoDocumento() != null && !antes.getTipoDocumento().getTipoDocumento().equals(despues.getTipoDocumento())) cambios.put("tipoDocumento", "'"+antes.getTipoDocumento().getTipoDocumento()+"' → '"+despues.getTipoDocumento()+"'");
        return cambios;
    }

    private boolean equalsOrNull(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    // Utilidad para convertir Usuario a un Map serializable
    private HashMap<String, Object> usuarioToSimpleMap(Usuario usuario) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("usuarioId", usuario.getUsuarioId());
        map.put("nombre", usuario.getNombre());
        map.put("apellido", usuario.getApellido());
        map.put("email", usuario.getEmail());
        map.put("documento", usuario.getDocumento());
        map.put("telefono", usuario.getTelefono());
        map.put("fechaRegistro", usuario.getFechaRegistro());
        map.put("ultimoAcceso", usuario.getUltimoAcceso());
        map.put("rol", usuario.getRol() != null ? usuario.getRol().getNombreRol() : null);
        map.put("estado", usuario.getEstado() != null ? usuario.getEstado().getNombreEstado() : null);
        map.put("tipoDocumento", usuario.getTipoDocumento() != null ? usuario.getTipoDocumento().getTipoDocumento() : null);
        return map;
    }

    // Utilidad para convertir UsuarioDTO a Map serializable
    private HashMap<String, Object> usuarioDtoToMap(UsuarioDTO dto) {
        if (dto == null) return null;
        HashMap<String, Object> map = new HashMap<>();
        if (dto.getUsuarioId() != null) map.put("usuarioId", dto.getUsuarioId());
        if (dto.getNombre() != null) map.put("nombre", dto.getNombre());
        if (dto.getApellido() != null) map.put("apellido", dto.getApellido());
        if (dto.getEmail() != null) map.put("email", dto.getEmail());
        if (dto.getDocumento() != null) map.put("documento", dto.getDocumento());
        if (dto.getTelefono() != null) map.put("telefono", dto.getTelefono());
        if (dto.getFechaRegistro() != null) map.put("fechaRegistro", dto.getFechaRegistro());
        if (dto.getUltimoAcceso() != null) map.put("ultimoAcceso", dto.getUltimoAcceso());
        if (dto.getRolId() != null) map.put("rolId", dto.getRolId());
        if (dto.getEstado() != null) map.put("estado", dto.getEstado());
        if (dto.getTipoDocumento() != null) map.put("tipoDocumento", dto.getTipoDocumento());
        // No incluir password ni campos complejos
        return map;
    }
}
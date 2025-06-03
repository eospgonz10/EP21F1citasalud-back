package com.udea.EP21F1citasalud_back.controller;

import com.udea.EP21F1citasalud_back.DTO.LoginRequest;
import com.udea.EP21F1citasalud_back.DTO.UsuarioDTO;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
@Tag(name = "Usuario", description = "API para la gestión de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Obtiene todos los usuarios registrados en el sistema
     * @return Lista de usuarios
     */
    @GetMapping
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
        return usuarioService.updateUser(id, usuarioDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Inicia la sesión de un usuario registrado
     * @param Request contiene el correo y la contraseña del usuario
     * @return ResponseEntity con los datos del usuario autenticado o un mensaje de error si falla
     */
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión de un usuario", description = "Inicia sesión de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario autenticado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "401", description = "Crdenciales inválidas o usuario no registrado",
                    content = @Content)
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest Request){
        try {
            UsuarioDTO usuarioDTO = usuarioService.login(Request.getEmail(), Request.getPassword());
            return ResponseEntity.ok(usuarioDTO);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }
}
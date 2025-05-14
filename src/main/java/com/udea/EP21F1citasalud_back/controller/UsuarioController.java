package com.udea.EP21F1citasalud_back.controller;

import com.udea.EP21F1citasalud_back.DTO.UserDTO;
import com.udea.EP21F1citasalud_back.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Obtiene todos los usuarios (requiere rol ADMIN)
     * @return Lista de usuarios
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> obtenerTodosUsuarios() {
        List<UserDTO> usuarios = usuarioService.obtenerTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtiene todos los usuarios activos (requiere rol ADMIN)
     * @return Lista de usuarios activos
     */
    @GetMapping("/activos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> obtenerUsuariosActivos() {
        List<UserDTO> usuarios = usuarioService.obtenerTodosUsuariosActivos();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtiene un usuario por su ID
     * @param id ID del usuario a buscar
     * @return Usuario encontrado o 404 si no existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @seguridadService.esUsuarioActual(#id)")
    public ResponseEntity<UserDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        UserDTO usuario = usuarioService.buscarPorId(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea un nuevo usuario (requiere rol ADMIN)
     * @param usuarioDTO Datos del usuario a crear
     * @return Usuario creado
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> crearUsuario(@Valid @RequestBody UserDTO usuarioDTO) {
        try {
            UserDTO nuevoUsuario = usuarioService.crearUsuario(usuarioDTO);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza los datos de un usuario existente
     * @param id ID del usuario a actualizar
     * @param usuarioDTO Nuevos datos del usuario
     * @return Usuario actualizado o 404 si no existe
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @seguridadService.esUsuarioActual(#id)")
    public ResponseEntity<UserDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UserDTO usuarioDTO) {
        try {
            UserDTO usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDTO);
            if (usuarioActualizado != null) {
                return ResponseEntity.ok(usuarioActualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Inactiva un usuario (requiere rol ADMIN)
     * @param id ID del usuario a inactivar
     * @return Usuario inactivado o 404 si no existe
     */
    @PatchMapping("/{id}/inactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> inactivarUsuario(@PathVariable Long id) {
        UserDTO usuario = usuarioService.inactivarUsuario(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Activa un usuario (requiere rol ADMIN)
     * @param id ID del usuario a activar
     * @return Usuario activado o 404 si no existe
     */
    @PatchMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> activarUsuario(@PathVariable Long id) {
        UserDTO usuario = usuarioService.activarUsuario(id);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cambia el estado de un usuario (activo/inactivo) (requiere rol ADMIN)
     * @param id ID del usuario
     * @param estadoId ID del nuevo estado
     * @return Usuario actualizado o 404 si no existe
     */
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> cambiarEstadoUsuario(
            @PathVariable Long id,
            @RequestParam Integer estadoId) {
        UserDTO usuario = usuarioService.cambiarEstadoUsuario(id, estadoId);
        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina un usuario (requiere rol ADMIN)
     * @param id ID del usuario a eliminar
     * @return 204 No Content si se eliminó correctamente, 404 si no existía
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        boolean eliminado = usuarioService.eliminarUsuario(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
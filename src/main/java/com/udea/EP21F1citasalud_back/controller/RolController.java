package com.udea.EP21F1citasalud_back.controller;

import com.udea.EP21F1citasalud_back.DTO.RolDTO;
import com.udea.EP21F1citasalud_back.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@CrossOrigin("*")
@Tag(name = "Roles", description = "API para la gestión de roles")
public class RolController {

    private final RolService rolService;

    @Autowired
    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    /**
     * Obtiene todos los roles registrados en el sistema
     * @return Lista de roles
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ASIGNAR_ROLES') or hasRole('ADMINISTRADOR')")
    @Operation(summary = "Obtener todos los roles", description = "Retorna una lista de todos los roles registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RolDTO.class)))
    public ResponseEntity<List<RolDTO>> getAllRoles() {
        List<RolDTO> roles = rolService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * Obtiene un rol por su ID
     * @param id ID del rol a buscar
     * @return Rol encontrado o status 404 si no existe
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ASIGNAR_ROLES') or hasRole('ADMINISTRADOR')")
    @Operation(summary = "Obtener un rol por ID", description = "Retorna un rol específico según su ID")
    @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RolDTO.class)))
    public ResponseEntity<RolDTO> getRolById(@PathVariable Integer id) {
        return rolService.getRolById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

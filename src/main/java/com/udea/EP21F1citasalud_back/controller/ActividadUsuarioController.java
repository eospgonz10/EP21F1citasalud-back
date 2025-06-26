package com.udea.EP21F1citasalud_back.controller;

import com.udea.EP21F1citasalud_back.DTO.ActividadUsuarioDTO;
import com.udea.EP21F1citasalud_back.service.ActividadUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/actividad-usuario")
@CrossOrigin("*")
@Tag(name = "Actividad de Usuario", description = "API para consultar los logs de actividades de usuario")
public class ActividadUsuarioController {

    private final ActividadUsuarioService actividadUsuarioService;

    @Autowired
    public ActividadUsuarioController(ActividadUsuarioService actividadUsuarioService) {
        this.actividadUsuarioService = actividadUsuarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('VER_REPORTES')")
    @Operation(summary = "Obtener logs de actividades (paginado)", description = "Retorna los logs de actividades de usuario de forma paginada")
    @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ActividadUsuarioDTO.class)))
    public ResponseEntity<Object> getAll(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        HashMap<String, Object> response = actividadUsuarioService.getAllActividades(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{id}")
    @PreAuthorize("authentication.principal.id == #id or hasRole('ADMINISTRADOR') or hasAuthority('VER_REPORTES')")
    @Operation(summary = "Obtener logs de actividades por usuario", description = "Retorna los logs de actividades de un usuario específico")
    @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ActividadUsuarioDTO.class)))
    public ResponseEntity<List<ActividadUsuarioDTO>> getByUsuarioId(@PathVariable Long id) {
        List<ActividadUsuarioDTO> actividades = actividadUsuarioService.getActividadesByUsuarioId(id);
        return ResponseEntity.ok(actividades);
    }

    @GetMapping("/{actividadId}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('VER_REPORTES')")
    @Operation(summary = "Obtener log de actividad por ID", description = "Retorna un log de actividad específico por su ID")
    @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ActividadUsuarioDTO.class)))
    @ApiResponse(responseCode = "404", description = "Log no encontrado")
    public ResponseEntity<ActividadUsuarioDTO> getById(@PathVariable Long actividadId) {
        return actividadUsuarioService.getActividadById(actividadId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

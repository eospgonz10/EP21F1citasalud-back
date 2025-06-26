package com.udea.EP21F1citasalud_back.controller;

import com.udea.EP21F1citasalud_back.DTO.RegistroAccesoDTO;
import com.udea.EP21F1citasalud_back.service.RegistroAccesoService;
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
@RequestMapping("/registro-acceso")
@CrossOrigin("*")
@Tag(name = "Registro de Acceso", description = "API para consultar los registros de acceso de usuarios")
public class RegistroAccesoController {

    private final RegistroAccesoService registroAccesoService;

    @Autowired
    public RegistroAccesoController(RegistroAccesoService registroAccesoService) {
        this.registroAccesoService = registroAccesoService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('VER_REPORTES')")
    @Operation(summary = "Obtener todos los registros de acceso (paginado)", description = "Retorna los registros de acceso del sistema de forma paginada")
    @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegistroAccesoDTO.class)))
    public ResponseEntity<Object> getAll(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        HashMap<String, Object> response = registroAccesoService.getAllRegistrosAcceso(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{id}")
    @PreAuthorize("authentication.principal.id == #id or hasRole('ADMINISTRADOR') or hasAuthority('VER_REPORTES')")
    @Operation(summary = "Obtener registros de acceso por usuario", description = "Retorna los registros de acceso de un usuario específico")
    @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegistroAccesoDTO.class)))
    public ResponseEntity<List<RegistroAccesoDTO>> getByUsuarioId(@PathVariable Long id) {
        List<RegistroAccesoDTO> registros = registroAccesoService.getRegistrosAccesoByUsuarioId(id);
        return ResponseEntity.ok(registros);
    }

    @GetMapping("/{accesoId}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('VER_REPORTES')")
    @Operation(summary = "Obtener registro de acceso por ID", description = "Retorna un registro de acceso específico por su ID")
    @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegistroAccesoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    public ResponseEntity<RegistroAccesoDTO> getById(@PathVariable Long accesoId) {
        return registroAccesoService.getRegistroAccesoById(accesoId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

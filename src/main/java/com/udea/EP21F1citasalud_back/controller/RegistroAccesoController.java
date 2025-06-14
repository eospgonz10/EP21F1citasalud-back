package com.udea.EP21F1citasalud_back.controller;

import com.udea.EP21F1citasalud_back.DTO.RegistroAccesoDTO;
import com.udea.EP21F1citasalud_back.entity.RegistroAcceso;
import com.udea.EP21F1citasalud_back.repository.RegistroAccesoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/registro-acceso")
@CrossOrigin("*")
@Tag(name = "Registro de Acceso", description = "API para consultar los registros de acceso de usuarios")
public class RegistroAccesoController {

    @Autowired
    private RegistroAccesoRepository registroAccesoRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('VER_REPORTES')")
    @Operation(summary = "Obtener todos los registros de acceso (paginado)", description = "Retorna los registros de acceso del sistema de forma paginada")
    @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegistroAccesoDTO.class)))
    public ResponseEntity<Object> getAll(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "20") int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<RegistroAccesoDTO> dtoPage = registroAccesoRepository.findAll(pageable).map(this::toDTO);
        HashMap<String, Object> response = new HashMap<>();
        response.put("content", dtoPage.getContent());
        response.put("currentPage", dtoPage.getNumber());
        response.put("totalItems", dtoPage.getTotalElements());
        response.put("totalPages", dtoPage.getTotalPages());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{id}")
    @PreAuthorize("authentication.principal.id == #id or hasRole('ADMINISTRADOR') or hasAuthority('VER_REPORTES')")
    @Operation(summary = "Obtener registros de acceso por usuario", description = "Retorna los registros de acceso de un usuario específico")
    @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegistroAcceso.class)))
    public ResponseEntity<List<RegistroAccesoDTO>> getByUsuarioId(@PathVariable Long id) {
        List<RegistroAccesoDTO> dtos = registroAccesoRepository.findAll().stream()
            .filter(r -> r.getUsuario() != null && r.getUsuario().getUsuarioId().equals(id))
            .map(this::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{accesoId}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('VER_REPORTES')")
    @Operation(summary = "Obtener registro de acceso por ID", description = "Retorna un registro de acceso específico por su ID")
    @ApiResponse(responseCode = "200", description = "Operación exitosa",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegistroAccesoDTO.class)))
    @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    public ResponseEntity<RegistroAccesoDTO> getById(@PathVariable Long accesoId) {
        return registroAccesoRepository.findById(accesoId)
            .map(this::toDTO)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private RegistroAccesoDTO toDTO(RegistroAcceso entity) {
        RegistroAccesoDTO dto = new RegistroAccesoDTO();
        dto.setAccesoId(entity.getAccesoId());
        if (entity.getUsuario() != null) {
            dto.setUsuarioId(entity.getUsuario().getUsuarioId());
            dto.setUsuarioEmail(entity.getUsuario().getEmail());
        }
        dto.setFechaHora(entity.getFechaHora());
        dto.setDireccionIp(entity.getDireccionIp());
        dto.setEstado(entity.getEstado());
        dto.setCodigoError(entity.getCodigoError());
        return dto;
    }
}

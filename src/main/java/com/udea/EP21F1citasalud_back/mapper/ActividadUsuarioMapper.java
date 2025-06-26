package com.udea.EP21F1citasalud_back.mapper;

import com.udea.EP21F1citasalud_back.DTO.ActividadUsuarioDTO;
import com.udea.EP21F1citasalud_back.entity.ActividadUsuario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre ActividadUsuario entity y DTO
 * Centraliza la lógica de mapeo y elimina duplicación en controllers
 */
@Component
public class ActividadUsuarioMapper {

    /**
     * Convierte una entidad ActividadUsuario a DTO
     * @param entity Entidad a convertir
     * @return DTO correspondiente
     */
    public ActividadUsuarioDTO toDto(ActividadUsuario entity) {
        if (entity == null) {
            return null;
        }
        
        ActividadUsuarioDTO dto = new ActividadUsuarioDTO();
        dto.setActividadId(entity.getActividadId());
        
        if (entity.getUsuario() != null) {
            dto.setUsuarioId(entity.getUsuario().getUsuarioId());
            dto.setUsuarioEmail(entity.getUsuario().getEmail());
        }
        
        dto.setTipoActividad(entity.getTipoActividad());
        dto.setDescripcion(entity.getDescripcion());
        dto.setFechaHora(entity.getFechaHora());
        dto.setDetalleAdiccionales(entity.getDetalleAdiccionales());
        
        return dto;
    }

    /**
     * Convierte una lista de entidades a lista de DTOs
     * @param entities Lista de entidades
     * @return Lista de DTOs
     */
    public List<ActividadUsuarioDTO> toDtoList(List<ActividadUsuario> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

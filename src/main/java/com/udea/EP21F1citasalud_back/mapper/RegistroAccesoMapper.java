package com.udea.EP21F1citasalud_back.mapper;

import com.udea.EP21F1citasalud_back.DTO.RegistroAccesoDTO;
import com.udea.EP21F1citasalud_back.entity.RegistroAcceso;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre RegistroAcceso entity y DTO
 * Centraliza la lógica de mapeo y elimina duplicación en controllers
 */
@Component
public class RegistroAccesoMapper {

    /**
     * Convierte una entidad RegistroAcceso a DTO
     * @param entity Entidad a convertir
     * @return DTO correspondiente
     */
    public RegistroAccesoDTO toDto(RegistroAcceso entity) {
        if (entity == null) {
            return null;
        }
        
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

    /**
     * Convierte una lista de entidades a lista de DTOs
     * @param entities Lista de entidades
     * @return Lista de DTOs
     */
    public List<RegistroAccesoDTO> toDtoList(List<RegistroAcceso> entities) {
        if (entities == null) {
            return null;
        }
        
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

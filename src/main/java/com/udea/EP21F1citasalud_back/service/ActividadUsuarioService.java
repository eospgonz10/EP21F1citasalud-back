package com.udea.EP21F1citasalud_back.service;

import com.udea.EP21F1citasalud_back.DTO.ActividadUsuarioDTO;
import com.udea.EP21F1citasalud_back.entity.ActividadUsuario;
import com.udea.EP21F1citasalud_back.mapper.ActividadUsuarioMapper;
import com.udea.EP21F1citasalud_back.repository.ActividadUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar consultas de actividades de usuario
 * Centraliza la lógica de negocio y separa responsabilidades del controller
 */
@Service
@Transactional
public class ActividadUsuarioService {

    private final ActividadUsuarioRepository actividadUsuarioRepository;
    private final ActividadUsuarioMapper actividadUsuarioMapper;

    @Autowired
    public ActividadUsuarioService(ActividadUsuarioRepository actividadUsuarioRepository,
                                  ActividadUsuarioMapper actividadUsuarioMapper) {
        this.actividadUsuarioRepository = actividadUsuarioRepository;
        this.actividadUsuarioMapper = actividadUsuarioMapper;
    }

    /**
     * Obtiene todas las actividades paginadas
     * @param pageable Configuración de paginación
     * @return Respuesta paginada con actividades
     */
    @Transactional(readOnly = true)
    public HashMap<String, Object> getAllActividades(Pageable pageable) {
        Page<ActividadUsuario> page = actividadUsuarioRepository.findAll(pageable);
        Page<ActividadUsuarioDTO> dtoPage = page.map(actividadUsuarioMapper::toDto);
        
        HashMap<String, Object> response = new HashMap<>();
        response.put("content", dtoPage.getContent());
        response.put("currentPage", dtoPage.getNumber());
        response.put("totalItems", dtoPage.getTotalElements());
        response.put("totalPages", dtoPage.getTotalPages());
        
        return response;
    }

    /**
     * Obtiene actividades por usuario ID
     * @param usuarioId ID del usuario
     * @return Lista de actividades del usuario
     */
    @Transactional(readOnly = true)
    public List<ActividadUsuarioDTO> getActividadesByUsuarioId(Long usuarioId) {
        List<ActividadUsuario> actividades = actividadUsuarioRepository.findAll().stream()
                .filter(a -> a.getUsuario() != null && a.getUsuario().getUsuarioId().equals(usuarioId))
                .toList();
        
        return actividadUsuarioMapper.toDtoList(actividades);
    }

    /**
     * Obtiene una actividad por su ID
     * @param actividadId ID de la actividad
     * @return DTO de la actividad o Optional vacío
     */
    @Transactional(readOnly = true)
    public Optional<ActividadUsuarioDTO> getActividadById(Long actividadId) {
        return actividadUsuarioRepository.findById(actividadId)
                .map(actividadUsuarioMapper::toDto);
    }
}

package com.udea.EP21F1citasalud_back.service;

import com.udea.EP21F1citasalud_back.DTO.RegistroAccesoDTO;
import com.udea.EP21F1citasalud_back.entity.RegistroAcceso;
import com.udea.EP21F1citasalud_back.mapper.RegistroAccesoMapper;
import com.udea.EP21F1citasalud_back.repository.RegistroAccesoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar consultas de registros de acceso
 * Centraliza la lógica de negocio y separa responsabilidades del controller
 */
@Service
@Transactional
public class RegistroAccesoService {

    private final RegistroAccesoRepository registroAccesoRepository;
    private final RegistroAccesoMapper registroAccesoMapper;

    @Autowired
    public RegistroAccesoService(RegistroAccesoRepository registroAccesoRepository,
                                RegistroAccesoMapper registroAccesoMapper) {
        this.registroAccesoRepository = registroAccesoRepository;
        this.registroAccesoMapper = registroAccesoMapper;
    }

    /**
     * Obtiene todos los registros de acceso paginados
     * @param pageable Configuración de paginación
     * @return Respuesta paginada con registros de acceso
     */
    @Transactional(readOnly = true)
    public HashMap<String, Object> getAllRegistrosAcceso(Pageable pageable) {
        Page<RegistroAcceso> page = registroAccesoRepository.findAll(pageable);
        Page<RegistroAccesoDTO> dtoPage = page.map(registroAccesoMapper::toDto);
        
        HashMap<String, Object> response = new HashMap<>();
        response.put("content", dtoPage.getContent());
        response.put("currentPage", dtoPage.getNumber());
        response.put("totalItems", dtoPage.getTotalElements());
        response.put("totalPages", dtoPage.getTotalPages());
        
        return response;
    }

    /**
     * Obtiene registros de acceso por usuario ID
     * @param usuarioId ID del usuario
     * @return Lista de registros de acceso del usuario
     */
    @Transactional(readOnly = true)
    public List<RegistroAccesoDTO> getRegistrosAccesoByUsuarioId(Long usuarioId) {
        List<RegistroAcceso> registros = registroAccesoRepository.findAll().stream()
                .filter(r -> r.getUsuario() != null && r.getUsuario().getUsuarioId().equals(usuarioId))
                .toList();
        
        return registroAccesoMapper.toDtoList(registros);
    }

    /**
     * Obtiene un registro de acceso por su ID
     * @param accesoId ID del registro de acceso
     * @return DTO del registro o Optional vacío
     */
    @Transactional(readOnly = true)
    public Optional<RegistroAccesoDTO> getRegistroAccesoById(Long accesoId) {
        return registroAccesoRepository.findById(accesoId)
                .map(registroAccesoMapper::toDto);
    }
}

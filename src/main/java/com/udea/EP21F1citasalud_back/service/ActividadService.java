package com.udea.EP21F1citasalud_back.service;

import com.udea.EP21F1citasalud_back.entity.ActividadUsuario;
import com.udea.EP21F1citasalud_back.entity.Usuario;
import com.udea.EP21F1citasalud_back.repository.ActividadUsuarioRepository;
import com.udea.EP21F1citasalud_back.repository.UsuarioRepository;
import com.udea.EP21F1citasalud_back.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Servicio para gestionar el registro de actividades de usuario
 * Centraliza la lógica de logging y elimina duplicación de código
 */
@Service
public class ActividadService {

    private final ActividadUsuarioRepository actividadUsuarioRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public ActividadService(ActividadUsuarioRepository actividadUsuarioRepository, 
                          UsuarioRepository usuarioRepository) {
        this.actividadUsuarioRepository = actividadUsuarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Registra una actividad del usuario autenticado
     * @param tipoActividad Tipo de actividad realizada
     * @param descripcion Descripción detallada de la actividad
     * @param detalles Detalles adicionales
     */
    public void registrarActividad(String tipoActividad, String descripcion, String detalles) {
        try {
            Usuario usuarioAccion = obtenerUsuarioAutenticado();
            if (usuarioAccion == null) {
                System.err.println("[LOG ACTIVIDAD] usuarioAccion es null, no se registra actividad");
                return;
            }

            ActividadUsuario actividad = crearActividad(usuarioAccion, tipoActividad, descripcion, detalles);
            actividadUsuarioRepository.save(actividad);
            System.out.println("[LOG ACTIVIDAD] Actividad guardada correctamente");
            
        } catch (Exception e) {
            System.err.println("[LOG ACTIVIDAD] Error guardando actividad: " + e.getMessage());
        }
    }

    /**
     * Registra actividades públicas que no requieren usuario autenticado
     * @param mensaje Mensaje de log para actividades públicas
     */
    public void registrarActividadPublica(String mensaje) {
        System.out.println("[LOG REGISTRO PÚBLICO] " + mensaje);
    }

    /**
     * Obtiene el usuario autenticado del contexto de seguridad
     * @return Usuario autenticado o null si no existe
     */
    private Usuario obtenerUsuarioAutenticado() {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
            return usuarioRepository.findById(userDetails.getId()).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Crea una nueva instancia de ActividadUsuario
     * @param usuario Usuario que realiza la actividad
     * @param tipoActividad Tipo de actividad
     * @param descripcion Descripción de la actividad
     * @param detalles Detalles adicionales
     * @return Nueva instancia de ActividadUsuario
     */
    private ActividadUsuario crearActividad(Usuario usuario, String tipoActividad, 
                                          String descripcion, String detalles) {
        ActividadUsuario actividad = new ActividadUsuario();
        actividad.setUsuario(usuario);
        actividad.setTipoActividad(tipoActividad);
        actividad.setDescripcion(descripcion);
        actividad.setDetalleAdiccionales(detalles);
        actividad.setFechaHora(LocalDateTime.now());
        return actividad;
    }
}

package com.udea.EP21F1citasalud_back.repository;

import com.udea.EP21F1citasalud_back.entity.RegistroAcceso;
import com.udea.EP21F1citasalud_back.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistroAccesoRepository extends JpaRepository<RegistroAcceso, Long> {
    long countByUsuarioAndEstadoAndFechaHoraAfter(Usuario usuario, String estado, java.time.LocalDateTime fechaHora);
}

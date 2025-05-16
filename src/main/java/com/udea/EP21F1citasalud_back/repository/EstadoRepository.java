package com.udea.EP21F1citasalud_back.repository;

import com.udea.EP21F1citasalud_back.entity.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
}
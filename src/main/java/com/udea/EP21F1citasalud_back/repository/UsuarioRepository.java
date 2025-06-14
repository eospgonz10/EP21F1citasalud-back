package com.udea.EP21F1citasalud_back.repository;

import com.udea.EP21F1citasalud_back.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  
    // Método personalizado para buscar usuario por email
    Optional<Usuario> findByEmail(String email);
    
    // Verificar si existe un usuario con un email específico
    boolean existsByEmail(String email);
}
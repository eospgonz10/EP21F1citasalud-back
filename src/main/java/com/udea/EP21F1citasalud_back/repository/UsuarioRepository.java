package com.udea.EP21F1citasalud_back.repository;

import com.udea.EP21F1citasalud_back.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // JpaRepository ya incluye los métodos:
    // - findAll() para obtener todos los usuarios
    // - findById() para obtener un usuario por ID
    // - save() para crear o actualizar un usuario
    // - deleteById() para eliminar un usuario por ID
}
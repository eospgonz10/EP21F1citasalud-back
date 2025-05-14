package com.udea.EP21F1citasalud_back.repository;

import com.udea.EP21F1citasalud_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su email
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca un usuario por su número de documento
     */
    Optional<User> findByDocumento(String documento);

    /**
     * Verifica si existe un usuario con el email dado
     */
    boolean existsByEmail(String email);

    /**
     * Verifica si existe un usuario con el documento dado
     */
    boolean existsByDocumento(String documento);

    /**
     * Busca usuarios por tipo de documento
     */
    List<User> findByTipoDocumento_Id_tipo_documento(Integer idTipoDocumento);

    /**
     * Busca usuarios por estado
     */
    List<User> findByEstado_Id_estado(boolean idEstado);

    /**
     * Busca usuarios registrados en un rango de fechas
     */
    List<User> findByFecha_registroBetween(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Busca usuarios por nombre o apellido conteniendo el texto
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<User> buscarPorNombreOApellido(@Param("texto") String texto);
}

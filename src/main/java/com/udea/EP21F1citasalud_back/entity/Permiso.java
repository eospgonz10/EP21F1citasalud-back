package com.udea.EP21F1citasalud_back.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permisos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permiso_id")
    private Long permisoId;

    @Column(name = "nombre_permiso", length = 50, nullable = false, unique = true)
    private String nombrePermiso;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @ManyToMany(mappedBy = "permisos")
    private Set<Rol> roles = new HashSet<>();
}

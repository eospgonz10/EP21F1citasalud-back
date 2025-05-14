package com.udea.EP21F1citasalud_back.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "permisos")
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permiso_id;

    @Column(nullable = false)
    private String nombre_permiso;

    @Column(length = 100)
    private String descripcion;

    @ManyToMany(mappedBy = "permisos")
    private List<Rol> roles;

    // Constructor vacío
    public Permiso() {
    }

    // Constructor con parámetros
    public Permiso(String nombre_permiso, String descripcion) {
        this.nombre_permiso = nombre_permiso;
        this.descripcion = descripcion;
    }

    // Getters y setters
    public Long getPermiso_id() {
        return permiso_id;
    }

    public void setPermiso_id(Long permiso_id) {
        this.permiso_id = permiso_id;
    }

    public String getNombre_permiso() {
        return nombre_permiso;
    }

    public void setNombre_permiso(String nombre_permiso) {
        this.nombre_permiso = nombre_permiso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }
}
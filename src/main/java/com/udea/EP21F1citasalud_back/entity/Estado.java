package com.udea.EP21F1citasalud_back.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "estado")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_estado;

    @Column(nullable = false, length = 20)
    private String nombre_estado;

    @Column(length = 100)
    private String descripcion;

    @OneToMany(mappedBy = "estado")
    private List<User> usuarios;

    // Constructor vacío
    public Estado() {
    }

    // Constructor con parámetros
    public Estado(String nombre_estado, String descripcion) {
        this.nombre_estado = nombre_estado;
        this.descripcion = descripcion;
    }

    // Getters y setters
    public Integer getId_estado() {
        return id_estado;
    }

    public void setId_estado(Integer id_estado) {
        this.id_estado = id_estado;
    }

    public String getNombre_estado() {
        return nombre_estado;
    }

    public void setNombre_estado(String nombre_estado) {
        this.nombre_estado = nombre_estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<User> usuarios) {
        this.usuarios = usuarios;
    }
}
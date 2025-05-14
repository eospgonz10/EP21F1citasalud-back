package com.udea.EP21F1citasalud_back.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "actividad_usuario")
public class ActividadUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actividad_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Column(nullable = false)
    private String tipo_actividad;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fecha_hora;

    @Column(columnDefinition = "jsonb")
    private String detalle_adiccionales;

    // Constructor vacío
    public ActividadUsuario() {
    }

    // Constructor con parámetros
    public ActividadUsuario(User usuario, String tipo_actividad,
                            String descripcion, LocalDateTime fecha_hora,
                            String detalle_adiccionales) {
        this.usuario = usuario;
        this.tipo_actividad = tipo_actividad;
        this.descripcion = descripcion;
        this.fecha_hora = fecha_hora;
        this.detalle_adiccionales = detalle_adiccionales;
    }

    // Getters y setters
    public Long getActividad_id() {
        return actividad_id;
    }

    public void setActividad_id(Long actividad_id) {
        this.actividad_id = actividad_id;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public String getTipo_actividad() {
        return tipo_actividad;
    }

    public void setTipo_actividad(String tipo_actividad) {
        this.tipo_actividad = tipo_actividad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(LocalDateTime fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public String getDetalle_adiccionales() {
        return detalle_adiccionales;
    }

    public void setDetalle_adiccionales(String detalle_adiccionales) {
        this.detalle_adiccionales = detalle_adiccionales;
    }
}
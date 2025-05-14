package com.udea.EP21F1citasalud_back.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_acceso")
public class RegistroAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long acceso_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Column(nullable = false)
    private LocalDateTime fecha_hora;

    private String direccion_ip;

    private String dispositivo;

    private String ubicacion;

    private String estado;

    private String codigo_error;

    // Constructor vacío
    public RegistroAcceso() {
    }

    // Constructor con parámetros
    public RegistroAcceso(User usuario, LocalDateTime fecha_hora,
                          String direccion_ip, String dispositivo,
                          String ubicacion, String estado, String codigo_error) {
        this.usuario = usuario;
        this.fecha_hora = fecha_hora;
        this.direccion_ip = direccion_ip;
        this.dispositivo = dispositivo;
        this.ubicacion = ubicacion;
        this.estado = estado;
        this.codigo_error = codigo_error;
    }

    // Getters y setters
    public Long getAcceso_id() {
        return acceso_id;
    }

    public void setAcceso_id(Long acceso_id) {
        this.acceso_id = acceso_id;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(LocalDateTime fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public String getDireccion_ip() {
        return direccion_ip;
    }

    public void setDireccion_ip(String direccion_ip) {
        this.direccion_ip = direccion_ip;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigo_error() {
        return codigo_error;
    }

    public void setCodigo_error(String codigo_error) {
        this.codigo_error = codigo_error;
    }
}
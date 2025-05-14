package com.udea.EP21F1citasalud_back.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificacion_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Column(nullable = false)
    private String tipo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    @Column(nullable = false)
    private LocalDateTime fecha_hora;

    @Column(nullable = false)
    private boolean leido;

    private String metodo_envio;

    // Constructor vacío
    public Notificacion() {
    }

    // Constructor con parámetros
    public Notificacion(User usuario, String tipo, String mensaje,
                        LocalDateTime fecha_hora, boolean leido, String metodo_envio) {
        this.usuario = usuario;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fecha_hora = fecha_hora;
        this.leido = leido;
        this.metodo_envio = metodo_envio;
    }

    // Getters y setters
    public Long getNotificacion_id() {
        return notificacion_id;
    }

    public void setNotificacion_id(Long notificacion_id) {
        this.notificacion_id = notificacion_id;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(LocalDateTime fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public String getMetodo_envio() {
        return metodo_envio;
    }

    public void setMetodo_envio(String metodo_envio) {
        this.metodo_envio = metodo_envio;
    }
}
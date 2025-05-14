package com.udea.EP21F1citasalud_back.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "credencial")
public class Credencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long credencial_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Column(nullable = false)
    private String metodo_autenticacion;

    private String token_verificacion;

    private LocalDateTime expiracion_token;

    // Constructor vacío
    public Credencial() {
    }

    // Constructor con parámetros
    public Credencial(User usuario, String metodo_autenticacion,
                      String token_verificacion, LocalDateTime expiracion_token) {
        this.usuario = usuario;
        this.metodo_autenticacion = metodo_autenticacion;
        this.token_verificacion = token_verificacion;
        this.expiracion_token = expiracion_token;
    }

    // Getters y setters
    public Long getCredencial_id() {
        return credencial_id;
    }

    public void setCredencial_id(Long credencial_id) {
        this.credencial_id = credencial_id;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public String getMetodo_autenticacion() {
        return metodo_autenticacion;
    }

    public void setMetodo_autenticacion(String metodo_autenticacion) {
        this.metodo_autenticacion = metodo_autenticacion;
    }

    public String getToken_verificacion() {
        return token_verificacion;
    }

    public void setToken_verificacion(String token_verificacion) {
        this.token_verificacion = token_verificacion;
    }

    public LocalDateTime getExpiracion_token() {
        return expiracion_token;
    }

    public void setExpiracion_token(LocalDateTime expiracion_token) {
        this.expiracion_token = expiracion_token;
    }
}
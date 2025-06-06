package com.udea.EP21F1citasalud_back.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registro_acceso")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RegistroAcceso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accesoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private LocalDateTime fechaHora;

    private String direccionIp;

    private String estado;

    private String codigoError;

    // Getters y setters
    public Long getAccesoId() { return accesoId; }
    public void setAccesoId(Long accesoId) { this.accesoId = accesoId; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getDireccionIp() { return direccionIp; }
    public void setDireccionIp(String direccionIp) { this.direccionIp = direccionIp; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCodigoError() { return codigoError; }
    public void setCodigoError(String codigoError) { this.codigoError = codigoError; }
}

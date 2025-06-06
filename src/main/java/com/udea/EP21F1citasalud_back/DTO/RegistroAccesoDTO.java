package com.udea.EP21F1citasalud_back.DTO;

import java.time.LocalDateTime;

public class RegistroAccesoDTO {
    private Long accesoId;
    private Long usuarioId;
    private String usuarioEmail;
    private LocalDateTime fechaHora;
    private String direccionIp;
    private String estado;
    private String codigoError;

    // Getters y setters
    public Long getAccesoId() { return accesoId; }
    public void setAccesoId(Long accesoId) { this.accesoId = accesoId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getUsuarioEmail() { return usuarioEmail; }
    public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getDireccionIp() { return direccionIp; }
    public void setDireccionIp(String direccionIp) { this.direccionIp = direccionIp; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCodigoError() { return codigoError; }
    public void setCodigoError(String codigoError) { this.codigoError = codigoError; }
}

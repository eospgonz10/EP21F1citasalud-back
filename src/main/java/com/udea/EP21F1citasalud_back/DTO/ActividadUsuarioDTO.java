package com.udea.EP21F1citasalud_back.DTO;

import java.time.LocalDateTime;

public class ActividadUsuarioDTO {
    private Long actividadId;
    private Long usuarioId;
    private String usuarioEmail;
    private String tipoActividad;
    private String descripcion;
    private LocalDateTime fechaHora;
    private String detalleAdiccionales;

    // Getters y setters
    public Long getActividadId() { return actividadId; }
    public void setActividadId(Long actividadId) { this.actividadId = actividadId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getUsuarioEmail() { return usuarioEmail; }
    public void setUsuarioEmail(String usuarioEmail) { this.usuarioEmail = usuarioEmail; }
    public String getTipoActividad() { return tipoActividad; }
    public void setTipoActividad(String tipoActividad) { this.tipoActividad = tipoActividad; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getDetalleAdiccionales() { return detalleAdiccionales; }
    public void setDetalleAdiccionales(String detalleAdiccionales) { this.detalleAdiccionales = detalleAdiccionales; }
}

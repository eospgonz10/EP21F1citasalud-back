package com.udea.EP21F1citasalud_back.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "actividad_usuario")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ActividadUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actividadId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private String tipoActividad;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private LocalDateTime fechaHora;

    @Column(columnDefinition = "TEXT")
    private String detalleAdiccionales;

    // Getters y setters
    public Long getActividadId() { return actividadId; }
    public void setActividadId(Long actividadId) { this.actividadId = actividadId; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getTipoActividad() { return tipoActividad; }
    public void setTipoActividad(String tipoActividad) { this.tipoActividad = tipoActividad; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getDetalleAdiccionales() { return detalleAdiccionales; }
    public void setDetalleAdiccionales(String detalleAdiccionales) { this.detalleAdiccionales = detalleAdiccionales; }
}

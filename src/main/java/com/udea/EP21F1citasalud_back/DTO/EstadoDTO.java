package com.udea.EP21F1citasalud_back.DTO;

import java.util.Objects;

public class EstadoDTO {

    private Integer idEstado;
    private String nombreEstado;
    private String descripcion;

    // Constructores
    public EstadoDTO() {
    }

    public EstadoDTO(Integer idEstado, String nombreEstado, String descripcion) {
        this.idEstado = idEstado;
        this.nombreEstado = nombreEstado;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Integer getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstadoDTO estadoDTO = (EstadoDTO) o;
        return Objects.equals(idEstado, estadoDTO.idEstado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEstado);
    }

    @Override
    public String toString() {
        return "EstadoDTO{" +
                "idEstado=" + idEstado +
                ", nombreEstado='" + nombreEstado + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}

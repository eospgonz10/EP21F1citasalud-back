package com.udea.EP21F1citasalud_back.DTO;

import java.util.HashSet;
import java.util.Set;

public class RolDTO {
    private Integer rolId;
    private String nombreRol;
    private String descripcion;
    private Set<PermisoDTO> permisos = new HashSet<>();

    public RolDTO() {
    }

    public RolDTO(Integer rolId, String nombreRol, String descripcion) {
        this.rolId = rolId;
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
    }

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<PermisoDTO> getPermisos() {
        return permisos;
    }

    public void setPermisos(Set<PermisoDTO> permisos) {
        this.permisos = permisos;
    }
}

package com.udea.EP21F1citasalud_back.DTO;

public class PermisoDTO {
    private Long permisoId;
    private String nombrePermiso;
    private String descripcion;

    public PermisoDTO() {
    }

    public PermisoDTO(Long permisoId, String nombrePermiso, String descripcion) {
        this.permisoId = permisoId;
        this.nombrePermiso = nombrePermiso;
        this.descripcion = descripcion;
    }

    public Long getPermisoId() {
        return permisoId;
    }

    public void setPermisoId(Long permisoId) {
        this.permisoId = permisoId;
    }

    public String getNombrePermiso() {
        return nombrePermiso;
    }

    public void setNombrePermiso(String nombrePermiso) {
        this.nombrePermiso = nombrePermiso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

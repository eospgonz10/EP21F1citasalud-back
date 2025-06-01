package com.udea.EP21F1citasalud_back.DTO;

import java.time.LocalDate;
import java.util.Objects;

public class UsuarioDTO {

    private Long usuarioId; // No incluir en el JSON de creación
    private String nombre;
    private String apellido;
    private String email;
    private String documento;
    private Integer tipoDocumento; // Solo el ID
    private String password;
    private String telefono;
    private LocalDate fechaRegistro;
    private LocalDate ultimoAcceso;
    private Integer estado; // Solo el ID
    private Integer rolId; // Solo el ID del rol

    public UsuarioDTO() {}

    public UsuarioDTO(Long usuarioId, String nombre, String apellido, String email,
                      String documento, Integer tipoDocumento, String password,
                      String telefono, LocalDate fechaRegistro, LocalDate ultimoAcceso,
                      Integer estado, Integer rolId) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.documento = documento;
        this.tipoDocumento = tipoDocumento;
        this.password = password;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
        this.ultimoAcceso = ultimoAcceso;
        this.estado = estado;
        this.rolId = rolId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Integer getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(Integer tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDate getUltimoAcceso() {
        return ultimoAcceso;
    }

    public void setUltimoAcceso(LocalDate ultimoAcceso) {
        this.ultimoAcceso = ultimoAcceso;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioDTO usuarioDTO = (UsuarioDTO) o;
        return Objects.equals(usuarioId, usuarioDTO.usuarioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuarioId);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "usuarioId=" + usuarioId +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", documento='" + documento + '\'' +
                ", tipoDocumento=" + tipoDocumento +
                ", telefono='" + telefono + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                ", ultimoAcceso=" + ultimoAcceso +
                ", estado=" + estado +
                ", rolId=" + rolId +
                '}';
    }
}
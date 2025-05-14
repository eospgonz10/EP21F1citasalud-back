package com.udea.EP21F1citasalud_back.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuario_id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 20)
    private String documento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_documento", nullable = false)
    private TipoDocumento tipoDocumento;

    @Column(nullable = false, length = 200)
    private String password;

    @Column(nullable = false, length = 20)
    private String telefono;

    @Column(nullable = false)
    private LocalDate fecha_registro;

    private LocalDate ultimo_acceso;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Credencial> credenciales;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<RegistroAcceso> registrosAcceso;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Notificacion> notificaciones;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<ActividadUsuario> actividades;

    @ManyToMany
    @JoinTable(
            name = "roles_usuario",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private List<Rol> roles;

    public User() {
    }

    public User(String nombre, String apellido, String email,
                String documento, TipoDocumento tipoDocumento,
                String password, String telefono,
                LocalDate fecha_registro, LocalDate ultimo_acceso,
                Estado estado) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.documento = documento;
        this.tipoDocumento = tipoDocumento;
        this.password = password;
        this.telefono = telefono;
        this.fecha_registro = fecha_registro;
        this.ultimo_acceso = ultimo_acceso;
        this.estado = estado;
    }

    // Getters y setters
    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long usuario_id) {
        this.usuario_id = usuario_id;
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

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
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

    public LocalDate getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(LocalDate fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public LocalDate getUltimo_acceso() {
        return ultimo_acceso;
    }

    public void setUltimo_acceso(LocalDate ultimo_acceso) {
        this.ultimo_acceso = ultimo_acceso;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public List<Credencial> getCredenciales() {
        return credenciales;
    }

    public void setCredenciales(List<Credencial> credenciales) {
        this.credenciales = credenciales;
    }

    public List<RegistroAcceso> getRegistrosAcceso() {
        return registrosAcceso;
    }

    public void setRegistrosAcceso(List<RegistroAcceso> registrosAcceso) {
        this.registrosAcceso = registrosAcceso;
    }

    public List<Notificacion> getNotificaciones() {
        return notificaciones;
    }

    public void setNotificaciones(List<Notificacion> notificaciones) {
        this.notificaciones = notificaciones;
    }

    public List<ActividadUsuario> getActividades() {
        return actividades;
    }

    public void setActividades(List<ActividadUsuario> actividades) {
        this.actividades = actividades;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public Object isEstado() {
    }
}
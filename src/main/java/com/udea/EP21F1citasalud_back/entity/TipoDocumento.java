package com.udea.EP21F1citasalud_back.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tipo_documento")
public class TipoDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_tipo_documento;

    @Column(nullable = false)
    private String tipo_documento;

    @OneToMany(mappedBy = "tipoDocumento")
    private List<User> usuarios;

    public TipoDocumento() {
    }

    public TipoDocumento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    // Getters y setters
    public Integer getId_tipo_documento() {
        return id_tipo_documento;
    }

    public void setId_tipo_documento(Integer id_tipo_documento) {
        this.id_tipo_documento = id_tipo_documento;
    }

    public String getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(String tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public List<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<User> usuarios) {
        this.usuarios = usuarios;
    }
}
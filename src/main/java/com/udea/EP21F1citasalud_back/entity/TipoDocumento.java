package com.udea.EP21F1citasalud_back.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tipo_documento")
public class TipoDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_documento")
    private Integer idTipoDocumento;

    @Column(name = "tipo_documento", nullable = false)
    private String tipoDocumento;

    // Constructores
    public TipoDocumento() {
    }

    // Getters y Setters
    public Integer getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(Integer idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoDocumento tipoDoc = (TipoDocumento) o;
        return Objects.equals(idTipoDocumento, tipoDoc.idTipoDocumento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTipoDocumento);
    }

    @Override
    public String toString() {
        return "TipoDocumento{" +
                "idTipoDocumento=" + idTipoDocumento +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                '}';
    }
}
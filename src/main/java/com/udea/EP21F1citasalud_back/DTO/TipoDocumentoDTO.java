package com.udea.EP21F1citasalud_back.DTO;

import java.util.Objects;

public class TipoDocumentoDTO {

    private Integer idTipoDocumento;
    private String tipoDocumento;

    // Constructores
    public TipoDocumentoDTO() {
    }

    public TipoDocumentoDTO(Integer idTipoDocumento, String tipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
        this.tipoDocumento = tipoDocumento;
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
        TipoDocumentoDTO that = (TipoDocumentoDTO) o;
        return Objects.equals(idTipoDocumento, that.idTipoDocumento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTipoDocumento);
    }

    @Override
    public String toString() {
        return "TipoDocumentoDTO{" +
                "idTipoDocumento=" + idTipoDocumento +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                '}';
    }
}
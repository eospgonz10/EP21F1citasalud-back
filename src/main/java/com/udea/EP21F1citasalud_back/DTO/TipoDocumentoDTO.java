package com.udea.EP21F1citasalud_back.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoDocumentoDTO {
    private Integer idTipoDocumento;
    private String tipoDocumento;
}
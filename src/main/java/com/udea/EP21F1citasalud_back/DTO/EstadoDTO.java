package com.udea.EP21F1citasalud_back.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoDTO {
    private Integer idEstado;
    private String nombreEstado;
    private String descripcion;
}

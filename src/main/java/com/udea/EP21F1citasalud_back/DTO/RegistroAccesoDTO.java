package com.udea.EP21F1citasalud_back.DTO;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroAccesoDTO {
    private Long accesoId;
    private Long usuarioId;
    private String usuarioEmail;
    private LocalDateTime fechaHora;
    private String direccionIp;
    private String estado;
    private String codigoError;
}

package com.udea.EP21F1citasalud_back.DTO;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActividadUsuarioDTO {
    private Long actividadId;
    private Long usuarioId;
    private String usuarioEmail;
    private String tipoActividad;
    private String descripcion;
    private LocalDateTime fechaHora;
    private String detalleAdiccionales;
}

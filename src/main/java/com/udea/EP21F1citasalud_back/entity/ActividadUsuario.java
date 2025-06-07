package com.udea.EP21F1citasalud_back.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "actividad_usuario")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActividadUsuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long actividadId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private String tipoActividad;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private LocalDateTime fechaHora;

    @Column(columnDefinition = "TEXT")
    private String detalleAdiccionales;
}

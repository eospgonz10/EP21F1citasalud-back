package com.udea.EP21F1citasalud_back.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "registro_acceso")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroAcceso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accesoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private LocalDateTime fechaHora;

    private String direccionIp;

    private String estado;

    private String codigoError;
}

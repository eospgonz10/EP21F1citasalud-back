package com.udea.EP21F1citasalud_back.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwoFactorRequest {
    @NotBlank(message = "El email es obligatorio")
    private String email;
    @NotBlank(message = "El código es obligatorio")
    private String code;
}

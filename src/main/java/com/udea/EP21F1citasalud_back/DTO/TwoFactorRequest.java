package com.udea.EP21F1citasalud_back.DTO;

import jakarta.validation.constraints.NotBlank;

public class TwoFactorRequest {
    @NotBlank(message = "El email es obligatorio")
    private String email;
    @NotBlank(message = "El código es obligatorio")
    private String code;

    public TwoFactorRequest() {}
    public TwoFactorRequest(String email, String code) {
        this.email = email;
        this.code = code;
    }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}

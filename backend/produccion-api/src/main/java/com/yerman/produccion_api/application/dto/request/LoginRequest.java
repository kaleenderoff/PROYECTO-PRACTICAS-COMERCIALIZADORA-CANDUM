package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class LoginRequest {

    @NotBlank(message = "La cédula es obligatoria")
    @Pattern(regexp = "^[0-9]{6,20}$", message = "La cédula debe contener solo números y tener entre 6 y 20 dígitos")
    private String cc;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
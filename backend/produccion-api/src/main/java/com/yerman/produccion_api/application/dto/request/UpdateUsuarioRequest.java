package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateUsuarioRequest {

    @NotBlank(message = "La cedula es obligatoria")
    @Pattern(regexp = "^[0-9]{6,20}$", message = "La cedula debe contener solo numeros y tener entre 6 y 20 digitos")
    private String cc;

    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(max = 100, message = "El primer nombre no puede superar los 100 caracteres")
    private String primerNombre;

    @Size(max = 100, message = "El segundo nombre no puede superar los 100 caracteres")
    private String segundoNombre;

    @NotBlank(message = "El primer apellido es obligatorio")
    @Size(max = 100, message = "El primer apellido no puede superar los 100 caracteres")
    private String primerApellido;

    @Size(max = 100, message = "El segundo apellido no puede superar los 100 caracteres")
    private String segundoApellido;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(regexp = "JEFE_PRODUCCION|JEFE_LINEA|AUXILIAR_CALIDAD|ANALISTA_LACTEOS|JEFE_PLANTA|GERENCIA|ADMIN", message = "Rol invalido")
    private String rol;

    private boolean activo;

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}

package com.yerman.produccion_api.application.dto.response;

public class AuthResponse {

    private String token;
    private String cc;
    private String rol;
    private String primerNombre;
    private String primerApellido;

    public AuthResponse(String token, String cc, String rol,
            String primerNombre, String primerApellido) {
        this.token = token;
        this.cc = cc;
        this.rol = rol;
        this.primerNombre = primerNombre;
        this.primerApellido = primerApellido;
    }

    public String getToken() {
        return token;
    }

    public String getCc() {
        return cc;
    }

    public String getRol() {
        return rol;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }
}

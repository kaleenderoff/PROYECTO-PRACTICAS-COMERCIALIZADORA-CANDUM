package com.yerman.produccion_api.application.dto.response;

public class AuthResponse {

    private String token;
    private String cc;
    private String rol;
    private String primerNombre;
    private String primerApellido;
    private Long idUsuario;

    public AuthResponse(String token, String cc, String rol,
            String primerNombre, String primerApellido, Long idUsuario) {
        this.token = token;
        this.cc = cc;
        this.rol = rol;
        this.primerNombre = primerNombre;
        this.primerApellido = primerApellido;
        this.idUsuario = idUsuario;
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

    public Long getIdUsuario() {
        return idUsuario;
    }
}

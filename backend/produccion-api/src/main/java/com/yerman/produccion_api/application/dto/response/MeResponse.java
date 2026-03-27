package com.yerman.produccion_api.application.dto.response;

public class MeResponse {

    private String cc;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String rol;

    public MeResponse(String cc,
            String primerNombre,
            String segundoNombre,
            String primerApellido,
            String segundoApellido,
            String rol) {
        this.cc = cc;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.rol = rol;
    }

    public String getCc() {
        return cc;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public String getRol() {
        return rol;
    }
}

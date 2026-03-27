package com.yerman.produccion_api.application.dto.response;

public class UsuarioResponse {

    private Long idUsuario;
    private String cc;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String email;
    private String rol;
    private boolean activo;

    public UsuarioResponse(Long idUsuario,
            String cc,
            String primerNombre,
            String segundoNombre,
            String primerApellido,
            String segundoApellido,
            String email,
            String rol,
            boolean activo) {
        this.idUsuario = idUsuario;
        this.cc = cc;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.email = email;
        this.rol = rol;
        this.activo = activo;
    }

    public Long getIdUsuario() {
        return idUsuario;
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

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }

    public boolean isActivo() {
        return activo;
    }
}

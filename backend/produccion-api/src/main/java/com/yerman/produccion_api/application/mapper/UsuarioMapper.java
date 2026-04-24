package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.request.CreateUsuarioRequest;
import com.yerman.produccion_api.application.dto.request.UpdateUsuarioRequest;
import com.yerman.produccion_api.application.dto.response.UsuarioResponse;
import com.yerman.produccion_api.domain.model.Usuario;

public class UsuarioMapper {

    // 🔹 CREATE → DTO → DOMAIN
    public static Usuario toDomain(CreateUsuarioRequest request) {
        Usuario usuario = new Usuario();

        usuario.setCc(request.getCc());
        usuario.setPrimerNombre(request.getPrimerNombre());
        usuario.setSegundoNombre(request.getSegundoNombre());
        usuario.setPrimerApellido(request.getPrimerApellido());
        usuario.setSegundoApellido(request.getSegundoApellido());
        usuario.setPasswordHash(request.getPassword());
        usuario.setRol(Usuario.Rol.valueOf(request.getRol().toUpperCase()));

        return usuario;
    }

    // 🔹 UPDATE → DTO → DOMAIN
    public static Usuario toDomain(UpdateUsuarioRequest request) {
        Usuario usuario = new Usuario();

        usuario.setCc(request.getCc());
        usuario.setPrimerNombre(request.getPrimerNombre());
        usuario.setSegundoNombre(request.getSegundoNombre());
        usuario.setPrimerApellido(request.getPrimerApellido());
        usuario.setRol(Usuario.Rol.valueOf(request.getRol().toUpperCase()));
        usuario.setActivo(request.isActivo());

        return usuario;
    }

    // 🔹 DOMAIN → RESPONSE
    public static UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getCc(),
                usuario.getPrimerNombre(),
                usuario.getSegundoNombre(),
                usuario.getPrimerApellido(),
                usuario.getSegundoApellido(),
                usuario.getRol().name(),
                usuario.isActivo());
    }
}

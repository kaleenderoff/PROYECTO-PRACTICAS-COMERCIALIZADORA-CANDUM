package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.response.MeResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.domain.model.Usuario;
import com.yerman.produccion_api.domain.port.out.UsuarioRepositoryPort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public UserController(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @GetMapping("/me")
    public MeResponse me(Authentication authentication) {
        String cc = authentication.getName();

        Usuario usuario = usuarioRepositoryPort.buscarPorCc(cc)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + cc));

        return new MeResponse(
                usuario.getCc(),
                usuario.getPrimerNombre(),
                usuario.getSegundoNombre(),
                usuario.getPrimerApellido(),
                usuario.getSegundoApellido(),
                usuario.getRol().name());
    }
}

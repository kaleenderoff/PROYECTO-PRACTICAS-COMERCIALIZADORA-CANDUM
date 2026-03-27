package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.dto.request.LoginRequest;
import com.yerman.produccion_api.application.dto.response.AuthResponse;
import com.yerman.produccion_api.application.exception.UsuarioInactivoException;
import com.yerman.produccion_api.domain.model.Usuario;
import com.yerman.produccion_api.domain.port.in.LoginUseCase;
import com.yerman.produccion_api.domain.port.out.UsuarioRepositoryPort;
import com.yerman.produccion_api.infrastructure.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements LoginUseCase {

        private final AuthenticationManager authenticationManager;
        private final JwtTokenProvider jwtTokenProvider;
        private final UsuarioRepositoryPort usuarioRepositoryPort;

        public LoginService(AuthenticationManager authenticationManager,
                        JwtTokenProvider jwtTokenProvider,
                        UsuarioRepositoryPort usuarioRepositoryPort) {
                this.authenticationManager = authenticationManager;
                this.jwtTokenProvider = jwtTokenProvider;
                this.usuarioRepositoryPort = usuarioRepositoryPort;
        }

        @Override
        public AuthResponse login(LoginRequest request) {

                Usuario usuario = usuarioRepositoryPort
                                .buscarPorCc(request.getCc())
                                .orElseThrow(() -> new RuntimeException(
                                                "Usuario no encontrado: " + request.getCc()));

                if (!usuario.isActivo()) {
                        throw new UsuarioInactivoException("Usuario inactivo");
                }

                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getCc(),
                                                request.getPassword()));

                String token = jwtTokenProvider.generarToken(
                                usuario.getCc(),
                                usuario.getRol().name());

                return new AuthResponse(
                                token,
                                usuario.getCc(),
                                usuario.getRol().name(),
                                usuario.getPrimerNombre(),
                                usuario.getPrimerApellido());
        }
}

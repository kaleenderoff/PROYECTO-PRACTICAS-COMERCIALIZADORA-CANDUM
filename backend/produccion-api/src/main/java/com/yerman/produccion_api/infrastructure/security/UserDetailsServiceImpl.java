package com.yerman.produccion_api.infrastructure.security;

import com.yerman.produccion_api.domain.model.Usuario;
import com.yerman.produccion_api.domain.port.out.UsuarioRepositoryPort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public UserDetailsServiceImpl(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public UserDetails loadUserByUsername(String cc) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositoryPort.buscarPorCc(cc)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con cc: " + cc));

        if (!usuario.isActivo()) {
            throw new UsernameNotFoundException("Usuario inactivo: " + cc);
        }

        return new User(
                usuario.getCc(),
                usuario.getPasswordHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())));
    }
}

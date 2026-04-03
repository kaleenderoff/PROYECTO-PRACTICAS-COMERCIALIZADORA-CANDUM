package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.dto.response.PageResponse;
import com.yerman.produccion_api.application.dto.response.UsuarioResponse;
import com.yerman.produccion_api.application.exception.CcDuplicadaException;
import com.yerman.produccion_api.application.exception.PasswordIncorrectaException;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.Usuario;
import com.yerman.produccion_api.domain.port.in.GestionUsuarioUseCase;
import com.yerman.produccion_api.domain.port.out.UsuarioRepositoryPort;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GestionUsuarioService implements GestionUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public GestionUsuarioService(UsuarioRepositoryPort usuarioRepositoryPort,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        validarCcUnicaParaCrear(usuario.getCc());

        usuario.setCc(limpiar(usuario.getCc()));
        usuario.setPrimerNombre(limpiar(usuario.getPrimerNombre()));
        usuario.setSegundoNombre(limpiarOpcional(usuario.getSegundoNombre()));
        usuario.setPrimerApellido(limpiar(usuario.getPrimerApellido()));
        usuario.setSegundoApellido(limpiarOpcional(usuario.getSegundoApellido()));
        usuario.setEmail(limpiarOpcional(usuario.getEmail()));

        usuario.setActivo(true);
        usuario.setCreatedAt(LocalDateTime.now());
        usuario.setUpdatedAt(LocalDateTime.now());

        if (usuario.getPasswordHash() != null && !usuario.getPasswordHash().isBlank()) {
            usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        }

        return usuarioRepositoryPort.guardar(usuario);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepositoryPort.buscarPorId(id);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepositoryPort.buscarPorEmail(email);
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepositoryPort.listarTodos();
    }

    @Override
    public List<Usuario> listarUsuariosActivos() {
        return usuarioRepositoryPort.listarActivos();
    }

    @Override
    public List<Usuario> listarPorRol(String rol) {
        Usuario.Rol rolEnum = Usuario.Rol.valueOf(rol.toUpperCase());
        return usuarioRepositoryPort.buscarPorRolYActivo(rolEnum);
    }

    @Override
    public PageResponse<UsuarioResponse> listarUsuariosActivosPaginado(int page, int size) {
        validarPaginacion(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("idUsuario").ascending());
        Page<Usuario> usuariosPage = usuarioRepositoryPort.listarActivosPaginado(pageable);

        return construirPageResponse(usuariosPage);
    }

    @Override
    public PageResponse<UsuarioResponse> listarTodosPaginado(int page, int size) {
        validarPaginacion(page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("idUsuario").ascending());
        Page<Usuario> usuariosPage = usuarioRepositoryPort.listarTodosPaginado(pageable);

        return construirPageResponse(usuariosPage);
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));

        String ccNueva = limpiar(usuarioActualizado.getCc());
        validarCcUnicaParaActualizar(id, ccNueva);

        usuarioExistente.setCc(ccNueva);
        usuarioExistente.setPrimerNombre(limpiar(usuarioActualizado.getPrimerNombre()));
        usuarioExistente.setSegundoNombre(limpiarOpcional(usuarioActualizado.getSegundoNombre()));
        usuarioExistente.setPrimerApellido(limpiar(usuarioActualizado.getPrimerApellido()));
        usuarioExistente.setSegundoApellido(limpiarOpcional(usuarioActualizado.getSegundoApellido()));
        usuarioExistente.setEmail(limpiarOpcional(usuarioActualizado.getEmail()));
        usuarioExistente.setRol(usuarioActualizado.getRol());
        usuarioExistente.setActivo(usuarioActualizado.isActivo());
        usuarioExistente.setUpdatedAt(LocalDateTime.now());

        if (usuarioActualizado.getPasswordHash() != null && !usuarioActualizado.getPasswordHash().isBlank()) {
            usuarioExistente.setPasswordHash(passwordEncoder.encode(usuarioActualizado.getPasswordHash()));
        }

        return usuarioRepositoryPort.guardar(usuarioExistente);
    }

    @Override
    public void desactivarUsuario(Long id) {
        Usuario usuario = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));

        usuario.setActivo(false);
        usuario.setUpdatedAt(LocalDateTime.now());
        usuarioRepositoryPort.guardar(usuario);
    }

    @Override
    public void activarUsuario(Long id) {
        Usuario usuario = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));

        usuario.setActivo(true);
        usuario.setUpdatedAt(LocalDateTime.now());
        usuarioRepositoryPort.guardar(usuario);
    }

    @Override
    public void resetearPassword(Long id, String nuevaPassword) {
        Usuario usuario = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));

        usuario.setPasswordHash(passwordEncoder.encode(nuevaPassword));
        usuario.setUpdatedAt(LocalDateTime.now());

        usuarioRepositoryPort.guardar(usuario);
    }

    @Override
    public void cambiarMiPassword(String cc, String passwordActual, String nuevaPassword) {
        Usuario usuario = usuarioRepositoryPort.buscarPorCc(cc)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con cc: " + cc));

        if (!passwordEncoder.matches(passwordActual, usuario.getPasswordHash())) {
            throw new PasswordIncorrectaException("La contraseña actual no es correcta");
        }

        usuario.setPasswordHash(passwordEncoder.encode(nuevaPassword));
        usuario.setUpdatedAt(LocalDateTime.now());

        usuarioRepositoryPort.guardar(usuario);
    }

    private void validarCcUnicaParaCrear(String cc) {
        String ccLimpia = limpiar(cc);
        if (usuarioRepositoryPort.existePorCc(ccLimpia)) {
            throw new CcDuplicadaException("Ya existe un usuario con la cédula " + ccLimpia);
        }
    }

    private void validarCcUnicaParaActualizar(Long idUsuarioActual, String ccNueva) {
        Optional<Usuario> usuarioConMismaCc = usuarioRepositoryPort.buscarPorCc(ccNueva);

        if (usuarioConMismaCc.isPresent()
                && !usuarioConMismaCc.get().getIdUsuario().equals(idUsuarioActual)) {
            throw new CcDuplicadaException("Ya existe un usuario con la cédula " + ccNueva);
        }
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }

    private String limpiarOpcional(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }

    private void validarPaginacion(int page, int size) {
        if (page < 0) {
            throw new ReglaNegocioException("El número de página no puede ser negativo");
        }
        if (size <= 0) {
            throw new ReglaNegocioException("El tamaño de página debe ser mayor que cero");
        }
    }

    private PageResponse<UsuarioResponse> construirPageResponse(Page<Usuario> page) {
        List<UsuarioResponse> content = page.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast());
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getCc(),
                usuario.getPrimerNombre(),
                usuario.getSegundoNombre(),
                usuario.getPrimerApellido(),
                usuario.getSegundoApellido(),
                usuario.getEmail(),
                usuario.getRol().name(),
                usuario.isActivo());
    }
}

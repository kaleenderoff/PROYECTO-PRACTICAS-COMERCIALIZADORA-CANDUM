package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.dto.response.PageResponse;
import com.yerman.produccion_api.application.dto.response.UsuarioResponse;
import com.yerman.produccion_api.application.exception.CcDuplicadaException;
import com.yerman.produccion_api.application.exception.PasswordIncorrectaException;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.application.mapper.UsuarioMapper;
import com.yerman.produccion_api.domain.model.Usuario;
import com.yerman.produccion_api.domain.port.in.GestionUsuarioUseCase;
import com.yerman.produccion_api.domain.port.out.UsuarioRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GestionUsuarioService implements GestionUsuarioUseCase {

    private static final String MENSAJE_USUARIO_NO_ENCONTRADO_ID = "Usuario no encontrado con id: ";

    private final UsuarioRepositoryPort usuarioRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public GestionUsuarioService(
            UsuarioRepositoryPort usuarioRepositoryPort,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario crearUsuario(Usuario usuario) {
        validarUsuarioObligatorio(usuario);
        validarCamposObligatoriosCreacion(usuario);

        String ccNormalizada = normalizarTextoObligatorio(usuario.getCc());
        String primerNombreNormalizado = normalizarTextoObligatorio(usuario.getPrimerNombre());
        String segundoNombreNormalizado = normalizarTextoOpcional(usuario.getSegundoNombre());
        String primerApellidoNormalizado = normalizarTextoObligatorio(usuario.getPrimerApellido());
        String segundoApellidoNormalizado = normalizarTextoOpcional(usuario.getSegundoApellido());
        String emailNormalizado = normalizarEmailOpcional(usuario.getEmail());

        validarCcUnica(ccNormalizada);
        validarEmailUnicoSiAplica(emailNormalizado);

        usuario.setCc(ccNormalizada);
        usuario.setPrimerNombre(primerNombreNormalizado);
        usuario.setSegundoNombre(segundoNombreNormalizado);
        usuario.setPrimerApellido(primerApellidoNormalizado);
        usuario.setSegundoApellido(segundoApellidoNormalizado);
        usuario.setEmail(emailNormalizado);
        usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        usuario.setCreatedAt(LocalDateTime.now());
        usuario.setUpdatedAt(LocalDateTime.now());

        return usuarioRepositoryPort.guardar(usuario);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        if (id == null) {
            throw new ReglaNegocioException("El id del usuario es obligatorio");
        }
        return usuarioRepositoryPort.buscarPorId(id);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorCc(String cc) {
        if (cc == null || cc.trim().isEmpty()) {
            throw new ReglaNegocioException("La cédula es obligatoria");
        }
        return usuarioRepositoryPort.buscarPorCc(cc.trim());
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return Optional.empty();
        }
        return usuarioRepositoryPort.buscarPorEmail(email.trim().toLowerCase());
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
        if (rol == null || rol.trim().isEmpty()) {
            throw new ReglaNegocioException("El rol es obligatorio");
        }

        try {
            Usuario.Rol rolEnum = Usuario.Rol.valueOf(rol.trim().toUpperCase());
            return usuarioRepositoryPort.buscarPorRolYActivo(rolEnum);
        } catch (IllegalArgumentException ex) {
            throw new ReglaNegocioException("Rol no válido: " + rol);
        }
    }

    @Override
    public PageResponse<UsuarioResponse> listarUsuariosActivosPaginado(int page, int size) {
        validarParametrosPaginacion(page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<Usuario> usuariosPage = usuarioRepositoryPort.listarActivosPaginado(pageable);

        List<UsuarioResponse> content = usuariosPage.getContent()
                .stream()
                .map(UsuarioMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                usuariosPage.getNumber(),
                usuariosPage.getSize(),
                usuariosPage.getTotalElements(),
                usuariosPage.getTotalPages(),
                usuariosPage.isLast());
    }

    @Override
    public PageResponse<UsuarioResponse> listarTodosPaginado(int page, int size) {
        validarParametrosPaginacion(page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<Usuario> usuariosPage = usuarioRepositoryPort.listarTodosPaginado(pageable);

        List<UsuarioResponse> content = usuariosPage.getContent()
                .stream()
                .map(UsuarioMapper::toResponse)
                .toList();

        return new PageResponse<>(
                content,
                usuariosPage.getNumber(),
                usuariosPage.getSize(),
                usuariosPage.getTotalElements(),
                usuariosPage.getTotalPages(),
                usuariosPage.isLast());
    }

    @Override
    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        if (id == null) {
            throw new ReglaNegocioException("El id del usuario es obligatorio");
        }

        validarUsuarioObligatorio(usuario);

        Usuario existente = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        MENSAJE_USUARIO_NO_ENCONTRADO_ID + id));

        if (usuario.getPrimerNombre() != null && !usuario.getPrimerNombre().trim().isEmpty()) {
            existente.setPrimerNombre(normalizarTextoObligatorio(usuario.getPrimerNombre()));
        }

        existente.setSegundoNombre(normalizarTextoOpcional(usuario.getSegundoNombre()));

        if (usuario.getPrimerApellido() != null && !usuario.getPrimerApellido().trim().isEmpty()) {
            existente.setPrimerApellido(normalizarTextoObligatorio(usuario.getPrimerApellido()));
        }

        existente.setSegundoApellido(normalizarTextoOpcional(usuario.getSegundoApellido()));

        String emailNormalizado = normalizarEmailOpcional(usuario.getEmail());
        if (emailNormalizado != null
                && (existente.getEmail() == null || !emailNormalizado.equalsIgnoreCase(existente.getEmail()))
                && usuarioRepositoryPort.existePorEmail(emailNormalizado)) {
            throw new ReglaNegocioException("Ya existe un usuario con el email: " + emailNormalizado);
        }
        existente.setEmail(emailNormalizado);

        if (usuario.getRol() != null) {
            existente.setRol(usuario.getRol());
        }

        existente.setActivo(usuario.isActivo());
        existente.setUpdatedAt(LocalDateTime.now());

        return usuarioRepositoryPort.guardar(existente);
    }

    @Override
    public void desactivarUsuario(Long id) {
        Usuario usuario = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        MENSAJE_USUARIO_NO_ENCONTRADO_ID + id));

        usuario.setActivo(false);
        usuario.setUpdatedAt(LocalDateTime.now());
        usuarioRepositoryPort.guardar(usuario);
    }

    @Override
    public void activarUsuario(Long id) {
        Usuario usuario = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        MENSAJE_USUARIO_NO_ENCONTRADO_ID + id));

        usuario.setActivo(true);
        usuario.setUpdatedAt(LocalDateTime.now());
        usuarioRepositoryPort.guardar(usuario);
    }

    @Override
    public void resetearPassword(Long id, String nuevaPassword) {
        if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
            throw new ReglaNegocioException("La nueva contraseña es obligatoria");
        }

        Usuario usuario = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        MENSAJE_USUARIO_NO_ENCONTRADO_ID + id));

        usuario.setPasswordHash(passwordEncoder.encode(nuevaPassword.trim()));
        usuario.setUpdatedAt(LocalDateTime.now());
        usuarioRepositoryPort.guardar(usuario);
    }

    @Override
    public void cambiarMiPassword(String cc, String passwordActual, String nuevaPassword) {
        if (cc == null || cc.trim().isEmpty()) {
            throw new ReglaNegocioException("La cédula del usuario autenticado es obligatoria");
        }

        if (passwordActual == null || passwordActual.trim().isEmpty()) {
            throw new ReglaNegocioException("La contraseña actual es obligatoria");
        }

        if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
            throw new ReglaNegocioException("La nueva contraseña es obligatoria");
        }

        Usuario usuario = usuarioRepositoryPort.buscarPorCc(cc.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con cc: " + cc));

        if (!passwordEncoder.matches(passwordActual, usuario.getPasswordHash())) {
            throw new PasswordIncorrectaException("La contraseña actual es incorrecta");
        }

        usuario.setPasswordHash(passwordEncoder.encode(nuevaPassword.trim()));
        usuario.setUpdatedAt(LocalDateTime.now());
        usuarioRepositoryPort.guardar(usuario);
    }

    private void validarUsuarioObligatorio(Usuario usuario) {
        if (usuario == null) {
            throw new ReglaNegocioException("El usuario es obligatorio");
        }
    }

    private void validarCamposObligatoriosCreacion(Usuario usuario) {
        if (usuario.getCc() == null || usuario.getCc().trim().isEmpty()) {
            throw new ReglaNegocioException("La cédula es obligatoria");
        }

        if (usuario.getPrimerNombre() == null || usuario.getPrimerNombre().trim().isEmpty()) {
            throw new ReglaNegocioException("El primer nombre es obligatorio");
        }

        if (usuario.getPrimerApellido() == null || usuario.getPrimerApellido().trim().isEmpty()) {
            throw new ReglaNegocioException("El primer apellido es obligatorio");
        }

        if (usuario.getPasswordHash() == null || usuario.getPasswordHash().trim().isEmpty()) {
            throw new ReglaNegocioException("La contraseña es obligatoria");
        }

        if (usuario.getRol() == null) {
            throw new ReglaNegocioException("El rol es obligatorio");
        }
    }

    private void validarCcUnica(String cc) {
        if (usuarioRepositoryPort.existePorCc(cc)) {
            throw new CcDuplicadaException("Ya existe un usuario con la cédula: " + cc);
        }
    }

    private void validarEmailUnicoSiAplica(String email) {
        if (email != null && usuarioRepositoryPort.existePorEmail(email)) {
            throw new ReglaNegocioException("Ya existe un usuario con el email: " + email);
        }
    }

    private void validarParametrosPaginacion(int page, int size) {
        if (page < 0) {
            throw new ReglaNegocioException("El número de página no puede ser negativo");
        }
        if (size <= 0) {
            throw new ReglaNegocioException("El tamaño de página debe ser mayor que cero");
        }
    }

    private String normalizarTextoObligatorio(String valor) {
        return valor.trim();
    }

    private String normalizarTextoOpcional(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }

    private String normalizarEmailOpcional(String email) {
        if (email == null) {
            return null;
        }
        String limpio = email.trim().toLowerCase();
        return limpio.isEmpty() ? null : limpio;
    }
}
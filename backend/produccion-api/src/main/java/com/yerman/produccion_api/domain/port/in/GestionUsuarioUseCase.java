package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.application.dto.response.PageResponse;
import com.yerman.produccion_api.application.dto.response.UsuarioResponse;
import com.yerman.produccion_api.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface GestionUsuarioUseCase {

    Usuario crearUsuario(Usuario usuario);

    Optional<Usuario> obtenerUsuarioPorId(Long id);

    Optional<Usuario> obtenerUsuarioPorCc(String cc);

    List<Usuario> listarUsuarios();

    List<Usuario> listarUsuariosActivos();

    List<Usuario> listarPorRol(String rol);

    PageResponse<UsuarioResponse> listarUsuariosActivosPaginado(int page, int size);

    PageResponse<UsuarioResponse> listarTodosPaginado(int page, int size);

    Usuario actualizarUsuario(Long id, Usuario usuario);

    void desactivarUsuario(Long id);

    void activarUsuario(Long id);

    void resetearPassword(Long id, String nuevaPassword);

    void cambiarMiPassword(String cc, String passwordActual, String nuevaPassword);
}
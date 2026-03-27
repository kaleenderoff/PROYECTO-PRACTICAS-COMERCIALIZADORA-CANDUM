package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryPort {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorCc(String cc);

    Optional<Usuario> buscarPorEmail(String email);

    List<Usuario> buscarPorRolYActivo(Usuario.Rol rol);

    List<Usuario> listarTodos();

    List<Usuario> listarActivos();

    Page<Usuario> listarActivosPaginado(Pageable pageable);

    Page<Usuario> listarTodosPaginado(Pageable pageable);

    void eliminar(Long id);

    boolean existePorCc(String cc);

    boolean existePorEmail(String email);
}

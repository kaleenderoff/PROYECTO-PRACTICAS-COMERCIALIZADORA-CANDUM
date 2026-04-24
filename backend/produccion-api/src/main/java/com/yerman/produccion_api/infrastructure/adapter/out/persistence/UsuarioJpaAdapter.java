package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.domain.model.Usuario;
import com.yerman.produccion_api.domain.port.out.UsuarioRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.repository.UsuarioJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsuarioJpaAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository repository;

    public UsuarioJpaAdapter(UsuarioJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity entity = toEntity(usuario);
        return toDomain(repository.save(entity));
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorCc(String cc) {
        return repository.findByCc(cc).map(this::toDomain);
    }

    @Override
    public List<Usuario> buscarPorRolYActivo(Usuario.Rol rol) {
        return repository.findByRolAndActivoTrue(UsuarioEntity.Rol.valueOf(rol.name()))
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Usuario> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Usuario> listarActivos() {
        return repository.findByActivoTrue()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Page<Usuario> listarActivosPaginado(Pageable pageable) {
        return repository.findByActivoTrue(pageable).map(this::toDomain);
    }

    @Override
    public Page<Usuario> listarTodosPaginado(Pageable pageable) {
        return repository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existePorCc(String cc) {
        return repository.existsByCc(cc);
    }

    private Usuario toDomain(UsuarioEntity entity) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(entity.getIdUsuario());
        usuario.setCc(entity.getCc());
        usuario.setPrimerNombre(entity.getPrimerNombre());
        usuario.setSegundoNombre(entity.getSegundoNombre());
        usuario.setPrimerApellido(entity.getPrimerApellido());
        usuario.setSegundoApellido(entity.getSegundoApellido());
        usuario.setPasswordHash(entity.getPasswordHash());
        usuario.setRol(Usuario.Rol.valueOf(entity.getRol().name()));
        usuario.setActivo(entity.isActivo());
        usuario.setCreatedAt(entity.getCreatedAt());
        usuario.setUpdatedAt(entity.getUpdatedAt());
        return usuario;
    }

    private UsuarioEntity toEntity(Usuario usuario) {
        UsuarioEntity entity = new UsuarioEntity();
        entity.setIdUsuario(usuario.getIdUsuario());
        entity.setCc(usuario.getCc());
        entity.setPrimerNombre(usuario.getPrimerNombre());
        entity.setSegundoNombre(usuario.getSegundoNombre());
        entity.setPrimerApellido(usuario.getPrimerApellido());
        entity.setSegundoApellido(usuario.getSegundoApellido());
        entity.setPasswordHash(usuario.getPasswordHash());
        entity.setRol(UsuarioEntity.Rol.valueOf(usuario.getRol().name()));
        entity.setActivo(usuario.isActivo());
        entity.setCreatedAt(usuario.getCreatedAt());
        entity.setUpdatedAt(usuario.getUpdatedAt());
        return entity;
    }
}

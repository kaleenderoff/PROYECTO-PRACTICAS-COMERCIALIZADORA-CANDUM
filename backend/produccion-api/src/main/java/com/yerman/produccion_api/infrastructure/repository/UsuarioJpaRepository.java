package com.yerman.produccion_api.infrastructure.repository;

import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByIdUsuario(Long idUsuario);

    Optional<UsuarioEntity> findByCc(String cc);

    Optional<UsuarioEntity> findByEmail(String email);

    boolean existsByCc(String cc);

    boolean existsByEmail(String email);

    List<UsuarioEntity> findByActivoTrue();

    List<UsuarioEntity> findByRolAndActivoTrue(UsuarioEntity.Rol rol);

    Page<UsuarioEntity> findByActivoTrue(Pageable pageable);
}
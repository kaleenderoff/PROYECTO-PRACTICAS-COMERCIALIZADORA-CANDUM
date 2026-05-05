package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.mapper.EmpaqueLacteoMapper;
import com.yerman.produccion_api.domain.model.EmpaqueLacteo;
import com.yerman.produccion_api.domain.port.out.EmpaqueLacteoRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.EmpaqueLacteoEntity;
import com.yerman.produccion_api.infrastructure.repository.EmpaqueLacteoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EmpaqueLacteoJpaAdapter implements EmpaqueLacteoRepositoryPort {

    private final EmpaqueLacteoJpaRepository empaqueLacteoJpaRepository;

    public EmpaqueLacteoJpaAdapter(EmpaqueLacteoJpaRepository empaqueLacteoJpaRepository) {
        this.empaqueLacteoJpaRepository = empaqueLacteoJpaRepository;
    }

    @Override
    public EmpaqueLacteo guardar(EmpaqueLacteo empaqueLacteo) {
        EmpaqueLacteoEntity entity = EmpaqueLacteoMapper.toEntity(empaqueLacteo);
        EmpaqueLacteoEntity guardado = empaqueLacteoJpaRepository.save(entity);
        return EmpaqueLacteoMapper.toDomain(guardado);
    }

    @Override
    public Optional<EmpaqueLacteo> buscarPorId(Long id) {
        return empaqueLacteoJpaRepository.findById(id)
                .map(EmpaqueLacteoMapper::toDomain);
    }

    @Override
    public List<EmpaqueLacteo> listarTodos() {
        return empaqueLacteoJpaRepository.findAll()
                .stream()
                .map(EmpaqueLacteoMapper::toDomain)
                .toList();
    }

    @Override
    public List<EmpaqueLacteo> listarPorProductoTerminadoLacteo(Long productoTerminadoLacteoId) {
        return empaqueLacteoJpaRepository.findByProductoTerminadoLacteo_Id(productoTerminadoLacteoId)
                .stream()
                .map(EmpaqueLacteoMapper::toDomain)
                .toList();
    }

    @Override
    public List<EmpaqueLacteo> listarPorLoteEmpaque(String loteEmpaque) {
        return empaqueLacteoJpaRepository.findByLoteEmpaque(loteEmpaque)
                .stream()
                .map(EmpaqueLacteoMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorLoteEmpaque(String loteEmpaque) {
        return empaqueLacteoJpaRepository.existsByLoteEmpaque(loteEmpaque);
    }
}
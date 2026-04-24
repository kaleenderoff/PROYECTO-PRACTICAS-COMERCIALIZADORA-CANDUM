package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.mapper.ProgramacionSkuMapper;
import com.yerman.produccion_api.domain.model.ProgramacionSku;
import com.yerman.produccion_api.domain.port.out.ProgramacionSkuRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.CatalogoSkuEntity;
import com.yerman.produccion_api.infrastructure.entity.ProgramacionProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.ProgramacionSkuEntity;
import com.yerman.produccion_api.infrastructure.repository.CatalogoSkuJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ProgramacionProduccionJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ProgramacionSkuJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProgramacionSkuJpaAdapter implements ProgramacionSkuRepositoryPort {

    private final ProgramacionSkuJpaRepository repository;
    private final ProgramacionProduccionJpaRepository programacionRepository;
    private final CatalogoSkuJpaRepository skuRepository;

    public ProgramacionSkuJpaAdapter(
            ProgramacionSkuJpaRepository repository,
            ProgramacionProduccionJpaRepository programacionRepository,
            CatalogoSkuJpaRepository skuRepository) {
        this.repository = repository;
        this.programacionRepository = programacionRepository;
        this.skuRepository = skuRepository;
    }

    @Override
    public ProgramacionSku guardar(ProgramacionSku programacionSku) {
        ProgramacionProduccionEntity programacion = programacionRepository.findById(programacionSku.getIdProgramacion())
                .orElseThrow(() -> new RecursoNoEncontradoException("Programación no encontrada"));

        CatalogoSkuEntity sku = skuRepository.findById(programacionSku.getIdSku())
                .orElseThrow(() -> new RecursoNoEncontradoException("SKU no encontrado"));

        ProgramacionSkuEntity entity = ProgramacionSkuMapper.toEntity(programacionSku, programacion, sku);
        ProgramacionSkuEntity saved = repository.save(entity);

        return ProgramacionSkuMapper.toDomain(saved);
    }

    @Override
    public Optional<ProgramacionSku> obtenerPorId(Long id) {
        return repository.findById(id).map(ProgramacionSkuMapper::toDomain);
    }

    @Override
    public List<ProgramacionSku> listarPorProgramacion(Long idProgramacion) {
        return repository.findByProgramacionIdOrderByIdAsc(idProgramacion)
                .stream()
                .map(ProgramacionSkuMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorProgramacionYSku(Long idProgramacion, Long idSku) {
        return repository.existsByProgramacionIdAndSkuId(idProgramacion, idSku);
    }
}

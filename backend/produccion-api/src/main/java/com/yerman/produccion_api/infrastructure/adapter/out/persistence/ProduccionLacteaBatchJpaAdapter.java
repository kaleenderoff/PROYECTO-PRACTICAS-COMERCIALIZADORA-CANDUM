package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.domain.port.out.ProduccionLacteaBatchRepositoryPort;
import com.yerman.produccion_api.infrastructure.repository.ProduccionLacteaBatchJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class ProduccionLacteaBatchJpaAdapter implements ProduccionLacteaBatchRepositoryPort {

    private final ProduccionLacteaBatchJpaRepository produccionLacteaBatchJpaRepository;

    public ProduccionLacteaBatchJpaAdapter(ProduccionLacteaBatchJpaRepository produccionLacteaBatchJpaRepository) {
        this.produccionLacteaBatchJpaRepository = produccionLacteaBatchJpaRepository;
    }

    @Override
    public boolean existePorId(Long id) {
        if (id == null) {
            return false;
        }

        return produccionLacteaBatchJpaRepository.existsById(id);
    }

    @Override
    public boolean existePorIdYProduccion(Long id, Long idProduccionLactea) {
        if (id == null || idProduccionLactea == null) {
            return false;
        }

        return produccionLacteaBatchJpaRepository.existsByIdAndProduccionId(id, idProduccionLactea);
    }
}

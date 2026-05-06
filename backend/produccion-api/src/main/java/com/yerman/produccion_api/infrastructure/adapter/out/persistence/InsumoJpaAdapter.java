package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.domain.port.out.InsumoRepositoryPort;
import com.yerman.produccion_api.infrastructure.repository.InsumoJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class InsumoJpaAdapter implements InsumoRepositoryPort {

    private final InsumoJpaRepository repository;

    public InsumoJpaAdapter(InsumoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existePorId(Long id) {
        return id != null && repository.existsById(id);
    }
}

package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.domain.port.out.CatalogoSkuRepositoryPort;
import com.yerman.produccion_api.infrastructure.repository.CatalogoSkuJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class CatalogoSkuJpaAdapter implements CatalogoSkuRepositoryPort {

    private final CatalogoSkuJpaRepository catalogoSkuJpaRepository;

    public CatalogoSkuJpaAdapter(CatalogoSkuJpaRepository catalogoSkuJpaRepository) {
        this.catalogoSkuJpaRepository = catalogoSkuJpaRepository;
    }

    @Override
    public boolean existePorId(Long id) {
        if (id == null) {
            return false;
        }

        return catalogoSkuJpaRepository.existsById(id);
    }
}

package com.yerman.produccion_api.domain.port.out;

public interface ProduccionLacteaBatchRepositoryPort {

    boolean existePorId(Long id);

    boolean existePorIdYProduccion(Long id, Long idProduccionLactea);
}

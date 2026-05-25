package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.infrastructure.repository.ValidacionOrdenProduccionJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ValidacionOrdenProduccionGuardService {

    private final ValidacionOrdenProduccionJpaRepository validacionRepository;

    public ValidacionOrdenProduccionGuardService(
            ValidacionOrdenProduccionJpaRepository validacionRepository) {
        this.validacionRepository = validacionRepository;
    }

    public void validarOrdenNoAprobada(Long idOrden) {
        if (idOrden != null && validacionRepository.existsByOrdenIdAndAprobadoTrue(idOrden)) {
            throw new ReglaNegocioException(
                    "La orden ya fue aprobada por jefe de produccion y no permite modificaciones operativas.");
        }
    }
}

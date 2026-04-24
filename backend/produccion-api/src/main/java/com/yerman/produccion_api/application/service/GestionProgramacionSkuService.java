package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoDuplicadoException;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.ProgramacionSku;
import com.yerman.produccion_api.domain.port.in.GestionProgramacionSkuUseCase;
import com.yerman.produccion_api.domain.port.out.ProgramacionSkuRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GestionProgramacionSkuService implements GestionProgramacionSkuUseCase {

    private final ProgramacionSkuRepositoryPort repositoryPort;

    public GestionProgramacionSkuService(ProgramacionSkuRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public ProgramacionSku agregarSku(ProgramacionSku programacionSku) {
        validar(programacionSku);

        if (repositoryPort.existePorProgramacionYSku(programacionSku.getIdProgramacion(), programacionSku.getIdSku())) {
            throw new RecursoDuplicadoException("El SKU ya está agregado a esta programación");
        }

        return repositoryPort.guardar(programacionSku);
    }

    @Override
    public Optional<ProgramacionSku> obtenerPorId(Long id) {
        return repositoryPort.obtenerPorId(id);
    }

    @Override
    public List<ProgramacionSku> listarPorProgramacion(Long idProgramacion) {
        return repositoryPort.listarPorProgramacion(idProgramacion);
    }

    private void validar(ProgramacionSku programacionSku) {
        if (programacionSku.getIdProgramacion() == null) {
            throw new ReglaNegocioException("La programación es obligatoria");
        }

        if (programacionSku.getIdSku() == null) {
            throw new ReglaNegocioException("El SKU es obligatorio");
        }

        if (programacionSku.getUnidadesObjetivo() == null || programacionSku.getUnidadesObjetivo() <= 0) {
            throw new ReglaNegocioException("Las unidades objetivo deben ser mayores que cero");
        }
    }
}
package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.EstadoProgramacionProduccion;
import com.yerman.produccion_api.domain.model.ProgramacionProduccion;
import com.yerman.produccion_api.domain.port.in.GestionProgramacionProduccionUseCase;
import com.yerman.produccion_api.domain.port.out.ProgramacionProduccionRepositoryPort;
import com.yerman.produccion_api.infrastructure.adapter.out.persistence.ProgramacionProduccionJpaAdapter;
import com.yerman.produccion_api.infrastructure.entity.FormulaVersionEntity;
import com.yerman.produccion_api.infrastructure.repository.FormulaVersionJpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GestionProgramacionProduccionService implements GestionProgramacionProduccionUseCase {

    private final ProgramacionProduccionRepositoryPort repositoryPort;
    private final ProgramacionProduccionJpaAdapter repositoryAdapter;
    private final FormulaVersionJpaRepository formulaVersionRepository;

    public GestionProgramacionProduccionService(
            ProgramacionProduccionRepositoryPort repositoryPort,
            ProgramacionProduccionJpaAdapter repositoryAdapter,
            FormulaVersionJpaRepository formulaVersionRepository) {
        this.repositoryPort = repositoryPort;
        this.repositoryAdapter = repositoryAdapter;
        this.formulaVersionRepository = formulaVersionRepository;
    }

    @Override
    public ProgramacionProduccion crear(ProgramacionProduccion programacion) {
        validar(programacion);

        boolean duplicada = repositoryAdapter.existeDuplicada(
                programacion.getFechaProduccion(),
                programacion.getIdLinea(),
                programacion.getIdTurno(),
                programacion.getIdProducto());

        if (duplicada) {
            throw new ReglaNegocioException("Ya existe una programación para esa fecha, línea, turno y producto");
        }

        FormulaVersionEntity formulaVigente = formulaVersionRepository
                .findFirstByFormulaProductoIdAndEstadoOrderByFechaInicioVigenciaDesc(
                        programacion.getIdProducto(),
                        FormulaVersionEntity.EstadoFormula.VIGENTE)
                .orElseThrow(() -> new ReglaNegocioException(
                        "No existe una fórmula vigente para el producto seleccionado. Primero debe crear y marcar una fórmula como vigente."));

        programacion.setIdFormulaVersion(formulaVigente.getId());
        programacion.setCodigoProgramacion(generarCodigo());
        programacion.setEstado(EstadoProgramacionProduccion.BORRADOR);

        return repositoryPort.guardar(programacion);
    }

    @Override
    public Optional<ProgramacionProduccion> obtenerPorId(Long id) {
        return repositoryPort.obtenerPorId(id);
    }

    @Override
    public List<ProgramacionProduccion> listarPorFecha(LocalDate fecha) {
        return repositoryPort.listarPorFecha(fecha);
    }

    @Override
    public List<ProgramacionProduccion> listarTodas() {
        return repositoryPort.listarTodas();
    }

    @Override
    public ProgramacionProduccion cambiarEstado(Long id, String estado) {
        ProgramacionProduccion actual = repositoryPort.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Programación no encontrada con id: " + id));

        try {
            actual.setEstado(EstadoProgramacionProduccion.valueOf(estado.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ReglaNegocioException("Estado de programación no válido: " + estado);
        }

        return repositoryPort.guardar(actual);
    }

    private void validar(ProgramacionProduccion programacion) {
        if (programacion.getFechaProduccion() == null) {
            throw new ReglaNegocioException("La fecha de producción es obligatoria");
        }
        if (programacion.getIdLinea() == null) {
            throw new ReglaNegocioException("La línea es obligatoria");
        }
        if (programacion.getIdProducto() == null) {
            throw new ReglaNegocioException("El producto es obligatorio");
        }
        if (programacion.getIdTurno() == null) {
            throw new ReglaNegocioException("El turno es obligatorio");
        }
        if (programacion.getIdJefeProduccion() == null) {
            throw new ReglaNegocioException("El jefe de producción es obligatorio");
        }
    }

    private String generarCodigo() {
        return "PROG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
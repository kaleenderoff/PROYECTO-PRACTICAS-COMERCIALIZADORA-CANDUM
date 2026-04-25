package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.domain.model.EstadoOrdenProduccion;
import com.yerman.produccion_api.domain.model.OrdenProduccion;
import com.yerman.produccion_api.domain.model.ProgramacionProduccion;
import com.yerman.produccion_api.domain.port.in.GestionOrdenProduccionUseCase;
import com.yerman.produccion_api.domain.port.out.OrdenProduccionRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ProgramacionProduccionRepositoryPort;
import com.yerman.produccion_api.application.exception.RecursoDuplicadoException;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GestionOrdenProduccionService implements GestionOrdenProduccionUseCase {

    private final OrdenProduccionRepositoryPort ordenRepository;
    private final ProgramacionProduccionRepositoryPort programacionRepository;

    public GestionOrdenProduccionService(
            OrdenProduccionRepositoryPort ordenRepository,
            ProgramacionProduccionRepositoryPort programacionRepository) {
        this.ordenRepository = ordenRepository;
        this.programacionRepository = programacionRepository;
    }

    @Override
    public OrdenProduccion crearDesdeProgramacion(Long idProgramacion, Long idCreadaPor, String observaciones) {
        ProgramacionProduccion programacion = programacionRepository.obtenerPorId(idProgramacion)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una programación de producción con ID: " + idProgramacion));

        if (ordenRepository.existePorProgramacion(idProgramacion)) {
            throw new RecursoDuplicadoException(
                    "Ya existe una orden de producción para la programación con ID: " + idProgramacion);
        }

        OrdenProduccion orden = new OrdenProduccion();
        orden.setNumeroOrden(generarNumeroOrden(programacion));
        orden.setIdProgramacion(programacion.getId());
        orden.setIdLinea(programacion.getIdLinea());
        orden.setIdProducto(programacion.getIdProducto());
        orden.setIdTurno(programacion.getIdTurno());
        orden.setIdCreadaPor(idCreadaPor);
        orden.setFechaProduccion(programacion.getFechaProduccion());
        orden.setEstado(EstadoOrdenProduccion.PROGRAMADA);
        orden.setObservaciones(observaciones);

        return ordenRepository.guardar(orden);
    }

    @Override
    public Optional<OrdenProduccion> obtenerPorId(Long id) {
        return ordenRepository.obtenerPorId(id);
    }

    @Override
    public List<OrdenProduccion> listarTodas() {
        return ordenRepository.listarTodas();
    }

    @Override
    public List<OrdenProduccion> listarPorFecha(LocalDate fechaProduccion) {
        return ordenRepository.listarPorFecha(fechaProduccion);
    }

    @Override
    public OrdenProduccion iniciar(Long idOrden, Long idJefeLineaEjecutor) {
        OrdenProduccion orden = buscarOrden(idOrden);

        if (orden.getEstado() != EstadoOrdenProduccion.PROGRAMADA) {
            throw new ReglaNegocioException("Solo se puede iniciar una orden en estado PROGRAMADA.");
        }

        orden.setEstado(EstadoOrdenProduccion.EN_EJECUCION);
        orden.setIdJefeLineaEjecutor(idJefeLineaEjecutor);
        orden.setFechaInicioReal(LocalDateTime.now());

        return ordenRepository.guardar(orden);
    }

    @Override
    public OrdenProduccion finalizar(Long idOrden) {
        OrdenProduccion orden = buscarOrden(idOrden);

        if (orden.getEstado() != EstadoOrdenProduccion.EN_EJECUCION) {
            throw new ReglaNegocioException("Solo se puede finalizar una orden en estado EN_EJECUCION.");
        }

        orden.setEstado(EstadoOrdenProduccion.FINALIZADA);
        orden.setFechaFinReal(LocalDateTime.now());

        return ordenRepository.guardar(orden);
    }

    @Override
    public OrdenProduccion cancelar(Long idOrden, String observaciones) {
        OrdenProduccion orden = buscarOrden(idOrden);

        if (orden.getEstado() == EstadoOrdenProduccion.FINALIZADA
                || orden.getEstado() == EstadoOrdenProduccion.CERRADA) {
            throw new ReglaNegocioException("No se puede cancelar una orden FINALIZADA o CERRADA.");
        }

        orden.setEstado(EstadoOrdenProduccion.CANCELADA);
        orden.setObservaciones(observaciones);

        return ordenRepository.guardar(orden);
    }

    private OrdenProduccion buscarOrden(Long idOrden) {
        return ordenRepository.obtenerPorId(idOrden)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una orden de producción con ID: " + idOrden));
    }

    private String generarNumeroOrden(ProgramacionProduccion programacion) {
        return "OP-" + programacion.getFechaProduccion().toString().replace("-", "")
                + "-" + programacion.getId();
    }
}
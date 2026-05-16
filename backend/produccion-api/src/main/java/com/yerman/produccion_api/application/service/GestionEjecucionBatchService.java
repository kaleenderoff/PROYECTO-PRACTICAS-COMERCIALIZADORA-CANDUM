package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.EjecucionBatch;
import com.yerman.produccion_api.domain.model.EjecucionBatch.EstadoBatch;
import com.yerman.produccion_api.domain.model.OrdenProduccion;
import com.yerman.produccion_api.domain.port.in.GestionEjecucionBatchUseCase;
import com.yerman.produccion_api.domain.port.out.EjecucionBatchRepositoryPort;
import com.yerman.produccion_api.domain.port.out.OrdenProduccionRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GestionEjecucionBatchService implements GestionEjecucionBatchUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(GestionEjecucionBatchService.class);

    private final EjecucionBatchRepositoryPort repository;
    private final OrdenProduccionRepositoryPort ordenRepository;

    public GestionEjecucionBatchService(EjecucionBatchRepositoryPort repository, OrdenProduccionRepositoryPort ordenRepository) {
        this.repository = repository;
        this.ordenRepository = ordenRepository;
    }

    @Override
    @Transactional
    public EjecucionBatch iniciarBatch(Long idOrden, Long idMarmita, BigDecimal kgEntrada) {
        if (kgEntrada == null || kgEntrada.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("La cantidad de entrada debe ser mayor a 0 kg.");
        }

        if (repository.existeMarmitaOcupadaEnOrden(idMarmita, idOrden)) {
            throw new ReglaNegocioException("La marmita ya tiene un batch EN_PROCESO para esta orden.");
        }

        // Validar límite de batches planificados
        OrdenProduccion orden = ordenRepository.obtenerPorId(idOrden)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la orden con ID: " + idOrden));

        if (orden.getEstado() == com.yerman.produccion_api.domain.model.EstadoOrdenProduccion.FINALIZADA) {
            throw new ReglaNegocioException("No se pueden iniciar más baches porque la orden ya está FINALIZADA.");
        }

        List<EjecucionBatch> batchesExistentes = repository.listarPorOrden(idOrden);
        
        if (batchesExistentes.size() >= orden.getNumBachesPlan()) {
            throw new ReglaNegocioException("Se ha alcanzado el número máximo de batches planificados (" + 
                    orden.getNumBachesPlan() + "). No es posible iniciar más baches.");
        }

        // Auto-numeración del batch
        int proximoNumero = batchesExistentes.stream()
                .mapToInt(EjecucionBatch::getNumeroBatch)
                .max()
                .orElse(0) + 1;

        EjecucionBatch batch = new EjecucionBatch();
        batch.setIdOrdenProduccion(idOrden);
        batch.setNumeroBatch(proximoNumero);
        batch.setIdMarmita(idMarmita);
        batch.setKgEntrada(kgEntrada);
        batch.setEstado(EstadoBatch.EN_PROCESO);
        batch.setFechaInicio(LocalDateTime.now());

        // Si la orden está PROGRAMADA, pasarla automáticamente a EN_EJECUCION al iniciar el primer bache
        if (orden.getEstado() == com.yerman.produccion_api.domain.model.EstadoOrdenProduccion.PROGRAMADA) {
            orden.setEstado(com.yerman.produccion_api.domain.model.EstadoOrdenProduccion.EN_EJECUCION);
            if (orden.getFechaInicioReal() == null) {
                orden.setFechaInicioReal(LocalDateTime.now());
            }
            ordenRepository.guardar(orden);
            LOGGER.info("Orden {} pasada a EN_EJECUCION al iniciar batch.", orden.getNumeroOrden());
        }

        return repository.guardar(batch);
    }

    @Override
    @Transactional
    public EjecucionBatch obtenerBatch(Long idBatch) {
        return repository.obtenerPorId(idBatch)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el batch con ID: " + idBatch));
    }

    @Override
    @Transactional
    public EjecucionBatch finalizarBatch(Long idBatch, BigDecimal kgProducidos, String observaciones, Boolean conNovedad, Boolean huboReproceso, Boolean batchConforme) {
        EjecucionBatch batch = repository.obtenerPorId(idBatch)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el batch con ID: " + idBatch));

        OrdenProduccion orden = ordenRepository.obtenerPorId(batch.getIdOrdenProduccion())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la orden vinculada al batch."));

        if (orden.getEstado() == com.yerman.produccion_api.domain.model.EstadoOrdenProduccion.FINALIZADA) {
            throw new ReglaNegocioException("No se puede modificar el bache porque la orden ya está FINALIZADA.");
        }

        if (batch.getEstado() != EstadoBatch.EN_PROCESO) {
            throw new ReglaNegocioException("Solo se puede finalizar un batch que esté EN_PROCESO.");
        }

        batch.setKgProducidos(kgProducidos);
        batch.setEstado(Boolean.TRUE.equals(conNovedad) ? EstadoBatch.CON_NOVEDAD : EstadoBatch.FINALIZADO);
        batch.setFechaFin(LocalDateTime.now());
        batch.setObservaciones(observaciones);
        batch.setHuboReproceso(huboReproceso);
        batch.setBatchConforme(batchConforme);

        if (batch.getKgEntrada() != null && batch.getKgEntrada().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal rendimiento = kgProducidos
                    .multiply(BigDecimal.valueOf(100))
                    .divide(batch.getKgEntrada(), 3, RoundingMode.HALF_UP);
            batch.setRendimientoPct(rendimiento);
        }

        return repository.guardar(batch);
    }

    @Override
    @Transactional
    public EjecucionBatch registrarNovedad(Long idBatch, String observaciones) {
        EjecucionBatch batch = repository.obtenerPorId(idBatch)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el batch con ID: " + idBatch));

        batch.setEstado(EstadoBatch.CON_NOVEDAD);
        batch.setObservaciones(observaciones);

        return repository.guardar(batch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EjecucionBatch> listarBatchesPorOrden(Long idOrden) {
        return repository.listarPorOrden(idOrden);
    }

    @Override
    @Transactional
    public void eliminarBatch(Long idBatch) {
        EjecucionBatch batch = repository.obtenerPorId(idBatch)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el batch con ID: " + idBatch));

        OrdenProduccion orden = ordenRepository.obtenerPorId(batch.getIdOrdenProduccion())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la orden vinculada al batch."));

        if (orden.getEstado() == com.yerman.produccion_api.domain.model.EstadoOrdenProduccion.FINALIZADA) {
            throw new ReglaNegocioException("No se puede eliminar el bache porque la orden ya está FINALIZADA.");
        }

        if (batch.getEstado() == EstadoBatch.FINALIZADO) {
            throw new ReglaNegocioException("No se puede eliminar un batch que ya ha sido FINALIZADO.");
        }

        repository.eliminar(idBatch);
    }
}

package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.EjecucionBatch;
import com.yerman.produccion_api.domain.model.EjecucionBatch.EstadoBatch;
import com.yerman.produccion_api.domain.model.MovimientoLeche;
import com.yerman.produccion_api.domain.model.OrdenProduccion;
import com.yerman.produccion_api.domain.model.TipoMovimientoLeche;
import com.yerman.produccion_api.domain.port.in.GestionEjecucionBatchUseCase;
import com.yerman.produccion_api.domain.port.in.GestionMovimientoLecheUseCase;
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
    private final ValidacionOrdenProduccionGuardService validacionGuardService;
    private final GestionMovimientoLecheUseCase movimientoLecheUseCase;

    public GestionEjecucionBatchService(
            EjecucionBatchRepositoryPort repository,
            OrdenProduccionRepositoryPort ordenRepository,
            ValidacionOrdenProduccionGuardService validacionGuardService,
            GestionMovimientoLecheUseCase movimientoLecheUseCase) {
        this.repository = repository;
        this.ordenRepository = ordenRepository;
        this.validacionGuardService = validacionGuardService;
        this.movimientoLecheUseCase = movimientoLecheUseCase;
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
        validacionGuardService.validarOrdenNoAprobada(idOrden);

        if (orden.getEstado() == com.yerman.produccion_api.domain.model.EstadoOrdenProduccion.FINALIZADA) {
            throw new ReglaNegocioException("No se pueden iniciar más baches porque la orden ya está FINALIZADA.");
        }

        List<EjecucionBatch> batchesExistentes = repository.listarPorOrden(idOrden);
        
        // Solo validar límite si numBachesPlan está definido
        Integer numBachesPlan = orden.getNumBachesPlan();
        if (numBachesPlan != null && numBachesPlan > 0 && batchesExistentes.size() >= numBachesPlan) {
            throw new ReglaNegocioException("Se ha alcanzado el número máximo de batches planificados (" + 
                    numBachesPlan + "). No es posible iniciar más baches.");
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
    public EjecucionBatch finalizarBatch(Long idBatch, BigDecimal kgProducidos, String observaciones, Boolean conNovedad, Boolean huboReproceso, Boolean batchConforme, BigDecimal brixFinal, String tipoNovedad) {
        EjecucionBatch batch = repository.obtenerPorId(idBatch)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el batch con ID: " + idBatch));

        OrdenProduccion orden = ordenRepository.obtenerPorId(batch.getIdOrdenProduccion())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la orden vinculada al batch."));
        validacionGuardService.validarOrdenNoAprobada(batch.getIdOrdenProduccion());

        if (orden.getEstado() == com.yerman.produccion_api.domain.model.EstadoOrdenProduccion.FINALIZADA) {
            throw new ReglaNegocioException("No se puede modificar el bache porque la orden ya está FINALIZADA.");
        }

        if (batch.getEstado() != EstadoBatch.EN_PROCESO) {
            throw new ReglaNegocioException("Solo se puede finalizar un batch que esté EN_PROCESO.");
        }

        MovimientoLeche movimientoLeche = registrarSalidaLecheProduccion(batch, orden);

        batch.setKgProducidos(kgProducidos);
        batch.setIdMovimientoLeche(movimientoLeche.getId());
        batch.setEstado(Boolean.TRUE.equals(conNovedad) ? EstadoBatch.CON_NOVEDAD : EstadoBatch.FINALIZADO);
        batch.setFechaFin(LocalDateTime.now());
        batch.setObservaciones(observaciones);
        batch.setHuboReproceso(huboReproceso);
        batch.setBatchConforme(batchConforme);
        batch.setBrixFinal(brixFinal);
        if (Boolean.TRUE.equals(conNovedad) && tipoNovedad != null && !tipoNovedad.isBlank()) {
            batch.setTipoNovedad(tipoNovedad.trim());
        }

        if (batch.getKgEntrada() != null && batch.getKgEntrada().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal rendimiento = kgProducidos
                    .multiply(BigDecimal.valueOf(100))
                    .divide(batch.getKgEntrada(), 3, RoundingMode.HALF_UP);
            batch.setRendimientoPct(rendimiento);
        }

        return repository.guardar(batch);
    }

    private MovimientoLeche registrarSalidaLecheProduccion(EjecucionBatch batch, OrdenProduccion orden) {
        if (orden.getIdTanqueLeche() == null) {
            throw new ReglaNegocioException(
                    "Debe seleccionar el tanque de leche descremada antes de finalizar el batch.");
        }

        if (batch.getKgEntrada() == null || batch.getKgEntrada().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("El batch no tiene kg de entrada validos para descontar leche.");
        }

        BigDecimal litrosConsumidos = batch.getKgEntrada()
                .divide(new BigDecimal("1.03"), 3, RoundingMode.HALF_UP);

        String referencia = "ORDEN-" + orden.getNumeroOrden() + "-BATCH-" + batch.getNumeroBatch();

        return movimientoLecheUseCase.registrarMovimiento(
                orden.getIdTanqueLeche(),
                TipoMovimientoLeche.SALIDA_PRODUCCION,
                litrosConsumidos,
                orden.getIdJefeLineaEjecutor() != null ? orden.getIdJefeLineaEjecutor() : 1L,
                referencia,
                "Consumo automatico de leche al finalizar batch de produccion.");
    }

    @Override
    @Transactional
    public EjecucionBatch registrarNovedad(Long idBatch, String observaciones) {
        EjecucionBatch batch = repository.obtenerPorId(idBatch)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el batch con ID: " + idBatch));
        validacionGuardService.validarOrdenNoAprobada(batch.getIdOrdenProduccion());

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
        validacionGuardService.validarOrdenNoAprobada(batch.getIdOrdenProduccion());

        if (orden.getEstado() == com.yerman.produccion_api.domain.model.EstadoOrdenProduccion.FINALIZADA) {
            throw new ReglaNegocioException("No se puede eliminar el bache porque la orden ya está FINALIZADA.");
        }

        if (batch.getIdMovimientoLeche() != null) {
            throw new ReglaNegocioException("No se puede eliminar un batch que ya descontó leche del tanque.");
        }

        if (batch.getEstado() != EstadoBatch.EN_PROCESO) {
            throw new ReglaNegocioException("Solo se pueden eliminar batches abiertos en estado EN_PROCESO.");
        }

        repository.eliminar(idBatch);
    }
}

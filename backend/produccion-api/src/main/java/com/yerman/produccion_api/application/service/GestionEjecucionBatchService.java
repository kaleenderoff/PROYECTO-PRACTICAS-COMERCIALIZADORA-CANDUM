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
import com.yerman.produccion_api.infrastructure.entity.FormulaDetalleEntity;
import com.yerman.produccion_api.infrastructure.entity.FormulaVersionEntity;
import com.yerman.produccion_api.infrastructure.repository.FormulaDetalleJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.FormulaVersionJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GestionEjecucionBatchService implements GestionEjecucionBatchUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(GestionEjecucionBatchService.class);
    private static final BigDecimal CIEN = new BigDecimal("100");

    private final EjecucionBatchRepositoryPort repository;
    private final OrdenProduccionRepositoryPort ordenRepository;
    private final ValidacionOrdenProduccionGuardService validacionGuardService;
    private final GestionMovimientoLecheUseCase movimientoLecheUseCase;
    private final FormulaVersionJpaRepository formulaVersionRepository;
    private final FormulaDetalleJpaRepository formulaDetalleRepository;

    public GestionEjecucionBatchService(
            EjecucionBatchRepositoryPort repository,
            OrdenProduccionRepositoryPort ordenRepository,
            ValidacionOrdenProduccionGuardService validacionGuardService,
            GestionMovimientoLecheUseCase movimientoLecheUseCase,
            FormulaVersionJpaRepository formulaVersionRepository,
            FormulaDetalleJpaRepository formulaDetalleRepository) {
        this.repository = repository;
        this.ordenRepository = ordenRepository;
        this.validacionGuardService = validacionGuardService;
        this.movimientoLecheUseCase = movimientoLecheUseCase;
        this.formulaVersionRepository = formulaVersionRepository;
        this.formulaDetalleRepository = formulaDetalleRepository;
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

        OrdenProduccion orden = ordenRepository.obtenerPorId(idOrden)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la orden con ID: " + idOrden));

        validacionGuardService.validarOrdenNoAprobada(idOrden);

        if (orden.getEstado() == com.yerman.produccion_api.domain.model.EstadoOrdenProduccion.FINALIZADA) {
            throw new ReglaNegocioException("No se pueden iniciar más baches porque la orden ya está FINALIZADA.");
        }

        List<EjecucionBatch> batchesExistentes = repository.listarPorOrden(idOrden);

        Integer numBachesPlan = orden.getNumBachesPlan();
        if (numBachesPlan != null && numBachesPlan > 0 && batchesExistentes.size() >= numBachesPlan) {
            throw new ReglaNegocioException("Se ha alcanzado el número máximo de batches planificados ("
                    + numBachesPlan + "). No es posible iniciar más baches.");
        }

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
    public EjecucionBatch finalizarBatch(
            Long idBatch,
            BigDecimal kgProducidos,
            String observaciones,
            Boolean conNovedad,
            Boolean huboReproceso,
            Boolean batchConforme,
            BigDecimal brixFinal,
            String tipoNovedad) {

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

        if (kgProducidos == null || kgProducidos.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("Los kg producidos deben ser mayores a cero.");
        }

        validarBrixFinal(brixFinal);

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

        recalcularRendimiento(batch);

        return repository.guardar(batch);
    }

    @Override
    @Transactional
    public EjecucionBatch actualizarBatchFinalizado(
            Long idBatch,
            BigDecimal kgProducidos,
            String observaciones,
            Boolean conNovedad,
            Boolean huboReproceso,
            Boolean batchConforme,
            BigDecimal brixFinal,
            String tipoNovedad) {

        EjecucionBatch batch = repository.obtenerPorId(idBatch)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el batch con ID: " + idBatch));

        OrdenProduccion orden = ordenRepository.obtenerPorId(batch.getIdOrdenProduccion())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la orden vinculada al batch."));

        validacionGuardService.validarOrdenNoAprobada(batch.getIdOrdenProduccion());

        if (orden.getEstado() == com.yerman.produccion_api.domain.model.EstadoOrdenProduccion.FINALIZADA) {
            throw new ReglaNegocioException("No se puede editar el batch porque la orden ya está FINALIZADA.");
        }

        if (batch.getEstado() != EstadoBatch.FINALIZADO && batch.getEstado() != EstadoBatch.CON_NOVEDAD) {
            throw new ReglaNegocioException("Solo se pueden editar batches FINALIZADOS o CON_NOVEDAD.");
        }

        if (kgProducidos == null || kgProducidos.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("Los kg producidos deben ser mayores a cero.");
        }

        validarBrixFinal(brixFinal);

        batch.setKgProducidos(kgProducidos);
        batch.setObservaciones(observaciones);
        batch.setHuboReproceso(huboReproceso);
        batch.setBatchConforme(batchConforme);
        batch.setBrixFinal(brixFinal);

        if (Boolean.TRUE.equals(conNovedad)) {
            batch.setEstado(EstadoBatch.CON_NOVEDAD);

            if (tipoNovedad != null && !tipoNovedad.isBlank()) {
                batch.setTipoNovedad(tipoNovedad.trim());
            }
        } else {
            batch.setEstado(EstadoBatch.FINALIZADO);
            batch.setTipoNovedad(null);
        }

        recalcularRendimiento(batch);

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

        BigDecimal porcentajeLecheLiquida = obtenerPorcentajeLecheLiquidaFormula(orden);
        BigDecimal litrosConsumidos = calcularLitrosLecheConsumida(batch.getKgEntrada(), porcentajeLecheLiquida);

        String referencia = "ORDEN-" + orden.getNumeroOrden() + "-BATCH-" + batch.getNumeroBatch();

        String observaciones = "Consumo automatico de leche liquida al finalizar batch de produccion. "
                + "Kg entrada batch: " + batch.getKgEntrada().setScale(3, RoundingMode.HALF_UP)
                + " kg. Porcentaje leche formula: " + porcentajeLecheLiquida.setScale(3, RoundingMode.HALF_UP)
                + "%. Litros descontados: " + litrosConsumidos.setScale(3, RoundingMode.HALF_UP)
                + " L.";

        LOGGER.info(
                "Descuento leche por formula. Orden: {}, Batch: {}, Kg entrada: {}, % leche: {}, Litros descontados: {}",
                orden.getNumeroOrden(),
                batch.getNumeroBatch(),
                batch.getKgEntrada(),
                porcentajeLecheLiquida,
                litrosConsumidos);

        return movimientoLecheUseCase.registrarMovimiento(
                orden.getIdTanqueLeche(),
                TipoMovimientoLeche.SALIDA_PRODUCCION,
                litrosConsumidos,
                orden.getIdJefeLineaEjecutor() != null ? orden.getIdJefeLineaEjecutor() : 1L,
                referencia,
                observaciones);
    }

    private BigDecimal obtenerPorcentajeLecheLiquidaFormula(OrdenProduccion orden) {
        if (orden.getIdProducto() == null) {
            throw new ReglaNegocioException(
                    "La orden no tiene producto asociado para buscar la formula vigente.");
        }

        FormulaVersionEntity formulaVigente = formulaVersionRepository
                .findFirstByFormulaProductoIdAndEstadoOrderByFechaInicioVigenciaDesc(
                        orden.getIdProducto(),
                        FormulaVersionEntity.EstadoFormula.VIGENTE)
                .orElseThrow(() -> new ReglaNegocioException(
                        "No existe una formula vigente para el producto de la orden. "
                                + "No es posible calcular el consumo real de leche liquida."));

        List<FormulaDetalleEntity> detalles = formulaDetalleRepository
                .findByFormulaVersionIdOrderByOrdenAdicionAsc(formulaVigente.getId());

        FormulaDetalleEntity detalleLecheLiquida = detalles.stream()
                .filter(this::esDetalleLecheLiquida)
                .findFirst()
                .orElseThrow(() -> new ReglaNegocioException(
                        "La formula vigente no tiene un insumo identificado como Leche liquida. "
                                + "No es posible descontar leche del tanque de forma correcta."));

        BigDecimal porcentaje = detalleLecheLiquida.getPorcentaje();

        if (porcentaje != null && porcentaje.compareTo(BigDecimal.ZERO) > 0) {
            return porcentaje;
        }

        if (detalleLecheLiquida.getCantidadKg() != null
                && formulaVigente.getKgBatchTotal() != null
                && formulaVigente.getKgBatchTotal().compareTo(BigDecimal.ZERO) > 0) {

            return detalleLecheLiquida.getCantidadKg()
                    .multiply(CIEN)
                    .divide(formulaVigente.getKgBatchTotal(), 6, RoundingMode.HALF_UP);
        }

        throw new ReglaNegocioException(
                "El insumo Leche liquida de la formula no tiene porcentaje ni cantidad valida.");
    }

    private BigDecimal calcularLitrosLecheConsumida(BigDecimal kgEntradaBatch, BigDecimal porcentajeLecheLiquida) {
        return kgEntradaBatch
                .multiply(porcentajeLecheLiquida)
                .divide(CIEN, 3, RoundingMode.HALF_UP);
    }

    private boolean esDetalleLecheLiquida(FormulaDetalleEntity detalle) {
        if (detalle == null || detalle.getInsumo() == null || detalle.getInsumo().getNombre() == null) {
            return false;
        }

        String nombre = normalizar(detalle.getInsumo().getNombre());

        return nombre.contains("LECHE")
                && (nombre.contains("LIQUIDA")
                        || nombre.contains("LIQUIDO")
                        || nombre.contains("LIQ"));
    }

    private String normalizar(String texto) {
        String sinAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return sinAcentos
                .toUpperCase()
                .trim();
    }

    private void validarBrixFinal(BigDecimal brixFinal) {
        if (brixFinal == null || brixFinal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("El Brix final es obligatorio para finalizar el batch.");
        }
    }

    private void recalcularRendimiento(EjecucionBatch batch) {
        if (batch.getKgEntrada() != null
                && batch.getKgEntrada().compareTo(BigDecimal.ZERO) > 0
                && batch.getKgProducidos() != null
                && batch.getKgProducidos().compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal rendimiento = batch.getKgProducidos()
                    .multiply(CIEN)
                    .divide(batch.getKgEntrada(), 3, RoundingMode.HALF_UP);

            batch.setRendimientoPct(rendimiento);
        }
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
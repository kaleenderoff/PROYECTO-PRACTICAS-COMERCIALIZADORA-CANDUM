package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.domain.model.EstadoOrdenProduccion;
import com.yerman.produccion_api.domain.model.OrdenProduccion;
import com.yerman.produccion_api.domain.model.ProgramacionProduccion;
import com.yerman.produccion_api.domain.port.in.GestionOrdenProduccionUseCase;
import com.yerman.produccion_api.domain.port.out.OrdenProduccionRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ProgramacionProduccionRepositoryPort;
import com.yerman.produccion_api.application.dto.request.RegistrarProduccionSkuRequest;
import com.yerman.produccion_api.application.exception.RecursoDuplicadoException;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.infrastructure.repository.OrdenProduccionDetalleJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GestionOrdenProduccionService implements GestionOrdenProduccionUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(GestionOrdenProduccionService.class);

    private final OrdenProduccionRepositoryPort ordenRepository;
    private final ProgramacionProduccionRepositoryPort programacionRepository;
    private final OrdenProduccionDetalleJpaRepository detalleRepository;
    private final com.yerman.produccion_api.domain.port.out.EjecucionBatchRepositoryPort batchRepository;
    private final com.yerman.produccion_api.domain.port.in.GestionMovimientoLecheUseCase movimientoLecheUseCase;

    public GestionOrdenProduccionService(
            OrdenProduccionRepositoryPort ordenRepository,
            ProgramacionProduccionRepositoryPort programacionRepository,
            OrdenProduccionDetalleJpaRepository detalleRepository,
            com.yerman.produccion_api.domain.port.out.EjecucionBatchRepositoryPort batchRepository,
            com.yerman.produccion_api.domain.port.in.GestionMovimientoLecheUseCase movimientoLecheUseCase) {
        this.ordenRepository = ordenRepository;
        this.programacionRepository = programacionRepository;
        this.detalleRepository = detalleRepository;
        this.batchRepository = batchRepository;
        this.movimientoLecheUseCase = movimientoLecheUseCase;
    }

    @Override
    public OrdenProduccion crearDesdeProgramacion(Long idProgramacion, Long idCreadaPor, String observaciones) {
        ProgramacionProduccion programacion = programacionRepository.obtenerPorId(idProgramacion)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una programaciÃ³n de producciÃ³n con ID: " + idProgramacion));

        if (ordenRepository.existePorProgramacion(idProgramacion)) {
            throw new RecursoDuplicadoException(
                    "Ya existe una orden de producciÃ³n para la programaciÃ³n con ID: " + idProgramacion);
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
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Optional<OrdenProduccion> obtenerPorId(Long id) {
        return ordenRepository.obtenerPorId(id);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<OrdenProduccion> listarTodas() {
        return ordenRepository.listarTodas();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
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
    @Transactional
    public OrdenProduccion finalizar(Long idOrden) {
        LOGGER.info("Iniciando proceso de finalizacion para orden ID: {}", idOrden);
        OrdenProduccion orden = buscarOrden(idOrden);

        if (orden.getEstado() != EstadoOrdenProduccion.EN_EJECUCION
                && orden.getEstado() != EstadoOrdenProduccion.PROGRAMADA) {
            throw new ReglaNegocioException("Solo se puede finalizar una orden en estado PROGRAMADA o EN_EJECUCION.");
        }

        // 1. Validar que todos los batches estÃ©n FINALIZADOS
        var batches = batchRepository.listarPorOrden(idOrden);

        if (batches.isEmpty()) {
            throw new ReglaNegocioException("No se puede finalizar una orden sin batches registrados.");
        }

        boolean hayBatchesAbiertos = batches.stream()
                .anyMatch(b -> b
                        .getEstado() == com.yerman.produccion_api.domain.model.EjecucionBatch.EstadoBatch.EN_PROCESO);

        if (hayBatchesAbiertos) {
            throw new ReglaNegocioException(
                    "No se puede finalizar la orden porque existen batches en proceso. Por favor, finalice todos los batches primero.");
        }

        // 2. Validar que se hayan registrado resultados para los SKUs
        var detalles = detalleRepository.findByOrdenId(idOrden);
        BigDecimal totalUnidadesReales = detalles.stream()
                .map(d -> d.getUnidadesReales() != null ? BigDecimal.valueOf(d.getUnidadesReales()) : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalUnidadesReales.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException(
                    "No se puede finalizar la orden porque no se han registrado unidades reales producidas para ningÃºn SKU. Por favor, guarde los resultados de producciÃ³n al final de la pÃ¡gina.");
        }

        // 3. Consolidar batches (Asegurar que todos queden en estado FINALIZADO o
        // CON_NOVEDAD)
        // Esto ya se valida arriba, pero podemos asegurar que no haya nada PENDIENTE.

        // 4. Calcular MÃ©tricas Reales Finales
        BigDecimal kgEntradaReal = batches.stream()
                .map(b -> b.getKgEntrada() != null ? b.getKgEntrada() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // A. ProducciÃ³n de proceso (Marmitas)
        BigDecimal kgProducidoBatches = batches.stream()
                .map(b -> b.getKgProducidos() != null ? b.getKgProducidos() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // B. ProducciÃ³n empacada (SKUs)
        BigDecimal kgPtReal = detalles.stream()
                .map(d -> d.getCantidadReal() != null ? d.getCantidadReal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        orden.setKgEntradaReal(kgEntradaReal);
        orden.setKgProducidoBatches(kgProducidoBatches);
        orden.setKgPtReal(kgPtReal);

        if (kgEntradaReal.compareTo(BigDecimal.ZERO) > 0) {
            // Rendimiento = (Kg Producido Batches / Kg Entrada) * 100
            // Usamos la producciÃ³n de marmitas para medir eficiencia de cocciÃ³n
            BigDecimal rendimiento = kgProducidoBatches.multiply(new BigDecimal("100"))
                    .divide(kgEntradaReal, 2, RoundingMode.HALF_UP);
            orden.setRendimientoReal(rendimiento);

            // Merma de Proceso = Kg Entrada - Kg Producido Batches
            orden.setMermaReal(kgEntradaReal.subtract(kgProducidoBatches));

            // Balance de empaque = Kg Producido Batches - Kg PT (SKUs)
            orden.setMermaEmpaque(kgProducidoBatches.subtract(kgPtReal));
        } else {
            orden.setRendimientoReal(BigDecimal.ZERO);
            orden.setMermaReal(BigDecimal.ZERO);
            orden.setMermaEmpaque(BigDecimal.ZERO);
        }

        // 4. Cambiar estado y registrar fecha de fin real
        orden.setEstado(EstadoOrdenProduccion.FINALIZADA);
        orden.setFechaFinReal(LocalDateTime.now());

        // Asegurar fecha de inicio si por alguna razÃ³n no se registrÃ³ automÃ¡ticamente
        if (orden.getFechaInicioReal() == null) {
            LocalDateTime fechaPrimerBatch = batches.stream()
                    .map(com.yerman.produccion_api.domain.model.EjecucionBatch::getFechaInicio)
                    .filter(java.util.Objects::nonNull)
                    .min(LocalDateTime::compareTo)
                    .orElse(LocalDateTime.now());
            orden.setFechaInicioReal(fechaPrimerBatch);
        }

        // 5. Impactar Inventario (Consumo de insumos e incremento de PT)
        impactarInventario(orden);

        LOGGER.info("Orden {} finalizada exitosamente.", orden.getNumeroOrden());
        return ordenRepository.guardar(orden);
    }

    private void impactarInventario(OrdenProduccion orden) {
        if (orden.getIdTanqueLeche() == null) {
            throw new ReglaNegocioException(
                    "Debe seleccionar el tanque de leche descremada antes de finalizar la orden.");
        }

        if (orden.getKgEntradaReal() == null || orden.getKgEntradaReal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException(
                    "No hay entrada real registrada para descontar leche del tanque.");
        }

        // ConversiÃ³n Kg -> Litros (Densidad Leche ~1.03 kg/L)
        // Litros = Kg / 1.03
        BigDecimal litrosConsumidos = orden.getKgEntradaReal()
                .divide(new BigDecimal("1.03"), 2, RoundingMode.HALF_UP);

        String referencia = "ORDEN-" + orden.getNumeroOrden();
        String obs = "Consumo automÃ¡tico por finalizaciÃ³n de orden de producciÃ³n.";

        movimientoLecheUseCase.registrarMovimiento(
                orden.getIdTanqueLeche(),
                com.yerman.produccion_api.domain.model.TipoMovimientoLeche.SALIDA_PRODUCCION,
                litrosConsumidos,
                orden.getIdJefeLineaEjecutor() != null ? orden.getIdJefeLineaEjecutor() : 1L,
                referencia,
                obs);
        LOGGER.info("Descuento de {} L realizado del tanque ID: {}", litrosConsumidos, orden.getIdTanqueLeche());
    }

    @Override
    public OrdenProduccion cancelar(Long idOrden, String observaciones) {
        OrdenProduccion orden = buscarOrden(idOrden);

        if (orden.getEstado() == EstadoOrdenProduccion.FINALIZADA) {
            throw new ReglaNegocioException("No se puede cancelar una orden FINALIZADA.");
        }

        orden.setEstado(EstadoOrdenProduccion.CANCELADA);
        orden.setObservaciones(observaciones);

        return ordenRepository.guardar(orden);
    }

    @Override
    @Transactional
    public void registrarProduccionSku(Long idOrden, List<RegistrarProduccionSkuRequest> producciones) {
        OrdenProduccion orden = buscarOrden(idOrden);

        if (orden.getEstado() == EstadoOrdenProduccion.FINALIZADA) {
            throw new ReglaNegocioException("No se pueden registrar SKUs porque la orden ya estÃ¡ FINALIZADA.");
        }

        for (RegistrarProduccionSkuRequest prod : producciones) {
            var detalle = detalleRepository.findById(prod.idOrdenDetalle())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe el detalle de orden con ID: " + prod.idOrdenDetalle()));

            if (!detalle.getOrden().getId().equals(idOrden)) {
                throw new ReglaNegocioException("El detalle no pertenece a la orden especificada.");
            }

            detalle.setCantidadReal(prod.cantidadReal());
            detalle.setUnidadesReales(prod.unidadesReales());
            detalle.setObservaciones(prod.observaciones());

            detalleRepository.save(detalle);
        }
    }

    @Override
    public OrdenProduccion actualizarTanqueLeche(Long idOrden, Long idTanque) {
        OrdenProduccion orden = buscarOrden(idOrden);
        if (orden.getEstado() == EstadoOrdenProduccion.FINALIZADA) {
            throw new ReglaNegocioException("No se puede cambiar el tanque de una orden ya finalizada.");
        }
        orden.setIdTanqueLeche(idTanque);
        return ordenRepository.guardar(orden);
    }

    private OrdenProduccion buscarOrden(Long idOrden) {
        return ordenRepository.obtenerPorId(idOrden)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una orden de producciÃ³n con ID: " + idOrden));
    }

    private String generarNumeroOrden(ProgramacionProduccion programacion) {
        return "OP-" + programacion.getFechaProduccion().toString().replace("-", "")
                + "-" + programacion.getId();
    }
}

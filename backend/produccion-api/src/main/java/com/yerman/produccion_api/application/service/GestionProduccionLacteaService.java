package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.MovimientoLeche;
import com.yerman.produccion_api.domain.model.Produccion;
import com.yerman.produccion_api.domain.model.ProduccionBatch;
import com.yerman.produccion_api.domain.model.TipoMovimientoLeche;
import com.yerman.produccion_api.domain.port.in.GestionMovimientoLecheUseCase;
import com.yerman.produccion_api.domain.port.in.GestionOrdenProduccionUseCase;
import com.yerman.produccion_api.domain.port.in.GestionProduccionLacteaUseCase;
import com.yerman.produccion_api.domain.port.out.ProduccionLacteaRepositoryPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GestionProduccionLacteaService implements GestionProduccionLacteaUseCase {

    private final ProduccionLacteaRepositoryPort repository;
    private final GestionMovimientoLecheUseCase movimientoLecheUseCase;
    private final GestionOrdenProduccionUseCase ordenProduccionUseCase;

    public GestionProduccionLacteaService(
            ProduccionLacteaRepositoryPort repository,
            GestionMovimientoLecheUseCase movimientoLecheUseCase,
            GestionOrdenProduccionUseCase ordenProduccionUseCase) {
        this.repository = repository;
        this.movimientoLecheUseCase = movimientoLecheUseCase;
        this.ordenProduccionUseCase = ordenProduccionUseCase;
    }

    @Override
    @Transactional
    public Produccion registrarProduccion(Produccion produccion) {

        validarProduccion(produccion);

        if (produccion.getIdOrdenProduccion() != null) {

            ordenProduccionUseCase.obtenerPorId(
                    produccion.getIdOrdenProduccion()).ifPresent(orden -> {

                        if (orden
                                .getEstado() == com.yerman.produccion_api.domain.model.EstadoOrdenProduccion.PROGRAMADA) {

                            ordenProduccionUseCase.iniciar(
                                    orden.getId(),
                                    produccion.getIdUsuario());
                        }
                    });
        }

        BigDecimal totalLitrosProduccion = produccion.getBatches()
                .stream()
                .map(ProduccionBatch::getLitrosConsumidos)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldoActual = movimientoLecheUseCase.obtenerSaldoActualPorTanque(
                produccion.getIdTanque());

        if (saldoActual.compareTo(totalLitrosProduccion) < 0) {

            throw new ReglaNegocioException(
                    "No hay suficiente leche disponible en el tanque. Saldo actual: "
                            + saldoActual
                            + " L, requerido: "
                            + totalLitrosProduccion
                            + " L.");
        }

        for (ProduccionBatch batch : produccion.getBatches()) {

            batch.setRendimiento(
                    calcularRendimiento(batch));

            MovimientoLeche movimiento = movimientoLecheUseCase.registrarMovimiento(

                    produccion.getIdTanque(),

                    TipoMovimientoLeche.SALIDA_PRODUCCION,

                    batch.getLitrosConsumidos(),

                    produccion.getIdUsuario(),

                    construirReferencia(produccion, batch),

                    construirObservacion(produccion, batch));

            batch.setIdMovimientoLeche(
                    movimiento.getId());
        }

        return repository.guardar(produccion);
    }

    @Override
    public Produccion obtenerPorId(Long id) {
        return repository.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró la producción láctea con ID: " + id));
    }

    @Override
    public List<Produccion> listarTodas() {
        return repository.listarTodas();
    }

    @Override
    public List<Produccion> listarPorFecha(LocalDate fechaProduccion) {
        return repository.listarPorFecha(fechaProduccion);
    }

    private void validarProduccion(Produccion produccion) {
        if (produccion == null) {
            throw new ReglaNegocioException("La producción láctea es obligatoria.");
        }

        if (produccion.getFechaProduccion() == null) {
            throw new ReglaNegocioException("La fecha de producción es obligatoria.");
        }

        if (produccion.getIdOrdenProduccion() != null
                && ordenProduccionUseCase.obtenerPorId(produccion.getIdOrdenProduccion()).isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No existe una orden de produccion con ID: " + produccion.getIdOrdenProduccion());
        }

        if (produccion.getProducto() == null || produccion.getProducto().isBlank()) {
            throw new ReglaNegocioException("El producto producido es obligatorio.");
        }

        if (produccion.getIdTanque() == null) {
            throw new ReglaNegocioException("El tanque de leche es obligatorio.");
        }

        if (produccion.getIdUsuario() == null) {
            throw new ReglaNegocioException("El usuario responsable es obligatorio.");
        }

        if (produccion.getBatches() == null || produccion.getBatches().isEmpty()) {
            throw new ReglaNegocioException("Debe registrar al menos un batch de producción.");
        }

        validarBatches(produccion.getBatches());
    }

    private void validarBatches(List<ProduccionBatch> batches) {
        Set<Integer> numerosBatch = new HashSet<>();
        Set<Long> marmitasUsadas = new HashSet<>();

        for (ProduccionBatch batch : batches) {
            if (batch.getNumeroBatch() == null || batch.getNumeroBatch() <= 0) {
                throw new ReglaNegocioException("Cada batch debe tener un número válido.");
            }

            if (!numerosBatch.add(batch.getNumeroBatch())) {
                throw new ReglaNegocioException("No se puede repetir el número de batch: " + batch.getNumeroBatch());
            }

            if (batch.getIdMarmita() == null) {
                throw new ReglaNegocioException("Cada batch debe tener una marmita asignada.");
            }

            if (!marmitasUsadas.add(batch.getIdMarmita())) {
                throw new ReglaNegocioException(
                        "No se puede repetir la marmita en la misma producción: " + batch.getIdMarmita());
            }

            if (batch.getLitrosConsumidos() == null
                    || batch.getLitrosConsumidos().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ReglaNegocioException("Los litros consumidos deben ser mayores que cero.");
            }

            if (batch.getKilosProducidos() == null
                    || batch.getKilosProducidos().compareTo(BigDecimal.ZERO) <= 0) {
                throw new ReglaNegocioException("Los kilos producidos deben ser mayores que cero.");
            }
        }
    }

    private BigDecimal calcularRendimiento(ProduccionBatch batch) {
        return batch.getKilosProducidos()
                .divide(batch.getLitrosConsumidos(), 4, RoundingMode.HALF_UP);
    }

    private String construirReferencia(Produccion produccion, ProduccionBatch batch) {
        return "Producción " + produccion.getProducto()
                + " - Batch " + batch.getNumeroBatch()
                + " - Marmita " + batch.getIdMarmita();
    }

    private String construirObservacion(Produccion produccion, ProduccionBatch batch) {
        if (batch.getObservaciones() != null && !batch.getObservaciones().isBlank()) {
            return batch.getObservaciones();
        }

        if (produccion.getObservaciones() != null && !produccion.getObservaciones().isBlank()) {
            return produccion.getObservaciones();
        }

        return "Salida automática por producción láctea";
    }
}

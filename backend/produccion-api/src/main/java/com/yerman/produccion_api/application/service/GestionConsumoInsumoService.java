package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.ConsumoInsumo;
import com.yerman.produccion_api.domain.model.DetalleProduccion;
import com.yerman.produccion_api.domain.model.Insumo;
import com.yerman.produccion_api.domain.model.Produccion;
import com.yerman.produccion_api.domain.port.in.GestionConsumoInsumoUseCase;
import com.yerman.produccion_api.domain.port.out.ConsumoInsumoRepositoryPort;
import com.yerman.produccion_api.domain.port.out.DetalleProduccionRepositoryPort;
import com.yerman.produccion_api.domain.port.out.InsumoRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ProduccionRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GestionConsumoInsumoService implements GestionConsumoInsumoUseCase {

    private final ConsumoInsumoRepositoryPort consumoInsumoRepositoryPort;
    private final ProduccionRepositoryPort produccionRepositoryPort;
    private final DetalleProduccionRepositoryPort detalleProduccionRepositoryPort;
    private final InsumoRepositoryPort insumoRepositoryPort;

    public GestionConsumoInsumoService(ConsumoInsumoRepositoryPort consumoInsumoRepositoryPort,
            ProduccionRepositoryPort produccionRepositoryPort,
            DetalleProduccionRepositoryPort detalleProduccionRepositoryPort,
            InsumoRepositoryPort insumoRepositoryPort) {
        this.consumoInsumoRepositoryPort = consumoInsumoRepositoryPort;
        this.produccionRepositoryPort = produccionRepositoryPort;
        this.detalleProduccionRepositoryPort = detalleProduccionRepositoryPort;
        this.insumoRepositoryPort = insumoRepositoryPort;
    }

    @Override
    public ConsumoInsumo crearConsumoInsumo(ConsumoInsumo consumoInsumo) {
        validarDatosObligatorios(consumoInsumo);

        Produccion produccion = produccionRepositoryPort.buscarPorId(consumoInsumo.getIdProduccion())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producción no encontrada con id: " + consumoInsumo.getIdProduccion()));

        DetalleProduccion detalle = detalleProduccionRepositoryPort.buscarPorId(consumoInsumo.getIdDetalleProduccion())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Detalle de producción no encontrado con id: " + consumoInsumo.getIdDetalleProduccion()));

        Long idInsumo = consumoInsumo.getInsumo().getIdInsumo();

        Insumo insumo = insumoRepositoryPort.buscarPorId(idInsumo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Insumo no encontrado con id: " + idInsumo));

        validarConsistenciaProduccionDetalle(produccion, detalle);

        if (consumoInsumoRepositoryPort.existePorProduccionInsumoDetalle(
                consumoInsumo.getIdProduccion(),
                idInsumo,
                consumoInsumo.getIdDetalleProduccion())) {
            throw new ReglaNegocioException(
                    "Ya existe un consumo de insumo para esa producción, detalle e insumo");
        }

        consumoInsumo.setInsumo(insumo);
        consumoInsumo.setObservaciones(limpiarOpcional(consumoInsumo.getObservaciones()));
        consumoInsumo.setCreatedAt(LocalDateTime.now());
        consumoInsumo.setUpdatedAt(LocalDateTime.now());

        return consumoInsumoRepositoryPort.guardar(consumoInsumo);
    }

    @Override
    public Optional<ConsumoInsumo> obtenerPorId(Long id) {
        return consumoInsumoRepositoryPort.buscarPorId(id);
    }

    @Override
    public List<ConsumoInsumo> listarTodos() {
        return consumoInsumoRepositoryPort.listarTodos();
    }

    @Override
    public List<ConsumoInsumo> listarPorProduccion(Long idProduccion) {
        if (idProduccion == null) {
            throw new ReglaNegocioException("El id de la producción es obligatorio");
        }
        return consumoInsumoRepositoryPort.listarPorProduccion(idProduccion);
    }

    @Override
    public List<ConsumoInsumo> listarPorDetalleProduccion(Long idDetalleProduccion) {
        if (idDetalleProduccion == null) {
            throw new ReglaNegocioException("El id del detalle de producción es obligatorio");
        }
        return consumoInsumoRepositoryPort.listarPorDetalleProduccion(idDetalleProduccion);
    }

    @Override
    public List<ConsumoInsumo> listarPorInsumo(Long idInsumo) {
        if (idInsumo == null) {
            throw new ReglaNegocioException("El id del insumo es obligatorio");
        }
        return consumoInsumoRepositoryPort.listarPorInsumo(idInsumo);
    }

    @Override
    public Optional<ConsumoInsumo> obtenerPorProduccionInsumoDetalle(Long idProduccion, Long idInsumo,
            Long idDetalleProduccion) {
        if (idProduccion == null) {
            throw new ReglaNegocioException("El id de la producción es obligatorio");
        }
        if (idInsumo == null) {
            throw new ReglaNegocioException("El id del insumo es obligatorio");
        }
        if (idDetalleProduccion == null) {
            throw new ReglaNegocioException("El id del detalle de producción es obligatorio");
        }

        return consumoInsumoRepositoryPort.buscarPorProduccionInsumoDetalle(idProduccion, idInsumo,
                idDetalleProduccion);
    }

    public ConsumoInsumo obtenerPorIdObligatorio(Long id) {
        return consumoInsumoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Consumo de insumo no encontrado con id: " + id));
    }

    private void validarDatosObligatorios(ConsumoInsumo consumoInsumo) {
        if (consumoInsumo == null) {
            throw new ReglaNegocioException("El consumo de insumo es obligatorio");
        }

        if (consumoInsumo.getIdProduccion() == null) {
            throw new ReglaNegocioException("La producción del consumo es obligatoria");
        }

        if (consumoInsumo.getIdDetalleProduccion() == null) {
            throw new ReglaNegocioException("El detalle de producción del consumo es obligatorio");
        }

        if (consumoInsumo.getInsumo() == null || consumoInsumo.getInsumo().getIdInsumo() == null) {
            throw new ReglaNegocioException("El insumo es obligatorio");
        }

        if (consumoInsumo.getCantidadConsumida() == null
                || consumoInsumo.getCantidadConsumida().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("La cantidad consumida debe ser mayor que cero");
        }
    }

    private void validarConsistenciaProduccionDetalle(Produccion produccion, DetalleProduccion detalle) {
        if (detalle.getIdProduccion() == null) {
            throw new ReglaNegocioException("El detalle de producción no tiene una producción válida");
        }

        if (!produccion.getIdProduccion().equals(detalle.getIdProduccion())) {
            throw new ReglaNegocioException(
                    "El detalle de producción no pertenece a la producción indicada");
        }
    }

    private String limpiarOpcional(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }
}

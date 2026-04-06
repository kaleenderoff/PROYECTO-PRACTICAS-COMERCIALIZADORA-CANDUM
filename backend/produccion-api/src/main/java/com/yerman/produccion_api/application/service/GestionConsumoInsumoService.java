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

    public GestionConsumoInsumoService(
            ConsumoInsumoRepositoryPort consumoInsumoRepositoryPort,
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
        validarConsumoObligatorio(consumoInsumo);
        validarCamposObligatorios(consumoInsumo);

        Long idProduccion = consumoInsumo.getIdProduccion();
        Long idDetalleProduccion = consumoInsumo.getIdDetalleProduccion();
        Long idInsumo = consumoInsumo.getInsumo().getIdInsumo();

        Produccion produccion = produccionRepositoryPort.buscarPorId(idProduccion)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producción no encontrada con id: " + idProduccion));

        DetalleProduccion detalle = detalleProduccionRepositoryPort.buscarPorId(idDetalleProduccion)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Detalle de producción no encontrado con id: " + idDetalleProduccion));

        Insumo insumo = insumoRepositoryPort.buscarPorId(idInsumo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Insumo no encontrado con id: " + idInsumo));

        validarConsistenciaProduccionDetalle(produccion, detalle);
        validarInsumoActivo(insumo);
        validarDuplicadoConsumo(idProduccion, idInsumo, idDetalleProduccion);

        consumoInsumo.setInsumo(insumo);
        consumoInsumo.setObservaciones(normalizarTextoOpcional(consumoInsumo.getObservaciones()));
        consumoInsumo.setCreatedAt(LocalDateTime.now());
        consumoInsumo.setUpdatedAt(LocalDateTime.now());

        return consumoInsumoRepositoryPort.guardar(consumoInsumo);
    }

    @Override
    public Optional<ConsumoInsumo> obtenerPorId(Long id) {
        if (id == null) {
            throw new ReglaNegocioException("El id del consumo de insumo es obligatorio");
        }
        return consumoInsumoRepositoryPort.buscarPorId(id);
    }

    @Override
    public List<ConsumoInsumo> listarTodos() {
        return consumoInsumoRepositoryPort.listarTodos();
    }

    @Override
    public List<ConsumoInsumo> listarPorProduccion(Long idProduccion) {
        validarIdProduccion(idProduccion);
        return consumoInsumoRepositoryPort.listarPorProduccion(idProduccion);
    }

    @Override
    public List<ConsumoInsumo> listarPorDetalleProduccion(Long idDetalleProduccion) {
        validarIdDetalleProduccion(idDetalleProduccion);
        return consumoInsumoRepositoryPort.listarPorDetalleProduccion(idDetalleProduccion);
    }

    @Override
    public List<ConsumoInsumo> listarPorInsumo(Long idInsumo) {
        validarIdInsumo(idInsumo);
        return consumoInsumoRepositoryPort.listarPorInsumo(idInsumo);
    }

    @Override
    public Optional<ConsumoInsumo> obtenerPorProduccionInsumoDetalle(
            Long idProduccion,
            Long idInsumo,
            Long idDetalleProduccion) {
        validarIdProduccion(idProduccion);
        validarIdInsumo(idInsumo);
        validarIdDetalleProduccion(idDetalleProduccion);

        return consumoInsumoRepositoryPort.buscarPorProduccionInsumoDetalle(
                idProduccion,
                idInsumo,
                idDetalleProduccion);
    }

    public ConsumoInsumo obtenerPorIdObligatorio(Long id) {
        if (id == null) {
            throw new ReglaNegocioException("El id del consumo de insumo es obligatorio");
        }

        return consumoInsumoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Consumo de insumo no encontrado con id: " + id));
    }

    private void validarConsumoObligatorio(ConsumoInsumo consumoInsumo) {
        if (consumoInsumo == null) {
            throw new ReglaNegocioException("El consumo de insumo es obligatorio");
        }
    }

    private void validarCamposObligatorios(ConsumoInsumo consumoInsumo) {
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
        if (produccion.getIdProduccion() == null) {
            throw new ReglaNegocioException("La producción indicada no es válida");
        }

        if (detalle.getIdProduccion() == null) {
            throw new ReglaNegocioException("El detalle de producción no tiene una producción válida");
        }

        if (!produccion.getIdProduccion().equals(detalle.getIdProduccion())) {
            throw new ReglaNegocioException(
                    "El detalle de producción no pertenece a la producción indicada");
        }
    }

    private void validarInsumoActivo(Insumo insumo) {
        if (Boolean.FALSE.equals(insumo.getActivo())) {
            throw new ReglaNegocioException("No se puede registrar consumo con un insumo inactivo");
        }
    }

    private void validarDuplicadoConsumo(Long idProduccion, Long idInsumo, Long idDetalleProduccion) {
        if (consumoInsumoRepositoryPort.existePorProduccionInsumoDetalle(
                idProduccion,
                idInsumo,
                idDetalleProduccion)) {
            throw new ReglaNegocioException(
                    "Ya existe un consumo de insumo para esa producción, detalle e insumo");
        }
    }

    private void validarIdProduccion(Long idProduccion) {
        if (idProduccion == null) {
            throw new ReglaNegocioException("El id de la producción es obligatorio");
        }
    }

    private void validarIdDetalleProduccion(Long idDetalleProduccion) {
        if (idDetalleProduccion == null) {
            throw new ReglaNegocioException("El id del detalle de producción es obligatorio");
        }
    }

    private void validarIdInsumo(Long idInsumo) {
        if (idInsumo == null) {
            throw new ReglaNegocioException("El id del insumo es obligatorio");
        }
    }

    private String normalizarTextoOpcional(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }
}

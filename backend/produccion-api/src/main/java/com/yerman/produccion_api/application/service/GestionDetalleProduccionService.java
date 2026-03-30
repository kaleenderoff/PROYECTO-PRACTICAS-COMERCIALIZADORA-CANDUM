package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.domain.model.DetalleProduccion;
import com.yerman.produccion_api.domain.model.Producto;
import com.yerman.produccion_api.domain.model.Produccion;
import com.yerman.produccion_api.domain.port.in.GestionDetalleProduccionUseCase;
import com.yerman.produccion_api.domain.port.out.DetalleProduccionRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ProductoRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ProduccionRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GestionDetalleProduccionService implements GestionDetalleProduccionUseCase {

    private final DetalleProduccionRepositoryPort detalleProduccionRepositoryPort;
    private final ProduccionRepositoryPort produccionRepositoryPort;
    private final ProductoRepositoryPort productoRepositoryPort;

    public GestionDetalleProduccionService(DetalleProduccionRepositoryPort detalleProduccionRepositoryPort,
            ProduccionRepositoryPort produccionRepositoryPort,
            ProductoRepositoryPort productoRepositoryPort) {
        this.detalleProduccionRepositoryPort = detalleProduccionRepositoryPort;
        this.produccionRepositoryPort = produccionRepositoryPort;
        this.productoRepositoryPort = productoRepositoryPort;
    }

    @Override
    public DetalleProduccion crearDetalleProduccion(DetalleProduccion detalleProduccion) {
        validarDatosObligatorios(detalleProduccion);

        Produccion produccion = produccionRepositoryPort.buscarPorId(detalleProduccion.getIdProduccion())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producción no encontrada con id: " + detalleProduccion.getIdProduccion()));

        Long idProducto = detalleProduccion.getProducto().getIdProducto();

        Producto producto = productoRepositoryPort.buscarPorId(idProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con id: " + idProducto));

        validarConsistenciaLinea(produccion, producto);

        if (detalleProduccionRepositoryPort.existePorProduccionProductoBatch(
                detalleProduccion.getIdProduccion(),
                idProducto,
                detalleProduccion.getNumBatch())) {
            throw new IllegalArgumentException(
                    "Ya existe un detalle de producción para esa producción, producto y batch");
        }

        if (detalleProduccion.getKgBatch().compareTo(detalleProduccion.getKgProgramados()) > 0) {
            throw new IllegalArgumentException("El kgBatch no puede ser mayor que el kgProgramados");
        }

        detalleProduccion.setProducto(producto);
        detalleProduccion.setObservaciones(limpiarOpcional(detalleProduccion.getObservaciones()));
        detalleProduccion.setFechaHoraRegistro(LocalDateTime.now());
        detalleProduccion.setCreatedAt(LocalDateTime.now());
        detalleProduccion.setUpdatedAt(LocalDateTime.now());

        DetalleProduccion guardado = detalleProduccionRepositoryPort.guardar(detalleProduccion);

        return detalleProduccionRepositoryPort.buscarPorId(guardado.getIdDetalleProduccion())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Detalle de producción no encontrado después de guardar con id: "
                                + guardado.getIdDetalleProduccion()));
    }

    @Override
    public Optional<DetalleProduccion> obtenerPorId(Long id) {
        return detalleProduccionRepositoryPort.buscarPorId(id);
    }

    @Override
    public List<DetalleProduccion> listarTodos() {
        return detalleProduccionRepositoryPort.listarTodos();
    }

    @Override
    public List<DetalleProduccion> listarPorProduccion(Long idProduccion) {
        if (idProduccion == null) {
            throw new IllegalArgumentException("El id de la producción es obligatorio");
        }
        return detalleProduccionRepositoryPort.listarPorProduccion(idProduccion);
    }

    @Override
    public List<DetalleProduccion> listarPorProducto(Long idProducto) {
        if (idProducto == null) {
            throw new IllegalArgumentException("El id del producto es obligatorio");
        }
        return detalleProduccionRepositoryPort.listarPorProducto(idProducto);
    }

    @Override
    public Optional<DetalleProduccion> obtenerPorProduccionProductoBatch(Long idProduccion, Long idProducto,
            Integer numBatch) {
        if (idProduccion == null) {
            throw new IllegalArgumentException("El id de la producción es obligatorio");
        }
        if (idProducto == null) {
            throw new IllegalArgumentException("El id del producto es obligatorio");
        }
        if (numBatch == null) {
            throw new IllegalArgumentException("El número de batch es obligatorio");
        }

        return detalleProduccionRepositoryPort.buscarPorProduccionProductoBatch(idProduccion, idProducto, numBatch);
    }

    public DetalleProduccion obtenerPorIdObligatorio(Long id) {
        return detalleProduccionRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Detalle de producción no encontrado con id: " + id));
    }

    private void validarDatosObligatorios(DetalleProduccion detalleProduccion) {
        if (detalleProduccion == null) {
            throw new IllegalArgumentException("El detalle de producción es obligatorio");
        }

        if (detalleProduccion.getIdProduccion() == null) {
            throw new IllegalArgumentException("La producción del detalle es obligatoria");
        }

        if (detalleProduccion.getProducto() == null || detalleProduccion.getProducto().getIdProducto() == null) {
            throw new IllegalArgumentException("El producto del detalle es obligatorio");
        }

        if (detalleProduccion.getKgProgramados() == null
                || detalleProduccion.getKgProgramados().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Los kg programados deben ser mayores que cero");
        }

        if (detalleProduccion.getKgBatch() == null
                || detalleProduccion.getKgBatch().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Los kg batch deben ser mayores que cero");
        }

        if (detalleProduccion.getNumBatch() == null || detalleProduccion.getNumBatch() <= 0) {
            throw new IllegalArgumentException("El número de batch debe ser mayor que cero");
        }

        if (detalleProduccion.getUnidadesReales() == null || detalleProduccion.getUnidadesReales() < 0) {
            throw new IllegalArgumentException("Las unidades reales no pueden ser negativas");
        }
    }

    private void validarConsistenciaLinea(Produccion produccion, Producto producto) {
        if (produccion.getLineaProduccion() == null || produccion.getLineaProduccion().getIdLineaProduccion() == null) {
            throw new IllegalArgumentException("La producción no tiene una línea de producción válida");
        }

        if (producto.getLineaProduccion() == null || producto.getLineaProduccion().getIdLineaProduccion() == null) {
            throw new IllegalArgumentException("El producto no tiene una línea de producción válida");
        }

        if (!produccion.getLineaProduccion().getIdLineaProduccion()
                .equals(producto.getLineaProduccion().getIdLineaProduccion())) {
            throw new IllegalArgumentException(
                    "El producto no pertenece a la misma línea de producción de la cabecera");
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

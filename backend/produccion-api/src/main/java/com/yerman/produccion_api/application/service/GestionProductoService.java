package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.LineaProduccion;
import com.yerman.produccion_api.domain.model.Producto;
import com.yerman.produccion_api.domain.port.in.GestionProductoUseCase;
import com.yerman.produccion_api.domain.port.out.LineaProduccionRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ProductoRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GestionProductoService implements GestionProductoUseCase {

    private final ProductoRepositoryPort productoRepositoryPort;
    private final LineaProduccionRepositoryPort lineaProduccionRepositoryPort;

    public GestionProductoService(ProductoRepositoryPort productoRepositoryPort,
            LineaProduccionRepositoryPort lineaProduccionRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
        this.lineaProduccionRepositoryPort = lineaProduccionRepositoryPort;
    }

    @Override
    public Producto crearProducto(Producto producto) {
        validarDatosObligatorios(producto);

        String nombreLimpio = limpiar(producto.getNombre());
        String marcaLimpia = limpiarOpcional(producto.getMarca());
        String descripcionLimpia = limpiarOpcional(producto.getDescripcion());
        String unidadMedidaLimpia = limpiar(producto.getUnidadMedida());

        validarLineaProduccion(producto.getLineaProduccion());

        Long idLinea = producto.getLineaProduccion().getIdLineaProduccion();

        LineaProduccion linea = lineaProduccionRepositoryPort.buscarPorId(idLinea)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Línea de producción no encontrada con id: " + idLinea));

        if (productoRepositoryPort.existePorNombreGramajeMarca(
                nombreLimpio,
                producto.getGramajeG(),
                marcaLimpia)) {
            throw new ReglaNegocioException(
                    "Ya existe un producto con nombre, gramaje y marca iguales");
        }

        producto.setNombre(nombreLimpio);
        producto.setMarca(marcaLimpia);
        producto.setDescripcion(descripcionLimpia);
        producto.setUnidadMedida(unidadMedidaLimpia);
        producto.setLineaProduccion(linea);

        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }

        producto.setCreatedAt(LocalDateTime.now());
        producto.setUpdatedAt(LocalDateTime.now());

        return productoRepositoryPort.guardar(producto);
    }

    @Override
    public Optional<Producto> obtenerPorId(Long id) {
        return productoRepositoryPort.buscarPorId(id);
    }

    @Override
    public Optional<Producto> obtenerPorNombreGramajeMarca(String nombre, BigDecimal gramajeG, String marca) {
        return productoRepositoryPort.buscarPorNombreGramajeMarca(
                limpiar(nombre),
                gramajeG,
                limpiarOpcional(marca));
    }

    @Override
    public List<Producto> listarTodos() {
        return productoRepositoryPort.listarTodos();
    }

    @Override
    public List<Producto> listarActivos() {
        return productoRepositoryPort.listarActivos();
    }

    @Override
    public List<Producto> listarPorLineaProduccion(Long idLineaProduccion) {
        validarIdLineaProduccion(idLineaProduccion);
        return productoRepositoryPort.listarPorLineaProduccion(idLineaProduccion);
    }

    @Override
    public List<Producto> listarActivosPorLineaProduccion(Long idLineaProduccion) {
        validarIdLineaProduccion(idLineaProduccion);
        return productoRepositoryPort.listarActivosPorLineaProduccion(idLineaProduccion);
    }

    public Producto obtenerPorIdObligatorio(Long id) {
        return productoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con id: " + id));
    }

    private void validarDatosObligatorios(Producto producto) {
        if (producto == null) {
            throw new ReglaNegocioException("El producto es obligatorio");
        }

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new ReglaNegocioException("El nombre del producto es obligatorio");
        }

        if (producto.getGramajeG() == null || producto.getGramajeG().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("El gramaje del producto debe ser mayor que cero");
        }

        if (producto.getUnidadMedida() == null || producto.getUnidadMedida().trim().isEmpty()) {
            throw new ReglaNegocioException("La unidad de medida del producto es obligatoria");
        }
    }

    private void validarLineaProduccion(LineaProduccion lineaProduccion) {
        if (lineaProduccion == null || lineaProduccion.getIdLineaProduccion() == null) {
            throw new ReglaNegocioException("La línea de producción del producto es obligatoria");
        }
    }

    private void validarIdLineaProduccion(Long idLineaProduccion) {
        if (idLineaProduccion == null) {
            throw new ReglaNegocioException("El id de la línea de producción es obligatorio");
        }
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }

    private String limpiarOpcional(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }
}
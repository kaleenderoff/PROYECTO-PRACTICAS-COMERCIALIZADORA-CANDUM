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
        validarProductoObligatorio(producto);
        validarCamposObligatorios(producto);
        validarLineaProduccionInformada(producto.getLineaProduccion());

        String nombreNormalizado = normalizarTextoObligatorio(producto.getNombre());
        String descripcionNormalizada = normalizarTextoOpcional(producto.getDescripcion());
        String marcaNormalizada = normalizarTextoOpcional(producto.getMarca());
        String unidadMedidaNormalizada = normalizarUnidadMedida(producto.getUnidadMedida());

        Long idLineaProduccion = producto.getLineaProduccion().getIdLineaProduccion();

        LineaProduccion lineaProduccion = lineaProduccionRepositoryPort.buscarPorId(idLineaProduccion)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Línea de producción no encontrada con id: " + idLineaProduccion));

        validarLineaProduccionActiva(lineaProduccion);

        validarDuplicadoProducto(nombreNormalizado, producto.getGramajeG(), marcaNormalizada);

        producto.setNombre(nombreNormalizado);
        producto.setDescripcion(descripcionNormalizada);
        producto.setMarca(marcaNormalizada);
        producto.setUnidadMedida(unidadMedidaNormalizada);
        producto.setLineaProduccion(lineaProduccion);

        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }

        producto.setCreatedAt(LocalDateTime.now());
        producto.setUpdatedAt(LocalDateTime.now());

        return productoRepositoryPort.guardar(producto);
    }

    @Override
    public Optional<Producto> obtenerPorId(Long id) {
        if (id == null) {
            throw new ReglaNegocioException("El id del producto es obligatorio");
        }
        return productoRepositoryPort.buscarPorId(id);
    }

    @Override
    public Optional<Producto> obtenerPorNombreGramajeMarca(String nombre, BigDecimal gramajeG, String marca) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ReglaNegocioException("El nombre del producto es obligatorio");
        }

        if (gramajeG == null || gramajeG.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("El gramaje del producto debe ser mayor que cero");
        }

        return productoRepositoryPort.buscarPorNombreGramajeMarca(
                normalizarTextoObligatorio(nombre),
                gramajeG,
                normalizarTextoOpcional(marca));
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
        if (id == null) {
            throw new ReglaNegocioException("El id del producto es obligatorio");
        }

        return productoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con id: " + id));
    }

    private void validarProductoObligatorio(Producto producto) {
        if (producto == null) {
            throw new ReglaNegocioException("El producto es obligatorio");
        }
    }

    private void validarCamposObligatorios(Producto producto) {
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

    private void validarLineaProduccionInformada(LineaProduccion lineaProduccion) {
        if (lineaProduccion == null || lineaProduccion.getIdLineaProduccion() == null) {
            throw new ReglaNegocioException("La línea de producción del producto es obligatoria");
        }
    }

    private void validarLineaProduccionActiva(LineaProduccion lineaProduccion) {
        if (Boolean.FALSE.equals(lineaProduccion.getActivo())) {
            throw new ReglaNegocioException("No se puede asociar el producto a una línea de producción inactiva");
        }
    }

    private void validarDuplicadoProducto(String nombre, BigDecimal gramajeG, String marca) {
        if (productoRepositoryPort.existePorNombreGramajeMarca(nombre, gramajeG, marca)) {
            throw new ReglaNegocioException(
                    "Ya existe un producto con el mismo nombre, gramaje y marca");
        }
    }

    private void validarIdLineaProduccion(Long idLineaProduccion) {
        if (idLineaProduccion == null) {
            throw new ReglaNegocioException("El id de la línea de producción es obligatorio");
        }
    }

    private String normalizarTextoObligatorio(String valor) {
        return valor.trim();
    }

    private String normalizarTextoOpcional(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }

    private String normalizarUnidadMedida(String unidadMedida) {
        return unidadMedida.trim().toUpperCase();
    }
}
package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.EstadoProductoTerminadoLacteo;
import com.yerman.produccion_api.domain.model.ProductoTerminadoLacteo;
import com.yerman.produccion_api.domain.port.in.GestionProductoTerminadoLacteoUseCase;
import com.yerman.produccion_api.domain.port.out.ProductoTerminadoLacteoRepositoryPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GestionProductoTerminadoLacteoService implements GestionProductoTerminadoLacteoUseCase {

    private final ProductoTerminadoLacteoRepositoryPort repository;

    public GestionProductoTerminadoLacteoService(ProductoTerminadoLacteoRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public ProductoTerminadoLacteo registrarProductoTerminado(ProductoTerminadoLacteo productoTerminado) {
        validarProductoTerminado(productoTerminado);

        if (productoTerminado.getKilosDisponibles() == null) {
            productoTerminado.setKilosDisponibles(productoTerminado.getKilosProducidos());
        }

        if (productoTerminado.getEstado() == null) {
            productoTerminado.setEstado(EstadoProductoTerminadoLacteo.DISPONIBLE);
        }

        return repository.guardar(productoTerminado);
    }

    @Override
    public ProductoTerminadoLacteo obtenerPorId(Long id) {
        return repository.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró el producto terminado lácteo con ID: " + id));
    }

    @Override
    public List<ProductoTerminadoLacteo> listarTodos() {
        return repository.listarTodos();
    }

    @Override
    public List<ProductoTerminadoLacteo> listarPorEstado(EstadoProductoTerminadoLacteo estado) {
        return repository.listarPorEstado(estado);
    }

    @Override
    public List<ProductoTerminadoLacteo> listarPorProducto(String producto) {
        return repository.listarPorProducto(producto);
    }

    private void validarProductoTerminado(ProductoTerminadoLacteo productoTerminado) {
        if (productoTerminado == null) {
            throw new ReglaNegocioException("El producto terminado es obligatorio.");
        }

        if (productoTerminado.getIdProduccionLacteaBatch() == null) {
            throw new ReglaNegocioException("El batch de producción es obligatorio.");
        }

        if (productoTerminado.getProducto() == null || productoTerminado.getProducto().isBlank()) {
            throw new ReglaNegocioException("El nombre del producto es obligatorio.");
        }

        if (productoTerminado.getKilosProducidos() == null
                || productoTerminado.getKilosProducidos().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("Los kilos producidos deben ser mayores que cero.");
        }

        if (productoTerminado.getKilosDisponibles() != null
                && productoTerminado.getKilosDisponibles().compareTo(BigDecimal.ZERO) < 0) {
            throw new ReglaNegocioException("Los kilos disponibles no pueden ser negativos.");
        }

        if (productoTerminado.getKilosDisponibles() != null
                && productoTerminado.getKilosDisponibles().compareTo(productoTerminado.getKilosProducidos()) > 0) {
            throw new ReglaNegocioException("Los kilos disponibles no pueden ser mayores a los kilos producidos.");
        }
    }
}
package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.domain.model.ProductoTerminado;
import com.yerman.produccion_api.domain.port.in.GestionProductoTerminadoUseCase;
import com.yerman.produccion_api.domain.port.out.ProductoRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ProductoTerminadoRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoTerminadoService implements GestionProductoTerminadoUseCase {

    private final ProductoTerminadoRepositoryPort productoTerminadoRepositoryPort;
    private final ProductoRepositoryPort productoRepositoryPort;

    public ProductoTerminadoService(ProductoTerminadoRepositoryPort productoTerminadoRepositoryPort,
            ProductoRepositoryPort productoRepositoryPort) {
        this.productoTerminadoRepositoryPort = productoTerminadoRepositoryPort;
        this.productoRepositoryPort = productoRepositoryPort;
    }

    @Override
    public ProductoTerminado crearProductoTerminado(ProductoTerminado productoTerminado) {
        validarProductoTerminado(productoTerminado);

        if (productoTerminadoRepositoryPort.existePorSku(productoTerminado.getSku())) {
            throw new IllegalArgumentException("Ya existe un producto terminado con ese SKU");
        }

        if (productoTerminado.getProductoBase() == null
                || productoTerminado.getProductoBase().getIdProducto() == null) {
            throw new IllegalArgumentException("Debe asociarse un producto base válido");
        }

        boolean productoBaseExiste = productoRepositoryPort
                .buscarPorId(productoTerminado.getProductoBase().getIdProducto())
                .isPresent();
        if (!productoBaseExiste) {
            throw new IllegalArgumentException("El producto base asociado no existe");
        }

        LocalDateTime ahora = LocalDateTime.now();
        productoTerminado.setActivo(productoTerminado.getActivo() != null ? productoTerminado.getActivo() : true);
        productoTerminado.setCreatedAt(ahora);
        productoTerminado.setUpdatedAt(ahora);

        return productoTerminadoRepositoryPort.guardar(productoTerminado);
    }

    @Override
    public ProductoTerminado actualizarProductoTerminado(Long id, ProductoTerminado productoTerminado) {
        ProductoTerminado existente = productoTerminadoRepositoryPort.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto terminado no encontrado"));

        validarProductoTerminado(productoTerminado);

        Optional<ProductoTerminado> porSku = productoTerminadoRepositoryPort.obtenerPorSku(productoTerminado.getSku());
        if (porSku.isPresent() && !porSku.get().getId().equals(id)) {
            throw new IllegalArgumentException("Ya existe otro producto terminado con ese SKU");
        }

        if (productoTerminado.getProductoBase() == null
                || productoTerminado.getProductoBase().getIdProducto() == null) {
            throw new IllegalArgumentException("Debe asociarse un producto base válido");
        }

        boolean productoBaseExiste = productoRepositoryPort
                .buscarPorId(productoTerminado.getProductoBase().getIdProducto())
                .isPresent();
        if (!productoBaseExiste) {
            throw new IllegalArgumentException("El producto base asociado no existe");
        }

        existente.setProductoBase(productoTerminado.getProductoBase());
        existente.setSku(productoTerminado.getSku());
        existente.setNombreComercial(productoTerminado.getNombreComercial());
        existente.setReferencia(productoTerminado.getReferencia());
        existente.setGramajeG(productoTerminado.getGramajeG());
        existente.setUnidadMedida(productoTerminado.getUnidadMedida());
        existente.setEmbalaje(productoTerminado.getEmbalaje());
        existente.setActivo(
                productoTerminado.getActivo() != null ? productoTerminado.getActivo() : existente.getActivo());
        existente.setUpdatedAt(LocalDateTime.now());

        return productoTerminadoRepositoryPort.guardar(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoTerminado> obtenerPorId(Long id) {
        return productoTerminadoRepositoryPort.obtenerPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoTerminado> obtenerPorSku(String sku) {
        return productoTerminadoRepositoryPort.obtenerPorSku(sku);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoTerminado> listarActivos() {
        return productoTerminadoRepositoryPort.listarActivos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoTerminado> listarTodos() {
        return productoTerminadoRepositoryPort.listarTodos();
    }

    @Override
    public void cambiarEstado(Long id, boolean activo) {
        ProductoTerminado existente = productoTerminadoRepositoryPort.obtenerPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto terminado no encontrado"));

        existente.setActivo(activo);
        existente.setUpdatedAt(LocalDateTime.now());

        productoTerminadoRepositoryPort.guardar(existente);
    }

    private void validarProductoTerminado(ProductoTerminado productoTerminado) {
        if (productoTerminado == null) {
            throw new IllegalArgumentException("El producto terminado no puede ser nulo");
        }
        if (productoTerminado.getSku() == null || productoTerminado.getSku().trim().isEmpty()) {
            throw new IllegalArgumentException("El SKU es obligatorio");
        }
        if (productoTerminado.getNombreComercial() == null || productoTerminado.getNombreComercial().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre comercial es obligatorio");
        }
        if (productoTerminado.getGramajeG() == null || productoTerminado.getGramajeG().doubleValue() <= 0) {
            throw new IllegalArgumentException("El gramaje debe ser mayor a cero");
        }
        if (productoTerminado.getUnidadMedida() == null || productoTerminado.getUnidadMedida().trim().isEmpty()) {
            throw new IllegalArgumentException("La unidad de medida es obligatoria");
        }
    }
}
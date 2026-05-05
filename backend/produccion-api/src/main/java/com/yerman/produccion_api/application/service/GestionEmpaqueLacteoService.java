package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.EmpaqueLacteo;
import com.yerman.produccion_api.domain.model.EstadoEmpaqueLacteo;
import com.yerman.produccion_api.domain.model.EstadoProductoTerminadoLacteo;
import com.yerman.produccion_api.domain.model.ProductoTerminadoLacteo;
import com.yerman.produccion_api.domain.port.in.GestionEmpaqueLacteoUseCase;
import com.yerman.produccion_api.domain.port.out.EmpaqueLacteoRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ProduccionLacteaBatchRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ProductoTerminadoLacteoRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class GestionEmpaqueLacteoService implements GestionEmpaqueLacteoUseCase {

    private final EmpaqueLacteoRepositoryPort empaqueLacteoRepositoryPort;
    private final ProductoTerminadoLacteoRepositoryPort productoTerminadoLacteoRepositoryPort;
    private final ProduccionLacteaBatchRepositoryPort produccionLacteaBatchRepositoryPort;

    public GestionEmpaqueLacteoService(
            EmpaqueLacteoRepositoryPort empaqueLacteoRepositoryPort,
            ProductoTerminadoLacteoRepositoryPort productoTerminadoLacteoRepositoryPort,
            ProduccionLacteaBatchRepositoryPort produccionLacteaBatchRepositoryPort) {
        this.empaqueLacteoRepositoryPort = empaqueLacteoRepositoryPort;
        this.productoTerminadoLacteoRepositoryPort = productoTerminadoLacteoRepositoryPort;
        this.produccionLacteaBatchRepositoryPort = produccionLacteaBatchRepositoryPort;
    }

    @Override
    @Transactional
    public EmpaqueLacteo crear(EmpaqueLacteo empaqueLacteo) {
        validarLote(empaqueLacteo);
        validarUnidades(empaqueLacteo);
        validarCajas(empaqueLacteo);
        validarBatchExiste(empaqueLacteo.getProduccionLacteaBatchId());

        ProductoTerminadoLacteo productoTerminado = productoTerminadoLacteoRepositoryPort
                .obtenerPorId(empaqueLacteo.getProductoTerminadoLacteoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto terminado lácteo no encontrado"));

        validarRelacionBatchProducto(productoTerminado, empaqueLacteo);
        validarProductoDisponible(productoTerminado);
        validarKilosDisponibles(productoTerminado, empaqueLacteo);
        validarPesoTotal(empaqueLacteo);

        BigDecimal nuevoSaldo = productoTerminado.getKilosDisponibles()
                .subtract(empaqueLacteo.getKilosUtilizados());

        productoTerminado.setKilosDisponibles(nuevoSaldo);
        productoTerminado.setEstado(calcularEstadoProductoTerminado(productoTerminado));

        productoTerminadoLacteoRepositoryPort.guardar(productoTerminado);

        empaqueLacteo.setEstado(EstadoEmpaqueLacteo.REGISTRADO);

        return empaqueLacteoRepositoryPort.guardar(empaqueLacteo);
    }

    @Override
    public Optional<EmpaqueLacteo> obtenerPorId(Long id) {
        return empaqueLacteoRepositoryPort.buscarPorId(id);
    }

    @Override
    public List<EmpaqueLacteo> listarTodos() {
        return empaqueLacteoRepositoryPort.listarTodos();
    }

    @Override
    public List<EmpaqueLacteo> listarPorProductoTerminadoLacteo(Long productoTerminadoLacteoId) {
        return empaqueLacteoRepositoryPort.listarPorProductoTerminadoLacteo(productoTerminadoLacteoId);
    }

    @Override
    public List<EmpaqueLacteo> listarPorLoteEmpaque(String loteEmpaque) {
        return empaqueLacteoRepositoryPort.listarPorLoteEmpaque(loteEmpaque);
    }

    @Override
    @Transactional
    public EmpaqueLacteo anular(Long id) {
        EmpaqueLacteo empaqueLacteo = empaqueLacteoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empaque lácteo no encontrado"));

        if (EstadoEmpaqueLacteo.ANULADO.equals(empaqueLacteo.getEstado())) {
            throw new ReglaNegocioException("El empaque lácteo ya se encuentra anulado");
        }

        ProductoTerminadoLacteo productoTerminado = productoTerminadoLacteoRepositoryPort
                .obtenerPorId(empaqueLacteo.getProductoTerminadoLacteoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto terminado lácteo no encontrado"));

        BigDecimal nuevoSaldo = productoTerminado.getKilosDisponibles()
                .add(empaqueLacteo.getKilosUtilizados());

        if (nuevoSaldo.compareTo(productoTerminado.getKilosProducidos()) > 0) {
            throw new ReglaNegocioException(
                    "No se puede anular el empaque porque el saldo superaría los kilos producidos");
        }

        productoTerminado.setKilosDisponibles(nuevoSaldo);
        productoTerminado.setEstado(calcularEstadoProductoTerminado(productoTerminado));

        productoTerminadoLacteoRepositoryPort.guardar(productoTerminado);

        empaqueLacteo.setEstado(EstadoEmpaqueLacteo.ANULADO);

        return empaqueLacteoRepositoryPort.guardar(empaqueLacteo);
    }

    private void validarLote(EmpaqueLacteo empaqueLacteo) {
        if (empaqueLacteo.getLoteEmpaque() == null
                || empaqueLacteo.getLoteEmpaque().isBlank()) {
            throw new ReglaNegocioException("El lote de empaque es obligatorio");
        }
    }

    private void validarUnidades(EmpaqueLacteo empaqueLacteo) {
        if (empaqueLacteo.getUnidades() == null || empaqueLacteo.getUnidades() <= 0) {
            throw new ReglaNegocioException("Las unidades deben ser mayores a cero");
        }
    }

    private void validarCajas(EmpaqueLacteo empaqueLacteo) {
        if (empaqueLacteo.getCajas() != null
                && empaqueLacteo.getCajas().compareTo(BigDecimal.ZERO) < 0) {
            throw new ReglaNegocioException("Las cajas no pueden ser negativas");
        }
    }

    private void validarBatchExiste(Long produccionLacteaBatchId) {
        if (produccionLacteaBatchId == null) {
            throw new ReglaNegocioException("Debe indicar el batch de producción láctea");
        }

        if (!produccionLacteaBatchRepositoryPort.existePorId(produccionLacteaBatchId)) {
            throw new RecursoNoEncontradoException("Batch de producción láctea no encontrado");
        }
    }

    private void validarRelacionBatchProducto(
            ProductoTerminadoLacteo productoTerminado,
            EmpaqueLacteo empaqueLacteo) {

        if (productoTerminado.getIdProduccionLacteaBatch() == null
                || !productoTerminado.getIdProduccionLacteaBatch()
                        .equals(empaqueLacteo.getProduccionLacteaBatchId())) {
            throw new ReglaNegocioException(
                    "El batch del empaque no corresponde al producto terminado");
        }
    }

    private void validarProductoDisponible(ProductoTerminadoLacteo productoTerminado) {
        if (EstadoProductoTerminadoLacteo.EMPACADO.equals(productoTerminado.getEstado())) {
            throw new ReglaNegocioException("El producto terminado ya se encuentra totalmente empacado");
        }

        if (productoTerminado.getKilosDisponibles() == null
                || productoTerminado.getKilosDisponibles().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("El producto terminado no tiene kilos disponibles para empacar");
        }
    }

    private void validarKilosDisponibles(
            ProductoTerminadoLacteo productoTerminado,
            EmpaqueLacteo empaqueLacteo) {

        if (empaqueLacteo.getKilosUtilizados() == null
                || empaqueLacteo.getKilosUtilizados().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("Los kilos utilizados deben ser mayores a cero");
        }

        if (empaqueLacteo.getKilosUtilizados().compareTo(productoTerminado.getKilosDisponibles()) > 0) {
            throw new ReglaNegocioException("No hay suficientes kilos disponibles para realizar el empaque");
        }
    }

    private void validarPesoTotal(EmpaqueLacteo empaqueLacteo) {
        if (empaqueLacteo.getPesoTotalKg() == null
                || empaqueLacteo.getPesoTotalKg().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("El peso total en kg debe ser mayor a cero");
        }

        if (empaqueLacteo.getPesoTotalKg().compareTo(empaqueLacteo.getKilosUtilizados()) > 0) {
            throw new ReglaNegocioException("El peso total del empaque no puede superar los kilos utilizados");
        }
    }

    private EstadoProductoTerminadoLacteo calcularEstadoProductoTerminado(ProductoTerminadoLacteo productoTerminado) {
        if (productoTerminado.getKilosDisponibles().compareTo(BigDecimal.ZERO) == 0) {
            return EstadoProductoTerminadoLacteo.EMPACADO;
        }

        if (productoTerminado.getKilosDisponibles().compareTo(productoTerminado.getKilosProducidos()) < 0) {
            return EstadoProductoTerminadoLacteo.PARCIALMENTE_EMPACADO;
        }

        return EstadoProductoTerminadoLacteo.DISPONIBLE;
    }
}
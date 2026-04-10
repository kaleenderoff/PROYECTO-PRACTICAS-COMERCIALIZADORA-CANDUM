package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.DetalleProduccion;
import com.yerman.produccion_api.domain.model.Empaque;
import com.yerman.produccion_api.domain.model.EstadoEmpaque;
import com.yerman.produccion_api.domain.port.in.GestionEmpaqueUseCase;
import com.yerman.produccion_api.domain.port.out.DetalleProduccionRepositoryPort;
import com.yerman.produccion_api.domain.port.out.EmpaqueRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ProductoTerminadoRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpaqueService implements GestionEmpaqueUseCase {

    private final EmpaqueRepositoryPort empaqueRepositoryPort;
    private final DetalleProduccionRepositoryPort detalleProduccionRepositoryPort;
    private final ProductoTerminadoRepositoryPort productoTerminadoRepositoryPort;

    public EmpaqueService(EmpaqueRepositoryPort empaqueRepositoryPort,
            DetalleProduccionRepositoryPort detalleProduccionRepositoryPort,
            ProductoTerminadoRepositoryPort productoTerminadoRepositoryPort) {
        this.empaqueRepositoryPort = empaqueRepositoryPort;
        this.detalleProduccionRepositoryPort = detalleProduccionRepositoryPort;
        this.productoTerminadoRepositoryPort = productoTerminadoRepositoryPort;
    }

    @Override
    public Empaque registrarEmpaque(Empaque empaque) {
        validarEmpaque(empaque);

        if (empaque.getDetalleProduccion() == null || empaque.getDetalleProduccion().getIdDetalleProduccion() == null) {
            throw new ReglaNegocioException("Debe asociarse un detalle de producción válido");
        }

        if (empaque.getProductoTerminado() == null || empaque.getProductoTerminado().getId() == null) {
            throw new ReglaNegocioException("Debe asociarse un producto terminado válido");
        }

        DetalleProduccion detalle = detalleProduccionRepositoryPort
                .buscarPorId(empaque.getDetalleProduccion().getIdDetalleProduccion())
                .orElseThrow(() -> new RecursoNoEncontradoException("El detalle de producción asociado no existe"));

        boolean productoTerminadoExiste = productoTerminadoRepositoryPort
                .obtenerPorId(empaque.getProductoTerminado().getId())
                .isPresent();

        if (!productoTerminadoExiste) {
            throw new RecursoNoEncontradoException("El producto terminado asociado no existe");
        }

        validarCantidadAcumulada(null, empaque, detalle);

        LocalDateTime ahora = LocalDateTime.now();
        empaque.setEstado(calcularEstadoEmpaque(detalle, empaque, null));
        empaque.setCreatedAt(ahora);
        empaque.setUpdatedAt(ahora);

        return empaqueRepositoryPort.guardar(empaque);
    }

    @Override
    public Empaque actualizarEmpaque(Long id, Empaque empaque) {
        Empaque existente = empaqueRepositoryPort.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empaque no encontrado"));

        validarEmpaque(empaque);

        if (empaque.getDetalleProduccion() == null || empaque.getDetalleProduccion().getIdDetalleProduccion() == null) {
            throw new ReglaNegocioException("Debe asociarse un detalle de producción válido");
        }

        if (empaque.getProductoTerminado() == null || empaque.getProductoTerminado().getId() == null) {
            throw new ReglaNegocioException("Debe asociarse un producto terminado válido");
        }

        DetalleProduccion detalle = detalleProduccionRepositoryPort
                .buscarPorId(empaque.getDetalleProduccion().getIdDetalleProduccion())
                .orElseThrow(() -> new RecursoNoEncontradoException("El detalle de producción asociado no existe"));

        boolean productoTerminadoExiste = productoTerminadoRepositoryPort
                .obtenerPorId(empaque.getProductoTerminado().getId())
                .isPresent();

        if (!productoTerminadoExiste) {
            throw new RecursoNoEncontradoException("El producto terminado asociado no existe");
        }

        validarCantidadAcumulada(existente.getId(), empaque, detalle);

        existente.setDetalleProduccion(empaque.getDetalleProduccion());
        existente.setProductoTerminado(empaque.getProductoTerminado());
        existente.setLoteEmpaque(empaque.getLoteEmpaque());
        existente.setFechaEmpaque(empaque.getFechaEmpaque());
        existente.setFechaVencimiento(empaque.getFechaVencimiento());
        existente.setEstado(calcularEstadoEmpaque(detalle, empaque, existente.getId()));
        existente.setCantidadUnidades(empaque.getCantidadUnidades());
        existente.setCantidadCajas(empaque.getCantidadCajas());
        existente.setPesoTotalKg(empaque.getPesoTotalKg());
        existente.setObservaciones(empaque.getObservaciones());
        existente.setUpdatedAt(LocalDateTime.now());

        return empaqueRepositoryPort.guardar(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Empaque> obtenerPorId(Long id) {
        return empaqueRepositoryPort.obtenerPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empaque> listarTodos() {
        return empaqueRepositoryPort.listarTodos();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empaque> listarPorDetalleProduccion(Long idDetalleProduccion) {
        return empaqueRepositoryPort.listarPorDetalleProduccion(idDetalleProduccion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empaque> listarPorProductoTerminado(Long idProductoTerminado) {
        return empaqueRepositoryPort.listarPorProductoTerminado(idProductoTerminado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empaque> listarPorLoteEmpaque(String loteEmpaque) {
        return empaqueRepositoryPort.listarPorLoteEmpaque(loteEmpaque);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empaque> listarPorRangoFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return empaqueRepositoryPort.listarPorRangoFecha(fechaInicio, fechaFin);
    }

    private void validarEmpaque(Empaque empaque) {
        if (empaque == null) {
            throw new ReglaNegocioException("El empaque no puede ser nulo");
        }
        if (empaque.getLoteEmpaque() == null || empaque.getLoteEmpaque().trim().isEmpty()) {
            throw new ReglaNegocioException("El lote de empaque es obligatorio");
        }
        if (empaque.getFechaEmpaque() == null) {
            throw new ReglaNegocioException("La fecha de empaque es obligatoria");
        }
        if (empaque.getCantidadUnidades() == null || empaque.getCantidadUnidades() <= 0) {
            throw new ReglaNegocioException("La cantidad de unidades debe ser mayor a cero");
        }
        if (empaque.getCantidadCajas() != null && empaque.getCantidadCajas() < 0) {
            throw new ReglaNegocioException("La cantidad de cajas no puede ser negativa");
        }
        if (empaque.getPesoTotalKg() != null && empaque.getPesoTotalKg().compareTo(BigDecimal.ZERO) < 0) {
            throw new ReglaNegocioException("El peso total en kg no puede ser negativo");
        }
    }

    private void validarCantidadAcumulada(Long idEmpaqueActual, Empaque empaque, DetalleProduccion detalle) {
        int yaEmpacado = empaqueRepositoryPort
                .listarPorDetalleProduccion(detalle.getIdDetalleProduccion())
                .stream()
                .filter(item -> idEmpaqueActual == null || !item.getId().equals(idEmpaqueActual))
                .map(Empaque::getCantidadUnidades)
                .filter(valor -> valor != null)
                .mapToInt(Integer::intValue)
                .sum();

        int totalConNuevoRegistro = yaEmpacado + empaque.getCantidadUnidades();

        if (detalle.getUnidadesReales() == null) {
            throw new ReglaNegocioException("El detalle de producción no tiene unidades reales registradas");
        }

        if (totalConNuevoRegistro > detalle.getUnidadesReales()) {
            throw new ReglaNegocioException("No se puede empacar más unidades de las producidas");
        }
    }

    private EstadoEmpaque calcularEstadoEmpaque(DetalleProduccion detalle, Empaque empaque, Long idEmpaqueActual) {
        int yaEmpacado = empaqueRepositoryPort
                .listarPorDetalleProduccion(detalle.getIdDetalleProduccion())
                .stream()
                .filter(item -> idEmpaqueActual == null || !item.getId().equals(idEmpaqueActual))
                .map(Empaque::getCantidadUnidades)
                .filter(valor -> valor != null)
                .mapToInt(Integer::intValue)
                .sum();

        int totalConNuevoRegistro = yaEmpacado + empaque.getCantidadUnidades();

        if (detalle.getUnidadesReales() != null && totalConNuevoRegistro == detalle.getUnidadesReales()) {
            return EstadoEmpaque.COMPLETADO;
        }

        return EstadoEmpaque.EN_PROCESO;
    }
}
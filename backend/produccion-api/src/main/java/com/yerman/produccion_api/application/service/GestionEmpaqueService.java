package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.AccionAuditoria;
import com.yerman.produccion_api.domain.model.DetalleProduccion;
import com.yerman.produccion_api.domain.model.Empaque;
import com.yerman.produccion_api.domain.model.EntidadAuditoria;
import com.yerman.produccion_api.domain.model.EstadoValidacion;
import com.yerman.produccion_api.domain.model.ProductoTerminado;
import com.yerman.produccion_api.domain.model.Validacion;
import com.yerman.produccion_api.domain.port.in.GestionAuditoriaUseCase;
import com.yerman.produccion_api.domain.port.in.GestionEmpaqueUseCase;
import com.yerman.produccion_api.domain.port.out.DetalleProduccionRepositoryPort;
import com.yerman.produccion_api.domain.port.out.EmpaqueRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ProductoTerminadoRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ValidacionRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GestionEmpaqueService implements GestionEmpaqueUseCase {

    private static final String LOTE_EMPAQUE_OBLIGATORIO_STRING = "El lote de empaque es obligatorio";

    private final EmpaqueRepositoryPort empaqueRepositoryPort;
    private final DetalleProduccionRepositoryPort detalleProduccionRepositoryPort;
    private final ProductoTerminadoRepositoryPort productoTerminadoRepositoryPort;
    private final ValidacionRepositoryPort validacionRepositoryPort;
    private final GestionAuditoriaUseCase gestionAuditoriaUseCase;

    public GestionEmpaqueService(
            EmpaqueRepositoryPort empaqueRepositoryPort,
            DetalleProduccionRepositoryPort detalleProduccionRepositoryPort,
            ProductoTerminadoRepositoryPort productoTerminadoRepositoryPort,
            ValidacionRepositoryPort validacionRepositoryPort,
            GestionAuditoriaUseCase gestionAuditoriaUseCase) {
        this.empaqueRepositoryPort = empaqueRepositoryPort;
        this.detalleProduccionRepositoryPort = detalleProduccionRepositoryPort;
        this.productoTerminadoRepositoryPort = productoTerminadoRepositoryPort;
        this.validacionRepositoryPort = validacionRepositoryPort;
        this.gestionAuditoriaUseCase = gestionAuditoriaUseCase;
    }

    @Override
    public Empaque registrarEmpaque(Empaque empaque) {
        validarDatosObligatorios(empaque);

        Long idDetalleProduccion = empaque.getDetalleProduccion().getIdDetalleProduccion();
        Long idProductoTerminado = empaque.getProductoTerminado().getId();

        DetalleProduccion detalleProduccion = detalleProduccionRepositoryPort.buscarPorId(idDetalleProduccion)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Detalle de producción no encontrado con id: " + idDetalleProduccion));

        ProductoTerminado productoTerminado = productoTerminadoRepositoryPort.obtenerPorId(idProductoTerminado)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto terminado no encontrado con id: " + idProductoTerminado));

        validarDetalleConValidacionAprobada(idDetalleProduccion);

        empaque.setDetalleProduccion(detalleProduccion);
        empaque.setProductoTerminado(productoTerminado);
        empaque.setLoteEmpaque(limpiarObligatorio(empaque.getLoteEmpaque(), LOTE_EMPAQUE_OBLIGATORIO_STRING));
        empaque.setObservaciones(limpiarOpcional(empaque.getObservaciones()));
        empaque.setFechaEmpaque(
                empaque.getFechaEmpaque() != null ? empaque.getFechaEmpaque() : LocalDateTime.now());
        empaque.setCreatedAt(LocalDateTime.now());
        empaque.setUpdatedAt(LocalDateTime.now());

        validarConsistenciaFechas(empaque);

        Empaque guardado = empaqueRepositoryPort.guardar(empaque);

        gestionAuditoriaUseCase.registrar(
                4L, // cámbialo luego por el usuario autenticado real si ya lo manejas
                AccionAuditoria.CREACION,
                EntidadAuditoria.EMPAQUE,
                guardado.getId(),
                "Se registró empaque con id " + guardado.getId()
                        + " para detalle de producción " + guardado.getDetalleProduccion().getIdDetalleProduccion()
                        + " y producto terminado " + guardado.getProductoTerminado().getId()
                        + " con lote " + guardado.getLoteEmpaque());

        return guardado;
    }

    @Override
    public Empaque actualizarEmpaque(Long id, Empaque empaque) {
        if (id == null) {
            throw new ReglaNegocioException("El id del empaque es obligatorio");
        }

        validarDatosObligatorios(empaque);

        Empaque existente = empaqueRepositoryPort.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Empaque no encontrado con id: " + id));

        Long idDetalleProduccion = empaque.getDetalleProduccion().getIdDetalleProduccion();
        Long idProductoTerminado = empaque.getProductoTerminado().getId();

        DetalleProduccion detalleProduccion = detalleProduccionRepositoryPort.buscarPorId(idDetalleProduccion)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Detalle de producción no encontrado con id: " + idDetalleProduccion));

        ProductoTerminado productoTerminado = productoTerminadoRepositoryPort.obtenerPorId(idProductoTerminado)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto terminado no encontrado con id: " + idProductoTerminado));

        validarDetalleConValidacionAprobada(idDetalleProduccion);

        existente.setDetalleProduccion(detalleProduccion);
        existente.setProductoTerminado(productoTerminado);
        existente.setLoteEmpaque(limpiarObligatorio(empaque.getLoteEmpaque(), LOTE_EMPAQUE_OBLIGATORIO_STRING));
        existente.setFechaEmpaque(
                empaque.getFechaEmpaque() != null ? empaque.getFechaEmpaque() : existente.getFechaEmpaque());
        existente.setFechaVencimiento(empaque.getFechaVencimiento());
        existente.setEstado(empaque.getEstado());
        existente.setCantidadUnidades(empaque.getCantidadUnidades());
        existente.setCantidadCajas(empaque.getCantidadCajas());
        existente.setPesoTotalKg(empaque.getPesoTotalKg());
        existente.setObservaciones(limpiarOpcional(empaque.getObservaciones()));
        existente.setUpdatedAt(LocalDateTime.now());

        validarConsistenciaFechas(existente);

        Empaque actualizado = empaqueRepositoryPort.guardar(existente);

        gestionAuditoriaUseCase.registrar(
                4L, // cámbialo luego por el usuario autenticado real
                AccionAuditoria.ACTUALIZACION,
                EntidadAuditoria.EMPAQUE,
                actualizado.getId(),
                "Se actualizó empaque con id " + actualizado.getId()
                        + " con lote " + actualizado.getLoteEmpaque()
                        + " y estado " + actualizado.getEstado().name());

        return actualizado;
    }

    @Override
    public Optional<Empaque> obtenerPorId(Long id) {
        if (id == null) {
            throw new ReglaNegocioException("El id del empaque es obligatorio");
        }
        return empaqueRepositoryPort.obtenerPorId(id);
    }

    @Override
    public List<Empaque> listarTodos() {
        return empaqueRepositoryPort.listarTodos();
    }

    @Override
    public List<Empaque> listarPorDetalleProduccion(Long idDetalleProduccion) {
        if (idDetalleProduccion == null) {
            throw new ReglaNegocioException("El id del detalle de producción es obligatorio");
        }
        return empaqueRepositoryPort.listarPorDetalleProduccion(idDetalleProduccion);
    }

    @Override
    public List<Empaque> listarPorProductoTerminado(Long idProductoTerminado) {
        if (idProductoTerminado == null) {
            throw new ReglaNegocioException("El id del producto terminado es obligatorio");
        }
        return empaqueRepositoryPort.listarPorProductoTerminado(idProductoTerminado);
    }

    @Override
    public List<Empaque> listarPorLoteEmpaque(String loteEmpaque) {
        String loteLimpio = limpiarObligatorio(loteEmpaque, LOTE_EMPAQUE_OBLIGATORIO_STRING);
        return empaqueRepositoryPort.listarPorLoteEmpaque(loteLimpio);
    }

    @Override
    public List<Empaque> listarPorRangoFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new ReglaNegocioException("Las fechas de inicio y fin son obligatorias");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new ReglaNegocioException("La fecha de inicio no puede ser mayor que la fecha fin");
        }

        return empaqueRepositoryPort.listarPorRangoFecha(fechaInicio, fechaFin);
    }

    private void validarDatosObligatorios(Empaque empaque) {
        if (empaque == null) {
            throw new ReglaNegocioException("El empaque es obligatorio");
        }

        if (empaque.getDetalleProduccion() == null
                || empaque.getDetalleProduccion().getIdDetalleProduccion() == null) {
            throw new ReglaNegocioException("El detalle de producción es obligatorio");
        }

        if (empaque.getProductoTerminado() == null
                || empaque.getProductoTerminado().getId() == null) {
            throw new ReglaNegocioException("El producto terminado es obligatorio");
        }

        if (empaque.getEstado() == null) {
            throw new ReglaNegocioException("El estado del empaque es obligatorio");
        }

        if (empaque.getCantidadUnidades() == null || empaque.getCantidadUnidades() <= 0) {
            throw new ReglaNegocioException("La cantidad de unidades debe ser mayor que cero");
        }

        if (empaque.getCantidadCajas() != null && empaque.getCantidadCajas() < 0) {
            throw new ReglaNegocioException("La cantidad de cajas no puede ser negativa");
        }

        if (empaque.getPesoTotalKg() != null && empaque.getPesoTotalKg().signum() < 0) {
            throw new ReglaNegocioException("El peso total no puede ser negativo");
        }
    }

    private void validarConsistenciaFechas(Empaque empaque) {
        if (empaque.getFechaEmpaque() != null
                && empaque.getFechaVencimiento() != null
                && empaque.getFechaVencimiento().isBefore(empaque.getFechaEmpaque().toLocalDate())) {
            throw new ReglaNegocioException(
                    "La fecha de vencimiento no puede ser menor que la fecha de empaque");
        }
    }

    private void validarDetalleConValidacionAprobada(Long idDetalleProduccion) {
        Optional<Validacion> validacionOpt = validacionRepositoryPort.buscarPorDetalleProduccion(idDetalleProduccion);

        if (validacionOpt.isEmpty()) {
            throw new ReglaNegocioException(
                    "No se puede registrar empaque porque el detalle de producción no tiene validación");
        }

        Validacion validacion = validacionOpt.get();
        if (validacion.getEstado() != EstadoValidacion.VALIDADO) {
            throw new ReglaNegocioException(
                    "No se puede registrar empaque porque el detalle de producción no está validado");
        }
    }

    private String limpiarObligatorio(String valor, String mensajeError) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ReglaNegocioException(mensajeError);
        }
        return valor.trim();
    }

    private String limpiarOpcional(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }
}
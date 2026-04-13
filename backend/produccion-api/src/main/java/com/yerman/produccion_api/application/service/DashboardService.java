package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.dto.response.DashboardProduccionPorSkuResponse;
import com.yerman.produccion_api.application.dto.response.DashboardProduccionVsEmpaqueResponse;
import com.yerman.produccion_api.application.dto.response.DashboardResumenResponse;
import com.yerman.produccion_api.application.dto.response.DashboardTrazabilidadDetalleResponse;
import com.yerman.produccion_api.application.dto.response.DashboardTrazabilidadEmpaqueResponse;
import com.yerman.produccion_api.application.dto.response.DashboardTrazabilidadLoteResponse;
import com.yerman.produccion_api.application.dto.response.DashboardValidacionPendienteResponse;
import com.yerman.produccion_api.application.dto.response.DashboardValidacionResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.domain.port.in.GestionDashboardUseCase;
import com.yerman.produccion_api.infrastructure.entity.DetalleProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.EmpaqueEntity;
import com.yerman.produccion_api.infrastructure.entity.ProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.entity.ValidacionEntity;
import com.yerman.produccion_api.infrastructure.repository.DetalleProduccionJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.EmpaqueJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ProduccionJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ProductoTerminadoJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ValidacionJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardProduccionPorSkuProjection;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardProduccionVsEmpaqueProjection;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardResumenDetalleProjection;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardResumenEmpaqueProjection;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardValidacionPendienteProjection;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService implements GestionDashboardUseCase {

    private final ProduccionJpaRepository produccionJpaRepository;
    private final DetalleProduccionJpaRepository detalleProduccionJpaRepository;
    private final ValidacionJpaRepository validacionJpaRepository;
    private final EmpaqueJpaRepository empaqueJpaRepository;
    private final ProductoTerminadoJpaRepository productoTerminadoJpaRepository;

    public DashboardService(
            ProduccionJpaRepository produccionJpaRepository,
            DetalleProduccionJpaRepository detalleProduccionJpaRepository,
            ValidacionJpaRepository validacionJpaRepository,
            EmpaqueJpaRepository empaqueJpaRepository,
            ProductoTerminadoJpaRepository productoTerminadoJpaRepository) {
        this.produccionJpaRepository = produccionJpaRepository;
        this.detalleProduccionJpaRepository = detalleProduccionJpaRepository;
        this.validacionJpaRepository = validacionJpaRepository;
        this.empaqueJpaRepository = empaqueJpaRepository;
        this.productoTerminadoJpaRepository = productoTerminadoJpaRepository;
    }

    @Override
    public DashboardResumenResponse obtenerResumenGeneral() {
        DashboardResumenResponse response = new DashboardResumenResponse();

        response.setTotalProducciones(produccionJpaRepository.count());
        response.setTotalDetallesProduccion(detalleProduccionJpaRepository.count());
        response.setTotalValidaciones(validacionJpaRepository.count());
        response.setTotalEmpaques(empaqueJpaRepository.count());
        response.setTotalProductosTerminados(productoTerminadoJpaRepository.count());

        DashboardResumenDetalleProjection detalleResumen = detalleProduccionJpaRepository.obtenerResumenDetalle();
        DashboardResumenEmpaqueProjection empaqueResumen = empaqueJpaRepository.obtenerResumenEmpaque();

        response.setTotalKgProgramados(valorBigDecimal(detalleResumen.getTotalKgProgramados()));
        response.setTotalKgBatch(valorBigDecimal(detalleResumen.getTotalKgBatch()));
        response.setTotalUnidadesReales(valorLong(detalleResumen.getTotalUnidadesReales()));
        response.setTotalUnidadesEmpacadas(valorLong(empaqueResumen.getTotalUnidadesEmpacadas()));
        response.setTotalCajasEmpacadas(valorLong(empaqueResumen.getTotalCajasEmpacadas()));
        response.setTotalPesoEmpacadoKg(valorBigDecimal(empaqueResumen.getTotalPesoEmpacadoKg()));

        return response;
    }

    @Override
    public List<DashboardProduccionPorSkuResponse> obtenerProduccionPorSku() {
        List<DashboardProduccionPorSkuProjection> rows = empaqueJpaRepository.obtenerProduccionPorSkuOptimizado();
        List<DashboardProduccionPorSkuResponse> response = new ArrayList<>();

        for (DashboardProduccionPorSkuProjection row : rows) {
            DashboardProduccionPorSkuResponse item = new DashboardProduccionPorSkuResponse();
            item.setIdProductoTerminado(row.getIdProductoTerminado());
            item.setSku(row.getSku());
            item.setNombreComercial(row.getNombreComercial());
            item.setReferencia(row.getReferencia());
            item.setTotalUnidades(valorLong(row.getTotalUnidades()));
            item.setTotalCajas(valorLong(row.getTotalCajas()));
            item.setTotalPesoKg(valorBigDecimal(row.getTotalPesoKg()));
            item.setTotalRegistrosEmpaque(valorLong(row.getTotalRegistrosEmpaque()));
            response.add(item);
        }

        return response;
    }

    @Override
    public List<DashboardProduccionVsEmpaqueResponse> obtenerProduccionVsEmpaque() {
        List<DashboardProduccionVsEmpaqueProjection> rows = empaqueJpaRepository.obtenerProduccionVsEmpaqueOptimizado();

        List<DashboardProduccionVsEmpaqueResponse> response = new ArrayList<>();

        for (DashboardProduccionVsEmpaqueProjection row : rows) {
            DashboardProduccionVsEmpaqueResponse item = new DashboardProduccionVsEmpaqueResponse();
            item.setIdDetalleProduccion(row.getIdDetalleProduccion());
            item.setIdProduccion(row.getIdProduccion());
            item.setNumeroLoteProduccion(row.getNumeroLoteProduccion());
            item.setIdProducto(row.getIdProducto());
            item.setNombreProducto(row.getNombreProducto());
            item.setNumBatch(row.getNumBatch());
            item.setKgProgramados(valorBigDecimal(row.getKgProgramados()));
            item.setKgBatch(valorBigDecimal(row.getKgBatch()));
            item.setUnidadesReales(valorIntegerToLong(row.getUnidadesReales()));
            item.setUnidadesEmpacadas(valorLong(row.getUnidadesEmpacadas()));
            item.setCajasEmpacadas(valorLong(row.getCajasEmpacadas()));
            item.setPesoEmpacadoKg(valorBigDecimal(row.getPesoEmpacadoKg()));
            item.setUnidadesPendientesPorEmpacar(
                    calcularPendientes(item.getUnidadesReales(), item.getUnidadesEmpacadas()));
            response.add(item);
        }

        return response;
    }

    @Override
    public DashboardTrazabilidadLoteResponse obtenerTrazabilidadPorLote(String lote) {
        ProduccionEntity produccion = produccionJpaRepository.findByNumeroLote(lote)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró una producción con el lote: " + lote));

        DashboardTrazabilidadLoteResponse response = new DashboardTrazabilidadLoteResponse();
        response.setIdProduccion(produccion.getIdProduccion());
        response.setNumeroLote(produccion.getNumeroLote());
        response.setFechaProduccion(produccion.getFechaProduccion());
        response.setFechaVencimiento(produccion.getFechaVencimiento());
        response.setEstadoProduccion(produccion.getEstado());
        response.setTipoTurno(produccion.getTipoTurno());
        response.setLineaProduccion(
                produccion.getLineaProduccion() != null ? produccion.getLineaProduccion().getNombre() : null);

        List<DetalleProduccionEntity> detalles = detalleProduccionJpaRepository
                .findByProduccion_IdProduccion(produccion.getIdProduccion());

        List<DashboardTrazabilidadDetalleResponse> detallesResponse = new ArrayList<>();

        for (DetalleProduccionEntity detalle : detalles) {
            detallesResponse.add(construirDetalleTrazabilidad(detalle));
        }

        response.setDetalles(detallesResponse);
        return response;
    }

    @Override
    public List<DashboardValidacionResponse> obtenerValidaciones() {
        List<ValidacionEntity> validaciones = validacionJpaRepository.findAll();
        List<DashboardValidacionResponse> response = new ArrayList<>();

        for (ValidacionEntity validacion : validaciones) {
            DashboardValidacionResponse item = new DashboardValidacionResponse();

            item.setIdValidacion(validacion.getIdValidacion());
            item.setEstadoValidacion(validacion.getEstado() != null ? validacion.getEstado().name() : null);
            item.setObservacion(validacion.getObservacion());
            item.setFechaValidacion(validacion.getFechaValidacion());

            if (validacion.getDetalleProduccion() != null) {
                item.setIdDetalleProduccion(validacion.getDetalleProduccion().getIdDetalleProduccion());

                ProduccionEntity produccion = validacion.getDetalleProduccion().getProduccion();
                if (produccion != null) {
                    item.setIdProduccion(produccion.getIdProduccion());
                    item.setNumeroLote(produccion.getNumeroLote());
                }
            }

            if (validacion.getValidador() != null) {
                UsuarioEntity validador = validacion.getValidador();
                item.setIdValidador(validador.getIdUsuario());
                item.setNombreValidador(construirNombreCompleto(validador));
            }

            response.add(item);
        }

        return response;
    }

    @Override
    public List<DashboardValidacionPendienteResponse> obtenerValidacionesPendientes() {
        List<DashboardValidacionPendienteProjection> rows = detalleProduccionJpaRepository
                .obtenerValidacionesPendientesOptimizado();

        List<DashboardValidacionPendienteResponse> response = new ArrayList<>();

        for (DashboardValidacionPendienteProjection row : rows) {
            DashboardValidacionPendienteResponse item = new DashboardValidacionPendienteResponse();
            item.setIdDetalleProduccion(row.getIdDetalleProduccion());
            item.setIdProduccion(row.getIdProduccion());
            item.setNumeroLote(row.getNumeroLote());
            item.setFechaProduccion(row.getFechaProduccion());
            item.setEstadoProduccion(row.getEstadoProduccion());
            item.setIdProducto(row.getIdProducto());
            item.setNombreProducto(row.getNombreProducto());
            item.setNumBatch(row.getNumBatch());
            item.setKgProgramados(valorBigDecimal(row.getKgProgramados()));
            item.setKgBatch(valorBigDecimal(row.getKgBatch()));
            item.setUnidadesReales(row.getUnidadesReales());
            item.setRendimientoPct(valorBigDecimal(row.getRendimientoPct()));
            response.add(item);
        }

        return response;
    }

    private String construirNombreCompleto(UsuarioEntity usuario) {
        List<String> partes = new ArrayList<>();

        if (usuario.getPrimerNombre() != null && !usuario.getPrimerNombre().isBlank()) {
            partes.add(usuario.getPrimerNombre().trim());
        }
        if (usuario.getSegundoNombre() != null && !usuario.getSegundoNombre().isBlank()) {
            partes.add(usuario.getSegundoNombre().trim());
        }
        if (usuario.getPrimerApellido() != null && !usuario.getPrimerApellido().isBlank()) {
            partes.add(usuario.getPrimerApellido().trim());
        }
        if (usuario.getSegundoApellido() != null && !usuario.getSegundoApellido().isBlank()) {
            partes.add(usuario.getSegundoApellido().trim());
        }

        return String.join(" ", partes);
    }

    private DashboardTrazabilidadDetalleResponse construirDetalleTrazabilidad(DetalleProduccionEntity detalle) {
        DashboardTrazabilidadDetalleResponse item = new DashboardTrazabilidadDetalleResponse();

        item.setIdDetalleProduccion(detalle.getIdDetalleProduccion());

        if (detalle.getProducto() != null) {
            item.setIdProducto(detalle.getProducto().getIdProducto());
            item.setNombreProducto(detalle.getProducto().getNombre());
        }

        item.setNumBatch(detalle.getNumBatch());
        item.setKgProgramados(valorBigDecimal(detalle.getKgProgramados()));
        item.setKgBatch(valorBigDecimal(detalle.getKgBatch()));
        item.setUnidadesReales(detalle.getUnidadesReales());
        item.setRendimientoPct(valorBigDecimal(detalle.getRendimientoPct()));

        completarDatosValidacion(item, detalle.getIdDetalleProduccion());
        item.setEmpaques(construirEmpaquesTrazabilidad(detalle.getIdDetalleProduccion()));

        return item;
    }

    private void completarDatosValidacion(
            DashboardTrazabilidadDetalleResponse item,
            Long idDetalleProduccion) {

        ValidacionEntity validacion = validacionJpaRepository
                .findByDetalleProduccion_IdDetalleProduccion(idDetalleProduccion)
                .orElse(null);

        if (validacion == null) {
            return;
        }

        item.setEstadoValidacion(validacion.getEstado() != null ? validacion.getEstado().name() : null);
        item.setFechaValidacion(validacion.getFechaValidacion());
        item.setObservacionValidacion(validacion.getObservacion());
    }

    private List<DashboardTrazabilidadEmpaqueResponse> construirEmpaquesTrazabilidad(Long idDetalleProduccion) {
        List<EmpaqueEntity> empaques = empaqueJpaRepository
                .findByDetalleProduccion_IdDetalleProduccion(idDetalleProduccion);

        List<DashboardTrazabilidadEmpaqueResponse> response = new ArrayList<>();

        for (EmpaqueEntity empaque : empaques) {
            DashboardTrazabilidadEmpaqueResponse item = new DashboardTrazabilidadEmpaqueResponse();
            item.setIdEmpaque(empaque.getId());
            item.setLoteEmpaque(empaque.getLoteEmpaque());
            item.setFechaEmpaque(empaque.getFechaEmpaque());
            item.setFechaVencimiento(empaque.getFechaVencimiento());
            item.setEstadoEmpaque(empaque.getEstado() != null ? empaque.getEstado().name() : null);
            item.setCantidadUnidades(empaque.getCantidadUnidades());
            item.setCantidadCajas(empaque.getCantidadCajas());

            if (empaque.getProductoTerminado() != null) {
                item.setIdProductoTerminado(empaque.getProductoTerminado().getId());
                item.setSku(empaque.getProductoTerminado().getSku());
                item.setNombreComercial(empaque.getProductoTerminado().getNombreComercial());
            }

            response.add(item);
        }

        return response;
    }

    private long calcularPendientes(long unidadesReales, long unidadesEmpacadas) {
        return Math.max(unidadesReales - unidadesEmpacadas, 0L);
    }

    private BigDecimal valorBigDecimal(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }

    private long valorLong(Long valor) {
        return valor != null ? valor : 0L;
    }

    private long valorIntegerToLong(Integer valor) {
        return valor != null ? valor.longValue() : 0L;
    }
}
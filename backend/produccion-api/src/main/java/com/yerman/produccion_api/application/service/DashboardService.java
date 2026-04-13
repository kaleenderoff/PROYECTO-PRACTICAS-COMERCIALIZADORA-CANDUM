package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.dto.response.DashboardProduccionPorSkuResponse;
import com.yerman.produccion_api.application.dto.response.DashboardProduccionVsEmpaqueResponse;
import com.yerman.produccion_api.application.dto.response.DashboardResumenResponse;
import com.yerman.produccion_api.domain.port.in.GestionDashboardUseCase;
import com.yerman.produccion_api.infrastructure.entity.DetalleProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.EmpaqueEntity;
import com.yerman.produccion_api.infrastructure.entity.ProductoEntity;
import com.yerman.produccion_api.infrastructure.entity.ProductoTerminadoEntity;
import com.yerman.produccion_api.infrastructure.entity.ProduccionEntity;
import com.yerman.produccion_api.infrastructure.repository.DetalleProduccionJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.EmpaqueJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ProduccionJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ProductoTerminadoJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ValidacionJpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        List<DetalleProduccionEntity> detalles = detalleProduccionJpaRepository.findAll();
        List<EmpaqueEntity> empaques = empaqueJpaRepository.findAll();

        response.setTotalKgProgramados(sumarKgProgramados(detalles));
        response.setTotalKgBatch(sumarKgBatch(detalles));
        response.setTotalUnidadesReales(sumarUnidadesReales(detalles));
        response.setTotalUnidadesEmpacadas(sumarUnidadesEmpacadas(empaques));
        response.setTotalCajasEmpacadas(sumarCajasEmpacadas(empaques));
        response.setTotalPesoEmpacadoKg(sumarPesoEmpacado(empaques));

        return response;
    }

    @Override
    public List<DashboardProduccionPorSkuResponse> obtenerProduccionPorSku() {
        List<EmpaqueEntity> empaques = empaqueJpaRepository.findAll();
        Map<Long, DashboardProduccionPorSkuResponse> acumuladoPorSku = new LinkedHashMap<>();

        for (EmpaqueEntity empaque : empaques) {
            ProductoTerminadoEntity productoTerminado = empaque.getProductoTerminado();

            if (!esProductoTerminadoValido(productoTerminado)) {
                continue;
            }

            Long idProductoTerminado = productoTerminado.getId();

            DashboardProduccionPorSkuResponse item = acumuladoPorSku.computeIfAbsent(
                    idProductoTerminado,
                    id -> crearItemProduccionPorSku(productoTerminado));

            acumularEmpaqueEnSku(item, empaque);
        }

        return new ArrayList<>(acumuladoPorSku.values());
    }

    @Override
    public List<DashboardProduccionVsEmpaqueResponse> obtenerProduccionVsEmpaque() {
        List<DetalleProduccionEntity> detalles = detalleProduccionJpaRepository.findAll();
        List<DashboardProduccionVsEmpaqueResponse> response = new ArrayList<>();

        for (DetalleProduccionEntity detalle : detalles) {
            response.add(construirProduccionVsEmpaque(detalle));
        }

        return response;
    }

    private boolean esProductoTerminadoValido(ProductoTerminadoEntity productoTerminado) {
        return productoTerminado != null && productoTerminado.getId() != null;
    }

    private DashboardProduccionPorSkuResponse crearItemProduccionPorSku(ProductoTerminadoEntity productoTerminado) {
        DashboardProduccionPorSkuResponse item = new DashboardProduccionPorSkuResponse();
        item.setIdProductoTerminado(productoTerminado.getId());
        item.setSku(productoTerminado.getSku());
        item.setNombreComercial(productoTerminado.getNombreComercial());
        item.setReferencia(productoTerminado.getReferencia());
        item.setTotalUnidades(0L);
        item.setTotalCajas(0L);
        item.setTotalPesoKg(BigDecimal.ZERO);
        item.setTotalRegistrosEmpaque(0L);
        return item;
    }

    private void acumularEmpaqueEnSku(DashboardProduccionPorSkuResponse item, EmpaqueEntity empaque) {
        item.setTotalUnidades(item.getTotalUnidades() + valorLong(empaque.getCantidadUnidades()));
        item.setTotalCajas(item.getTotalCajas() + valorLong(empaque.getCantidadCajas()));
        item.setTotalPesoKg(item.getTotalPesoKg().add(valorBigDecimal(empaque.getPesoTotalKg())));
        item.setTotalRegistrosEmpaque(item.getTotalRegistrosEmpaque() + 1);
    }

    private DashboardProduccionVsEmpaqueResponse construirProduccionVsEmpaque(DetalleProduccionEntity detalle) {
        DashboardProduccionVsEmpaqueResponse item = new DashboardProduccionVsEmpaqueResponse();

        item.setIdDetalleProduccion(detalle.getIdDetalleProduccion());
        completarDatosProduccion(item, detalle.getProduccion());
        completarDatosProducto(item, detalle.getProducto());
        completarDatosDetalle(item, detalle);

        List<EmpaqueEntity> empaquesDelDetalle = empaqueJpaRepository
                .findByDetalleProduccion_IdDetalleProduccion(detalle.getIdDetalleProduccion());

        long unidadesEmpacadas = sumarUnidadesEmpacadas(empaquesDelDetalle);
        long cajasEmpacadas = sumarCajasEmpacadas(empaquesDelDetalle);
        BigDecimal pesoEmpacadoKg = sumarPesoEmpacado(empaquesDelDetalle);

        item.setUnidadesEmpacadas(unidadesEmpacadas);
        item.setCajasEmpacadas(cajasEmpacadas);
        item.setPesoEmpacadoKg(pesoEmpacadoKg);
        item.setUnidadesPendientesPorEmpacar(calcularPendientes(item.getUnidadesReales(), unidadesEmpacadas));

        return item;
    }

    private void completarDatosProduccion(
            DashboardProduccionVsEmpaqueResponse item,
            ProduccionEntity produccion) {

        if (produccion == null) {
            return;
        }

        item.setIdProduccion(produccion.getIdProduccion());
        item.setNumeroLoteProduccion(produccion.getNumeroLote());
    }

    private void completarDatosProducto(
            DashboardProduccionVsEmpaqueResponse item,
            ProductoEntity producto) {

        if (producto == null) {
            return;
        }

        item.setIdProducto(producto.getIdProducto());
        item.setNombreProducto(producto.getNombre());
    }

    private void completarDatosDetalle(
            DashboardProduccionVsEmpaqueResponse item,
            DetalleProduccionEntity detalle) {

        item.setNumBatch(detalle.getNumBatch());
        item.setKgProgramados(valorBigDecimal(detalle.getKgProgramados()));
        item.setKgBatch(valorBigDecimal(detalle.getKgBatch()));
        item.setUnidadesReales(valorLong(detalle.getUnidadesReales()));
    }

    private BigDecimal sumarKgProgramados(List<DetalleProduccionEntity> detalles) {
        BigDecimal total = BigDecimal.ZERO;

        for (DetalleProduccionEntity detalle : detalles) {
            total = total.add(valorBigDecimal(detalle.getKgProgramados()));
        }

        return total;
    }

    private BigDecimal sumarKgBatch(List<DetalleProduccionEntity> detalles) {
        BigDecimal total = BigDecimal.ZERO;

        for (DetalleProduccionEntity detalle : detalles) {
            total = total.add(valorBigDecimal(detalle.getKgBatch()));
        }

        return total;
    }

    private long sumarUnidadesReales(List<DetalleProduccionEntity> detalles) {
        long total = 0L;

        for (DetalleProduccionEntity detalle : detalles) {
            total += valorLong(detalle.getUnidadesReales());
        }

        return total;
    }

    private long sumarUnidadesEmpacadas(List<EmpaqueEntity> empaques) {
        long total = 0L;

        for (EmpaqueEntity empaque : empaques) {
            total += valorLong(empaque.getCantidadUnidades());
        }

        return total;
    }

    private long sumarCajasEmpacadas(List<EmpaqueEntity> empaques) {
        long total = 0L;

        for (EmpaqueEntity empaque : empaques) {
            total += valorLong(empaque.getCantidadCajas());
        }

        return total;
    }

    private BigDecimal sumarPesoEmpacado(List<EmpaqueEntity> empaques) {
        BigDecimal total = BigDecimal.ZERO;

        for (EmpaqueEntity empaque : empaques) {
            total = total.add(valorBigDecimal(empaque.getPesoTotalKg()));
        }

        return total;
    }

    private long calcularPendientes(long unidadesReales, long unidadesEmpacadas) {
        return Math.max(unidadesReales - unidadesEmpacadas, 0L);
    }

    private BigDecimal valorBigDecimal(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }

    private long valorLong(Integer valor) {
        return valor != null ? valor.longValue() : 0L;
    }
}
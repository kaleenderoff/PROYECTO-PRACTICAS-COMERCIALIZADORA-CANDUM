package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.dto.response.DashboardProduccionPorSkuResponse;
import com.yerman.produccion_api.application.dto.response.DashboardResumenResponse;
import com.yerman.produccion_api.domain.port.in.GestionDashboardUseCase;
import com.yerman.produccion_api.infrastructure.entity.DetalleProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.EmpaqueEntity;
import com.yerman.produccion_api.infrastructure.entity.ProductoTerminadoEntity;
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

        long totalProducciones = produccionJpaRepository.count();
        long totalDetalles = detalleProduccionJpaRepository.count();
        long totalValidaciones = validacionJpaRepository.count();
        long totalEmpaques = empaqueJpaRepository.count();
        long totalProductosTerminados = productoTerminadoJpaRepository.count();

        List<DetalleProduccionEntity> detalles = detalleProduccionJpaRepository.findAll();
        List<EmpaqueEntity> empaques = empaqueJpaRepository.findAll();

        BigDecimal totalKgProgramados = BigDecimal.ZERO;
        BigDecimal totalKgBatch = BigDecimal.ZERO;
        long totalUnidadesReales = 0L;

        for (DetalleProduccionEntity detalle : detalles) {
            if (detalle.getKgProgramados() != null) {
                totalKgProgramados = totalKgProgramados.add(detalle.getKgProgramados());
            }

            if (detalle.getKgBatch() != null) {
                totalKgBatch = totalKgBatch.add(detalle.getKgBatch());
            }

            if (detalle.getUnidadesReales() != null) {
                totalUnidadesReales += detalle.getUnidadesReales();
            }
        }

        long totalUnidadesEmpacadas = 0L;
        long totalCajasEmpacadas = 0L;
        BigDecimal totalPesoEmpacadoKg = BigDecimal.ZERO;

        for (EmpaqueEntity empaque : empaques) {
            if (empaque.getCantidadUnidades() != null) {
                totalUnidadesEmpacadas += empaque.getCantidadUnidades();
            }

            if (empaque.getCantidadCajas() != null) {
                totalCajasEmpacadas += empaque.getCantidadCajas();
            }

            if (empaque.getPesoTotalKg() != null) {
                totalPesoEmpacadoKg = totalPesoEmpacadoKg.add(empaque.getPesoTotalKg());
            }
        }

        response.setTotalProducciones(totalProducciones);
        response.setTotalDetallesProduccion(totalDetalles);
        response.setTotalValidaciones(totalValidaciones);
        response.setTotalEmpaques(totalEmpaques);
        response.setTotalProductosTerminados(totalProductosTerminados);
        response.setTotalKgProgramados(totalKgProgramados);
        response.setTotalKgBatch(totalKgBatch);
        response.setTotalUnidadesReales(totalUnidadesReales);
        response.setTotalUnidadesEmpacadas(totalUnidadesEmpacadas);
        response.setTotalCajasEmpacadas(totalCajasEmpacadas);
        response.setTotalPesoEmpacadoKg(totalPesoEmpacadoKg);

        return response;
    }

    @Override
    public List<DashboardProduccionPorSkuResponse> obtenerProduccionPorSku() {
        List<EmpaqueEntity> empaques = empaqueJpaRepository.findAll();

        Map<Long, DashboardProduccionPorSkuResponse> acumuladoPorSku = new LinkedHashMap<>();

        for (EmpaqueEntity empaque : empaques) {
            ProductoTerminadoEntity productoTerminado = empaque.getProductoTerminado();

            if (productoTerminado == null || productoTerminado.getId() == null) {
                continue;
            }

            Long idProductoTerminado = productoTerminado.getId();

            DashboardProduccionPorSkuResponse item = acumuladoPorSku.get(idProductoTerminado);

            if (item == null) {
                item = new DashboardProduccionPorSkuResponse();
                item.setIdProductoTerminado(idProductoTerminado);
                item.setSku(productoTerminado.getSku());
                item.setNombreComercial(productoTerminado.getNombreComercial());
                item.setReferencia(productoTerminado.getReferencia());
                item.setTotalUnidades(0L);
                item.setTotalCajas(0L);
                item.setTotalPesoKg(BigDecimal.ZERO);
                item.setTotalRegistrosEmpaque(0L);

                acumuladoPorSku.put(idProductoTerminado, item);
            }

            if (empaque.getCantidadUnidades() != null) {
                item.setTotalUnidades(item.getTotalUnidades() + empaque.getCantidadUnidades());
            }

            if (empaque.getCantidadCajas() != null) {
                item.setTotalCajas(item.getTotalCajas() + empaque.getCantidadCajas());
            }

            if (empaque.getPesoTotalKg() != null) {
                item.setTotalPesoKg(item.getTotalPesoKg().add(empaque.getPesoTotalKg()));
            }

            item.setTotalRegistrosEmpaque(item.getTotalRegistrosEmpaque() + 1);
        }

        return new ArrayList<>(acumuladoPorSku.values());
    }
}
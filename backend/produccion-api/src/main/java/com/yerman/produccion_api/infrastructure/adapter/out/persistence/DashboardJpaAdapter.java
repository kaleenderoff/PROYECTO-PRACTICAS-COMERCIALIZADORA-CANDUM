package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.dto.response.*;
import com.yerman.produccion_api.domain.port.out.DashboardRepositoryPort;
import com.yerman.produccion_api.infrastructure.repository.DashboardJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardProduccionVsEmpaqueProjection;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardTrazabilidadLoteProjection;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class DashboardJpaAdapter implements DashboardRepositoryPort {

    private final DashboardJpaRepository repository;

    public DashboardJpaAdapter(DashboardJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DashboardProduccionVsEmpaqueResponse> obtenerProduccionVsEmpaque() {
        List<DashboardProduccionVsEmpaqueProjection> real = repository.obtenerProduccionVsEmpaque();
        return real.stream()
                .map(p -> {
                    BigDecimal prod = p.getTotalProducido() != null ? p.getTotalProducido() : BigDecimal.ZERO;
                    BigDecimal emp = p.getTotalEmpaquetado() != null ? p.getTotalEmpaquetado() : BigDecimal.ZERO;
                    return new DashboardProduccionVsEmpaqueResponse(
                        p.getFecha(),
                        prod,
                        emp,
                        prod.subtract(emp));
                })
                .toList();
    }

    @Override
    public DashboardTrazabilidadLoteResponse obtenerTrazabilidadPorLote(String lote) {
        DashboardTrazabilidadLoteProjection p = repository
                .obtenerTrazabilidadPorLote(lote)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        return new DashboardTrazabilidadLoteResponse(
                p.getLote(),
                p.getProducto(),
                p.getFechaProduccion(),
                p.getNumeroBatch(),
                p.getKilosProducidos(),
                p.getKilosDisponibles(),
                p.getKilosEmpacados(),
                p.getEstadoProductoTerminado());
    }

    @Override
    public List<DashboardProduccionSkuResponse> obtenerProduccionSkuMensual(int mes, int anio) {
        List<Object[]> resultados = repository.obtenerResumenSkuMensual(mes, anio);
        
        List<DashboardProduccionSkuResponse.SkuProduccionMensual> lcItems = new ArrayList<>();
        List<DashboardProduccionSkuResponse.SkuProduccionMensual> dlItems = new ArrayList<>();

        for (Object[] row : resultados) {
            String sku = (String) row[0];
            BigDecimal pesoNeto = row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO;
            Long unidades = ((Number) row[2]).longValue();
            BigDecimal kilos = (BigDecimal) row[3];

            DashboardProduccionSkuResponse.SkuProduccionMensual item = new DashboardProduccionSkuResponse.SkuProduccionMensual(
                    sku, pesoNeto, unidades.intValue(), kilos, new ArrayList<>()
            );

            if (sku.toUpperCase().contains("CONDENSADA") || sku.toUpperCase().contains("L.C")) {
                lcItems.add(item);
            } else {
                dlItems.add(item);
            }
        }

        List<DashboardProduccionSkuResponse> response = new ArrayList<>();
        if (!lcItems.isEmpty()) response.add(new DashboardProduccionSkuResponse("LC", lcItems));
        if (!dlItems.isEmpty()) response.add(new DashboardProduccionSkuResponse("DL", dlItems));
        
        return response;
    }

    @Override
    public List<RendimientoAnualResponse> obtenerRendimientoAnual(int anio) {
        List<Object[]> produccion = repository.obtenerProduccionMensualAnual(anio);
        List<Object[]> recepcion = repository.obtenerRecepcionMensualAnual(anio);
        
        List<RendimientoAnualResponse> response = new ArrayList<>();
        
        for (int m = 1; m <= 12; m++) {
            final int mes = m;
            BigDecimal ptDL = BigDecimal.ZERO;
            BigDecimal ptLC = BigDecimal.ZERO;
            BigDecimal leche = BigDecimal.ZERO;
            
            for (Object[] row : produccion) {
                if (((Number) row[0]).intValue() == mes) {
                    ptDL = row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO;
                    ptLC = row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO;
                    break;
                }
            }
            
            for (Object[] row : recepcion) {
                if (((Number) row[0]).intValue() == mes) {
                    leche = row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO;
                    break;
                }
            }
            
            BigDecimal rendDL = BigDecimal.ZERO;
            BigDecimal rendLC = BigDecimal.ZERO;
            
            if (leche.compareTo(BigDecimal.ZERO) > 0) {
                rendDL = ptDL.multiply(new BigDecimal("100")).divide(leche, 2, RoundingMode.HALF_UP);
                rendLC = ptLC.multiply(new BigDecimal("100")).divide(leche, 2, RoundingMode.HALF_UP);
            }
            
            response.add(new RendimientoAnualResponse(mes, ptDL, ptLC, leche, rendDL, rendLC));
        }
        
        return response;
    }

    @Override
    public DashboardGerencialResponse.ResumenMesGerencial obtenerResumenMensualGerencial(int mes, int anio) {
        List<Object[]> results = repository.obtenerResumenMensualGerencial(mes, anio);
        if (results.isEmpty()) return new DashboardGerencialResponse.ResumenMesGerencial(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

        Object[] row = results.get(0);
        BigDecimal lecheRecibida = row[0] != null ? new BigDecimal(row[0].toString()) : BigDecimal.ZERO;
        BigDecimal ptDulceLeche = row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO;
        BigDecimal ptLecheCondensada = row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO;

        BigDecimal cremaEsperada = BigDecimal.ZERO;
        if (lecheRecibida.compareTo(BigDecimal.ZERO) > 0) {
            cremaEsperada = lecheRecibida.divide(new BigDecimal("3000"), 4, RoundingMode.HALF_UP)
                                         .multiply(new BigDecimal("131"));
        }

        return new DashboardGerencialResponse.ResumenMesGerencial(
                lecheRecibida,
                BigDecimal.ZERO, 
                cremaEsperada,
                ptDulceLeche,
                ptLecheCondensada
        );
    }

    @Override
    public List<DashboardGerencialResponse.SemanaGerencialResponse> obtenerDetalleSemanalGerencial(int mes, int anio) {
        List<Object[]> results = repository.obtenerDetalleSemanalGerencial(mes, anio);
        List<DashboardGerencialResponse.SemanaGerencialResponse> semanal = new ArrayList<>();

        results.forEach(row -> {
            Integer semana = ((Number) row[0]).intValue();
            Long unidades = ((Number) row[1]).longValue();
            BigDecimal kilos = row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO;
            String tipo = (String) row[3];

            DashboardGerencialResponse.SemanaGerencialResponse s = semanal.stream()
                .filter(x -> x.numeroSemana().equals(semana))
                .findFirst()
                .orElseGet(() -> {
                    DashboardGerencialResponse.SemanaGerencialResponse newS = new DashboardGerencialResponse.SemanaGerencialResponse(
                        semana, "Semana " + semana, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 
                        BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 
                        BigDecimal.ZERO, BigDecimal.ZERO
                    );
                    semanal.add(newS);
                    return newS;
                });

            int idx = semanal.indexOf(s);
            if ("DL".equals(tipo)) {
                semanal.set(idx, new DashboardGerencialResponse.SemanaGerencialResponse(
                    s.numeroSemana(), s.rangoFechas(), s.lecheRecibida(), s.litrosCrema(), s.kgCrema(),
                    s.lecheProcesada(), kilos, s.kgReproceso(), new BigDecimal("46.5"), new BigDecimal("46.8"),
                    s.ptLecheCondensada(), s.rendLecheCondensada()
                ));
            } else if ("LC".equals(tipo)) {
                semanal.set(idx, new DashboardGerencialResponse.SemanaGerencialResponse(
                    s.numeroSemana(), s.rangoFechas(), s.lecheRecibida(), s.litrosCrema(), s.kgCrema(),
                    s.lecheProcesada(), s.ptDulceLeche(), s.kgReproceso(), s.rend1DulceLeche(), s.rend2DulceLeche(),
                    kilos, new BigDecimal("44.2")
                ));
            }
        });

        return semanal;
    }
}
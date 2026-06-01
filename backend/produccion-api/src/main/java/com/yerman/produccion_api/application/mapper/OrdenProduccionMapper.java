package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.response.OrdenProduccionResponse;
import com.yerman.produccion_api.domain.model.OrdenProduccion;
import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;

import java.math.BigDecimal;

public class OrdenProduccionMapper {

    private OrdenProduccionMapper() {
    }

    public static OrdenProduccion toDomain(OrdenProduccionEntity entity) {
        if (entity == null) {
            return null;
        }

        OrdenProduccion domain = new OrdenProduccion(
                entity.getId(),
                entity.getNumeroOrden(),
                entity.getProgramacion() != null ? entity.getProgramacion().getId() : null,
                entity.getLinea() != null ? entity.getLinea().getId() : null,
                entity.getProducto() != null ? entity.getProducto().getId() : null,
                entity.getTurno() != null ? entity.getTurno().getId() : null,
                entity.getJefeLineaEjecutor() != null ? entity.getJefeLineaEjecutor().getIdUsuario() : null,
                entity.getCreadaPor() != null ? entity.getCreadaPor().getIdUsuario() : null,
                entity.getFechaProduccion(),
                entity.getEstado(),
                entity.getObservaciones(),
                entity.getFechaInicioReal(),
                entity.getFechaFinReal(),
                entity.getTanqueLeche() != null ? entity.getTanqueLeche().getId() : null);

        domain.setKgEntradaReal(entity.getKgEntradaReal());
        domain.setKgProducidoBatches(entity.getKgProducidoBatches());
        domain.setKgPtReal(entity.getKgPtReal());
        domain.setRendimientoReal(entity.getRendimientoReal());
        domain.setMermaReal(entity.getMermaReal());
        domain.setMermaEmpaque(entity.getMermaEmpaque());

        domain.setTandasCerradas(Boolean.TRUE.equals(entity.getTandasCerradas()));
        domain.setFechaCierreTandas(entity.getFechaCierreTandas());
        domain.setIdUsuarioCierreTandas(
                entity.getUsuarioCierreTandas() != null ? entity.getUsuarioCierreTandas().getIdUsuario() : null);
        domain.setNombreUsuarioCierreTandas(
                entity.getUsuarioCierreTandas() != null ? nombreCompleto(entity.getUsuarioCierreTandas()) : null);
        domain.setObservacionesCierreTandas(entity.getObservacionesCierreTandas());

        domain.setNombreLinea(entity.getLinea() != null ? entity.getLinea().getNombre() : null);
        domain.setNombreProducto(entity.getProducto() != null ? entity.getProducto().getNombre() : null);
        domain.setNombreTurno(entity.getTurno() != null ? entity.getTurno().getNombre() : null);
        domain.setNombreCreadaPor(entity.getCreadaPor() != null ? nombreCompleto(entity.getCreadaPor()) : null);
        domain.setNombreJefeLineaEjecutor(
                entity.getJefeLineaEjecutor() != null ? nombreCompleto(entity.getJefeLineaEjecutor()) : null);
        domain.setNombreTanqueLeche(entity.getTanqueLeche() != null ? entity.getTanqueLeche().getNombre() : null);

        if (entity.getProgramacion() != null) {
            var prog = entity.getProgramacion();
            domain.setNumBachesPlan(prog.getNumBachesPlan());
            domain.setKgBachePlan(prog.getKgBachePlan());

            if (prog.getFormulaVersion() != null) {
                var formula = prog.getFormulaVersion().getFormula();
                domain.setNombreFormula(formula != null ? formula.getNombre() : null);
                domain.setVersionFormula(prog.getFormulaVersion().getVersion());
            }

            if (entity.getDetalles() != null && !entity.getDetalles().isEmpty()) {
                domain.setSkus(entity.getDetalles().stream()
                        .map(ProgramacionSkuMapper::toDomain)
                        .toList());
            } else if (prog.getSkus() != null) {
                domain.setSkus(prog.getSkus().stream()
                        .map(ProgramacionSkuMapper::toDomain)
                        .toList());
            }

            if (domain.getSkus() != null) {
                BigDecimal kgTotalPT = domain.getSkus().stream()
                        .map(sku -> sku.getKgProductoTerminado() != null
                                ? sku.getKgProductoTerminado()
                                : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal kgEntradaTotal = domain.getSkus().stream()
                        .map(sku -> sku.getKgBatchCalculado() != null
                                ? sku.getKgBatchCalculado()
                                : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                domain.setKgPTTotalPlan(kgTotalPT);
                domain.setKgEntradaTotalPlan(kgEntradaTotal);
            }
        }

        if (com.yerman.produccion_api.domain.model.EstadoOrdenProduccion.FINALIZADA.equals(domain.getEstado())) {

            if (domain.getSkus() != null) {
                BigDecimal totalSkus = domain.getSkus().stream()
                        .map(s -> s.getCantidadReal() != null ? s.getCantidadReal() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (totalSkus.compareTo(BigDecimal.ZERO) > 0) {
                    if (domain.getKgProducidoBatches() == null
                            || domain.getKgProducidoBatches().compareTo(BigDecimal.ZERO) == 0) {
                        domain.setKgProducidoBatches(domain.getKgPtReal());
                    }
                    domain.setKgPtReal(totalSkus);
                }
            }

            if (domain.getKgProducidoBatches() != null && domain.getKgPtReal() != null) {
                domain.setMermaEmpaque(domain.getKgProducidoBatches().subtract(domain.getKgPtReal()));
            }

            if (domain.getKgEntradaReal() != null && domain.getKgProducidoBatches() != null) {
                domain.setMermaReal(domain.getKgEntradaReal().subtract(domain.getKgProducidoBatches()));
            }
        }

        return domain;
    }

    public static OrdenProduccionResponse toResponse(OrdenProduccionEntity entity) {
        if (entity == null) {
            return null;
        }

        OrdenProduccion domain = toDomain(entity);
        return OrdenProduccionRestMapper.toResponse(domain);
    }

    private static String nombreCompleto(UsuarioEntity usuario) {
        if (usuario == null) {
            return null;
        }

        return String.join(" ",
                usuario.getPrimerNombre() != null ? usuario.getPrimerNombre() : "",
                usuario.getSegundoNombre() != null ? usuario.getSegundoNombre() : "",
                usuario.getPrimerApellido() != null ? usuario.getPrimerApellido() : "",
                usuario.getSegundoApellido() != null ? usuario.getSegundoApellido() : "")
                .trim();
    }
}
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

        // Mapear Métricas Reales
        domain.setKgEntradaReal(entity.getKgEntradaReal());
        domain.setKgProducidoBatches(entity.getKgProducidoBatches());
        domain.setKgPtReal(entity.getKgPtReal());
        domain.setRendimientoReal(entity.getRendimientoReal());
        domain.setMermaReal(entity.getMermaReal());
        domain.setMermaEmpaque(entity.getMermaEmpaque());

        // Enriquecer con nombres descriptivos
        domain.setNombreLinea(entity.getLinea() != null ? entity.getLinea().getNombre() : null);
        domain.setNombreProducto(entity.getProducto() != null ? entity.getProducto().getNombre() : null);
        domain.setNombreTurno(entity.getTurno() != null ? entity.getTurno().getNombre() : null);
        domain.setNombreCreadaPor(entity.getCreadaPor() != null ? nombreCompleto(entity.getCreadaPor()) : null);
        domain.setNombreJefeLineaEjecutor(entity.getJefeLineaEjecutor() != null ? nombreCompleto(entity.getJefeLineaEjecutor()) : null);
        domain.setNombreTanqueLeche(entity.getTanqueLeche() != null ? entity.getTanqueLeche().getNombre() : null);

        // Enriquecer con Resumen Operativo desde Programación
        if (entity.getProgramacion() != null) {
            var prog = entity.getProgramacion();
            domain.setNumBachesPlan(prog.getNumBachesPlan());
            domain.setKgBachePlan(prog.getKgBachePlan());
            
            if (prog.getFormulaVersion() != null) {
                var formula = prog.getFormulaVersion().getFormula();
                domain.setNombreFormula(formula != null ? formula.getNombre() : null);
                domain.setVersionFormula(prog.getFormulaVersion().getVersion());
            }

            // Mapear SKUs desde los detalles de la orden (tienen los valores reales)
            if (entity.getDetalles() != null && !entity.getDetalles().isEmpty()) {
                domain.setSkus(entity.getDetalles().stream()
                        .map(ProgramacionSkuMapper::toDomain)
                        .toList());
            } else if (prog.getSkus() != null) {
                domain.setSkus(prog.getSkus().stream()
                        .map(ProgramacionSkuMapper::toDomain)
                        .toList());
            }

            // Cálculos del Plan Operativo
            if (domain.getSkus() != null) {
                java.math.BigDecimal kgTotalPT = domain.getSkus().stream()
                        .map(sku -> sku.getKgProductoTerminado() != null ? sku.getKgProductoTerminado() : java.math.BigDecimal.ZERO)
                        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
                
                java.math.BigDecimal kgEntradaTotal = domain.getSkus().stream()
                        .map(sku -> sku.getKgBatchCalculado() != null ? sku.getKgBatchCalculado() : java.math.BigDecimal.ZERO)
                        .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

                domain.setKgPTTotalPlan(kgTotalPT);
                domain.setKgEntradaTotalPlan(kgEntradaTotal);
            }
        }

        // --- REPARACIÓN DE DATOS PARA ÓRDENES HISTÓRICAS ---
        // Si la orden está finalizada pero faltan métricas (debido a actualizaciones de versión),
        // recalculamos para que el usuario no vea datos vacíos en el detalle.
        if (com.yerman.produccion_api.domain.model.EstadoOrdenProduccion.FINALIZADA.equals(domain.getEstado())) {
            
            // 1. Recalcular Producto Terminado desde SKUs reales
            if (domain.getSkus() != null) {
                BigDecimal totalSkus = domain.getSkus().stream()
                        .map(s -> s.getCantidadReal() != null ? s.getCantidadReal() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                
                // Si el total de SKUs es diferente a lo guardado, priorizamos el detalle real
                if (totalSkus.compareTo(BigDecimal.ZERO) > 0) {
                    // Si el valor guardado en kgPtReal era el de los baches (error versión anterior),
                    // lo movemos a kgProducidoBatches antes de sobreescribirlo.
                    if (domain.getKgProducidoBatches() == null || domain.getKgProducidoBatches().compareTo(BigDecimal.ZERO) == 0) {
                        domain.setKgProducidoBatches(domain.getKgPtReal());
                    }
                    domain.setKgPtReal(totalSkus);
                }
            }
            
            // 2. Recalcular Merma de Empaque si es necesario
            if (domain.getKgProducidoBatches() != null && domain.getKgPtReal() != null) {
                domain.setMermaEmpaque(domain.getKgProducidoBatches().subtract(domain.getKgPtReal()));
            }
            
            // 3. Recalcular Merma de Proceso si es necesario
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

        // Usar toDomain para aprovechar toda la lógica de mapeo y cálculos
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
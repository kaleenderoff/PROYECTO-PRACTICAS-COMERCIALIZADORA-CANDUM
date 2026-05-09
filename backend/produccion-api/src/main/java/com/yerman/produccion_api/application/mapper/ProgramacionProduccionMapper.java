package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.response.ProgramacionProduccionResponse;
import com.yerman.produccion_api.application.dto.response.ProgramacionSkuResponse;
import com.yerman.produccion_api.domain.model.ProgramacionProduccion;
import com.yerman.produccion_api.infrastructure.entity.*;

import java.math.BigDecimal;
import java.util.List;

public class ProgramacionProduccionMapper {

    private ProgramacionProduccionMapper() {
    }

    public static ProgramacionProduccion toDomain(ProgramacionProduccionEntity entity) {
        if (entity == null) {
            return null;
        }

        return new ProgramacionProduccion(
                entity.getId(),
                entity.getCodigoProgramacion(),
                entity.getFechaProduccion(),
                entity.getLinea() != null ? entity.getLinea().getId() : null,
                entity.getProducto() != null ? entity.getProducto().getId() : null,
                entity.getTurno() != null ? entity.getTurno().getId() : null,
                entity.getNumBachesPlan(),
                entity.getKgBachePlan(),
                entity.getFormulaVersion() != null ? entity.getFormulaVersion().getId() : null,
                entity.getJefeProduccion() != null ? entity.getJefeProduccion().getIdUsuario() : null,
                entity.getEstado(),
                entity.getObservaciones());
    }

    public static ProgramacionProduccionEntity toEntity(
            ProgramacionProduccion domain,
            CatalogoLineaEntity linea,
            CatalogoProductoEntity producto,
            TurnoEntity turno,
            FormulaVersionEntity formulaVersion,
            UsuarioEntity jefeProduccion) {
        if (domain == null) {
            return null;
        }

        ProgramacionProduccionEntity entity = new ProgramacionProduccionEntity();
        entity.setCodigoProgramacion(domain.getCodigoProgramacion());
        entity.setFechaProduccion(domain.getFechaProduccion());
        entity.setLinea(linea);
        entity.setProducto(producto);
        entity.setTurno(turno);
        entity.setNumBachesPlan(domain.getNumBachesPlan());
        entity.setKgBachePlan(domain.getKgBachePlan());
        entity.setFormulaVersion(formulaVersion);
        entity.setJefeProduccion(jefeProduccion);
        entity.setEstado(domain.getEstado());
        entity.setObservaciones(domain.getObservaciones());
        return entity;
    }

    public static ProgramacionProduccionResponse toResponse(ProgramacionProduccion domain) {
        if (domain == null) {
            return null;
        }

        return new ProgramacionProduccionResponse(
                domain.getId(),
                domain.getCodigoProgramacion(),
                domain.getFechaProduccion(),
                domain.getIdLinea(),
                null,
                domain.getIdProducto(),
                null,
                domain.getIdTurno(),
                null,
                domain.getNumBachesPlan(),
                domain.getKgBachePlan(),
                domain.getIdFormulaVersion(),
                null,
                null,
                null,
                null,
                domain.getIdJefeProduccion(),
                null,
                domain.getEstado() != null ? domain.getEstado().name() : null,
                domain.getObservaciones(),
                List.of(),
                null,
                null,
                null);
    }

    public static ProgramacionProduccionResponse toResponse(
            ProgramacionProduccionEntity entity,
            List<ProgramacionSkuEntity> skuEntities) {
        if (entity == null) {
            return null;
        }

        List<ProgramacionSkuResponse> skus = skuEntities == null
                ? List.of()
                : skuEntities.stream()
                        .map(ProgramacionSkuMapper::toResponse)
                        .toList();

        BigDecimal totalKgProductoTerminado = sumar(
                skus.stream().map(ProgramacionSkuResponse::getKgProductoTerminado).toList());

        BigDecimal totalKgBatchCalculado = sumar(
                skus.stream().map(ProgramacionSkuResponse::getKgBatchCalculado).toList());

        BigDecimal totalNumBachesCalculado = sumar(
                skus.stream().map(ProgramacionSkuResponse::getNumBachesCalculado).toList());

        FormulaVersionEntity formulaVersion = entity.getFormulaVersion();
        FormulaEntity formula = formulaVersion != null ? formulaVersion.getFormula() : null;

        return new ProgramacionProduccionResponse(
                entity.getId(),
                entity.getCodigoProgramacion(),
                entity.getFechaProduccion(),
                entity.getLinea() != null ? entity.getLinea().getId() : null,
                entity.getLinea() != null ? entity.getLinea().getNombre() : null,
                entity.getProducto() != null ? entity.getProducto().getId() : null,
                entity.getProducto() != null ? entity.getProducto().getNombre() : null,
                entity.getTurno() != null ? entity.getTurno().getId() : null,
                entity.getTurno() != null ? entity.getTurno().getNombre() : null,
                entity.getNumBachesPlan(),
                entity.getKgBachePlan(),
                formulaVersion != null ? formulaVersion.getId() : null,
                formula != null ? formula.getNombre() : null,
                formulaVersion != null ? formulaVersion.getVersion() : null,
                formulaVersion != null ? formulaVersion.getRendimientoTeoricoPct() : null,
                formulaVersion != null ? formulaVersion.getKgBatchTotal() : null,
                entity.getJefeProduccion() != null ? entity.getJefeProduccion().getIdUsuario() : null,
                entity.getJefeProduccion() != null ? nombreUsuario(entity.getJefeProduccion()) : null,
                entity.getEstado() != null ? entity.getEstado().name() : null,
                entity.getObservaciones(),
                skus,
                totalKgProductoTerminado,
                totalKgBatchCalculado,
                totalNumBachesCalculado);
    }

    private static BigDecimal sumar(List<BigDecimal> valores) {
        if (valores == null || valores.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return valores.stream()
                .filter(valor -> valor != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static String nombreUsuario(UsuarioEntity usuario) {
        StringBuilder nombre = new StringBuilder();

        if (usuario.getPrimerNombre() != null) {
            nombre.append(usuario.getPrimerNombre());
        }

        if (usuario.getSegundoNombre() != null && !usuario.getSegundoNombre().isBlank()) {
            nombre.append(" ").append(usuario.getSegundoNombre());
        }

        if (usuario.getPrimerApellido() != null) {
            nombre.append(" ").append(usuario.getPrimerApellido());
        }

        if (usuario.getSegundoApellido() != null && !usuario.getSegundoApellido().isBlank()) {
            nombre.append(" ").append(usuario.getSegundoApellido());
        }

        return nombre.toString().trim();
    }
}
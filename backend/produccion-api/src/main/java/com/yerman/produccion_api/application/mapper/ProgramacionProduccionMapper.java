package com.yerman.produccion_api.application.mapper;

import com.yerman.produccion_api.application.dto.response.ProgramacionProduccionResponse;
import com.yerman.produccion_api.domain.model.ProgramacionProduccion;
import com.yerman.produccion_api.infrastructure.entity.*;

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
                domain.getIdProducto(),
                domain.getIdTurno(),
                domain.getNumBachesPlan(),
                domain.getKgBachePlan(),
                domain.getIdFormulaVersion(),
                domain.getIdJefeProduccion(),
                domain.getEstado() != null ? domain.getEstado().name() : null,
                domain.getObservaciones());
    }
}
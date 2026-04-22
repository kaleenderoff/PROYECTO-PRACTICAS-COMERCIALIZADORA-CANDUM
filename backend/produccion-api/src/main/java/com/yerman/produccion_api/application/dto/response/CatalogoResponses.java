package com.yerman.produccion_api.application.dto.response;

import java.time.LocalDateTime;
import java.time.LocalTime;

public final class CatalogoResponses {

    private CatalogoResponses() {
    }

    public record TurnoResponse(Long id, String nombre, LocalTime horaInicio, LocalTime horaFin, Boolean activo,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
    }

    public record ProveedorResponse(Long id, String nombre, Boolean activo, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
    }

    public record MarcaResponse(Long id, String nombre, Boolean esPropia, Boolean activo, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
    }

    public record LineaResponse(Long id, String nombre, Boolean activo, LocalDateTime createdAt,
            LocalDateTime updatedAt) {
    }

    public record ProductoResponse(Long id, String nombre, Long idLinea, String nombreLinea, Boolean activo,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
    }

    public record SkuResponse(Long id, String codigoSku, String descripcion, Long idProducto, String nombreProducto,
            Long idMarca, String nombreMarca, Integer pesoNetoGr, String tipoEnvase, Integer unidadesPorCaja,
            Boolean esExport, Boolean activo, LocalDateTime createdAt, LocalDateTime updatedAt) {
    }
}

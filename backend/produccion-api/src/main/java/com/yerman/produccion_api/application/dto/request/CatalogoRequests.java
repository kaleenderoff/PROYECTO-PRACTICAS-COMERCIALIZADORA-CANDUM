package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalTime;

public final class CatalogoRequests {

    private CatalogoRequests() {
    }

    public record TurnoRequest(
            @NotBlank String nombre,
            @NotNull LocalTime horaInicio,
            @NotNull LocalTime horaFin,
            Boolean activo) {
    }

    public record ProveedorRequest(
            @NotBlank String nombre,
            Boolean activo) {
    }

    public record MarcaRequest(
            @NotBlank String nombre,
            @NotNull Boolean esPropia,
            Boolean activo) {
    }

    public record LineaRequest(
            @NotBlank String nombre,
            Boolean activo) {
    }

    public record ProductoRequest(
            @NotBlank String nombre,
            @NotNull Long idLinea,
            Boolean activo) {
    }

    public record SkuRequest(
            @NotBlank String codigoSku,
            @NotBlank String descripcion,
            @NotNull Long idProducto,
            @NotNull Long idMarca,
            @NotNull @Min(1) Integer pesoNetoGr,
            String tipoEnvase,
            Integer unidadesPorCaja,
            Boolean esExport,
            Boolean activo) {
    }

    public record InsumoRequest(
            String codigo,
            @NotBlank String nombre,
            String descripcion,
            String tipo,
            @NotBlank String unidadMedida,
            BigDecimal stockMinimo,
            Long idProveedor,
            Boolean activo) {
    }
}

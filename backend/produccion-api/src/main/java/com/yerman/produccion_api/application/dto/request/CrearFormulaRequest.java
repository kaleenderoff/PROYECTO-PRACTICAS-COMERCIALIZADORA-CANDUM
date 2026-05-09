package com.yerman.produccion_api.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearFormulaRequest(

        @NotBlank(message = "El nombre de la fórmula es obligatorio") @Size(max = 150, message = "El nombre de la fórmula no puede superar 150 caracteres") String nombre,

        @NotNull(message = "El producto es obligatorio") Long idProducto) {
}
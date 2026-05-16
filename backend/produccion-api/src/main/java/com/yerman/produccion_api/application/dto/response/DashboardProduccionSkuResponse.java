package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DashboardProduccionSkuResponse(
        String tipoProducto, // "LC" o "DL"
        List<SkuProduccionMensual> items
) {
    public record SkuProduccionMensual(
            String sku,
            BigDecimal pesoNeto,
            Integer unidadesMes,
            BigDecimal kilosMes,
            List<ProduccionDiariaSku> detalleDiario
    ) {}

    public record ProduccionDiariaSku(
            LocalDate fecha,
            Integer unidades,
            BigDecimal kilos
    ) {}
}

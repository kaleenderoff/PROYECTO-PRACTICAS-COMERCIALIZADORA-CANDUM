package com.yerman.produccion_api.application.dto.request;

import java.math.BigDecimal;

public record RegistrarProduccionSkuRequest(
    Long idOrdenDetalle,
    BigDecimal cantidadReal,
    Integer unidadesReales,
    String observaciones
) {}

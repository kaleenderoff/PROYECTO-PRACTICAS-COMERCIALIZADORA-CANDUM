package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record DashboardGerencialResponse(
        ResumenMesGerencial resumenMes,
        List<SemanaGerencialResponse> tablaSemanal,
        KpisRendimiento kpis
) {

    public record ResumenMesGerencial(
            BigDecimal lecheRecibidaTotal,
            BigDecimal kgCremaReal,
            BigDecimal kgCremaEsperada,
            BigDecimal ptDulceLecheKg,
            BigDecimal ptLecheCondensadaKg
    ) {}

    public record SemanaGerencialResponse(
            Integer numeroSemana,
            String rangoFechas,
            BigDecimal lecheRecibida,
            BigDecimal litrosCrema,
            BigDecimal kgCrema,
            BigDecimal lecheProcesada,
            BigDecimal ptDulceLeche,
            BigDecimal kgReproceso,
            BigDecimal rend1DulceLeche,
            BigDecimal rend2DulceLeche,
            BigDecimal ptLecheCondensada,
            BigDecimal rendLecheCondensada
    ) {}

    public record KpisRendimiento(
            BigDecimal rendDulceLechePromedio,
            BigDecimal rendLecheCondensadaPromedio,
            BigDecimal metaDulceLeche,
            BigDecimal metaLecheCondensada,
            String estadoDulceLeche, // "OPTIMO", "ALERTA", "CRITICO"
            String estadoLecheCondensada
    ) {}
}

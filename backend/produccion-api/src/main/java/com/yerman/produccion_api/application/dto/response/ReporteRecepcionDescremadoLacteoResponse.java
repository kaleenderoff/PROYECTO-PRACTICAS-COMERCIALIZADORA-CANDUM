package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ReporteRecepcionDescremadoLacteoResponse(
        LocalDate fecha,
        TotalesRecepcionDescremadoResponse totales,
        List<RecepcionDescremadoDetalleResponse> detalles) {

    public record TotalesRecepcionDescremadoResponse(
            Integer recepciones,
            Integer proveedores,
            Integer descremados,
            BigDecimal litrosRecibidos,
            BigDecimal litrosRemision,
            BigDecimal litrosDescremados,
            BigDecimal cremaObtenidaKg,
            Integer unidadesCrema) {
    }

    public record RecepcionDescremadoDetalleResponse(
            Long idRecepcion,
            LocalDate fechaRecepcion,
            String proveedor,
            String tipoMateriaPrima,
            BigDecimal litrosRecibidos,
            BigDecimal litrosRemision,
            String numeroRemision,
            Long idTanqueRecepcion,
            String tanqueRecepcion,
            String recibidoPor,
            String observacionesRecepcion,
            List<PesajeRecepcionResponse> pesajes,
            List<DescremadoRecepcionDetalleResponse> descremados) {
    }

    public record PesajeRecepcionResponse(
            Long id,
            Integer numeroPesaje,
            BigDecimal pesoBrutoKg,
            BigDecimal taraKg,
            BigDecimal pesoNetoKg,
            String observaciones) {
    }

    public record DescremadoRecepcionDetalleResponse(
            Long idDescremado,
            BigDecimal litrosDescremados,
            BigDecimal cremaObtenidaKg,
            Long idSkuCrema,
            String skuCrema,
            Integer unidadesCrema,
            BigDecimal kgPorUnidadCrema,
            String loteCrema,
            Long idTanqueDestino,
            String tanqueDestino,
            MovimientoLecheReporteResponse movimientoSalida,
            MovimientoLecheReporteResponse movimientoEntrada,
            String observaciones) {
    }

    public record MovimientoLecheReporteResponse(
            Long id,
            Long idTanque,
            String tanque,
            String tipoMovimiento,
            LocalDateTime fechaHora,
            BigDecimal cantidadLitros,
            BigDecimal saldoResultanteLitros,
            String referencia,
            String observaciones) {
    }
}

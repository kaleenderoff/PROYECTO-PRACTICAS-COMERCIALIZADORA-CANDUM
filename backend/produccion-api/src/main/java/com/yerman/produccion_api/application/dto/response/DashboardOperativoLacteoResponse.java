package com.yerman.produccion_api.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DashboardOperativoLacteoResponse(
        LocalDate fecha,
        TotalesOperativosResponse totales,
        List<SaldoTanqueResponse> saldosTanques,
        List<RecepcionOperativaResponse> recepciones,
        List<DescremadoOperativoResponse> descremados,
        List<ProduccionOperativaResponse> producciones,
        List<ProductoTerminadoOperativoResponse> productosTerminados,
        List<EmpaqueOperativoResponse> empaques,
        List<MovimientoLecheOperativoResponse> movimientosLeche) {

    public record TotalesOperativosResponse(
            BigDecimal litrosRecibidos,
            BigDecimal litrosDescremados,
            BigDecimal cremaObtenidaKg,
            BigDecimal litrosConsumidosProduccion,
            BigDecimal kilosProducidos,
            BigDecimal kilosProductoTerminado,
            Integer unidadesEmpacadas,
            Integer recepciones,
            Integer producciones,
            Integer registrosInsumo,
            Integer medicionesCalidad) {
    }

    public record SaldoTanqueResponse(
            Long idTanque,
            String nombre,
            String tipo,
            BigDecimal saldoLitros,
            Boolean activo) {
    }

    public record RecepcionOperativaResponse(
            Long id,
            LocalDate fechaRecepcion,
            String proveedor,
            String tipoMateriaPrima,
            BigDecimal cantidadRecibidaLitros,
            BigDecimal cantidadRemisionLitros,
            Long idTanque,
            String tanque,
            String numeroRemision,
            String recibidoPor,
            String observaciones) {
    }

    public record DescremadoOperativoResponse(
            Long id,
            Long idRecepcionLeche,
            String proveedor,
            BigDecimal litrosDescremados,
            BigDecimal cremaObtenidaKg,
            Long idTanqueDestino,
            String tanqueDestino,
            Long idSkuCrema,
            String skuCrema,
            Integer unidadesCrema,
            BigDecimal kgPorUnidadCrema,
            String loteCrema,
            String observaciones) {
    }

    public record ProduccionOperativaResponse(
            Long id,
            LocalDate fechaProduccion,
            String producto,
            Long idTanque,
            String tanque,
            Long idUsuario,
            String usuario,
            BigDecimal litrosConsumidos,
            BigDecimal kilosProducidos,
            List<BatchOperativoResponse> batches,
            List<MedicionCalidadOperativaResponse> medicionesCalidad,
            List<RegistroInsumoOperativoResponse> insumos,
            String observaciones) {
    }

    public record BatchOperativoResponse(
            Long id,
            Integer numeroBatch,
            Long idMarmita,
            String marmita,
            BigDecimal litrosConsumidos,
            BigDecimal kilosProducidos,
            BigDecimal rendimiento,
            String observaciones) {
    }

    public record MedicionCalidadOperativaResponse(
            Long id,
            Long idBatch,
            String tipoMedicion,
            String referencia,
            BigDecimal brix,
            BigDecimal ph,
            LocalDateTime fechaHoraMedicion,
            String observaciones) {
    }

    public record RegistroInsumoOperativoResponse(
            Long id,
            Long idBatch,
            Long idInsumo,
            String insumo,
            String loteInsumo,
            BigDecimal cantidadRequerida,
            BigDecimal cantidadUsada,
            String unidadMedida,
            LocalDateTime fechaHoraRegistro,
            String observaciones) {
    }

    public record ProductoTerminadoOperativoResponse(
            Long id,
            Long idBatch,
            String producto,
            String lote,
            BigDecimal kilosProducidos,
            BigDecimal kilosDisponibles,
            String estado,
            String observaciones) {
    }

    public record EmpaqueOperativoResponse(
            Long id,
            Long idProductoTerminado,
            Long idBatch,
            Long idSku,
            String sku,
            String loteEmpaque,
            LocalDate fechaEmpaque,
            LocalDate fechaVencimiento,
            BigDecimal kilosUtilizados,
            Integer unidades,
            BigDecimal cajas,
            BigDecimal pesoTotalKg,
            String estado,
            String observaciones) {
    }

    public record MovimientoLecheOperativoResponse(
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

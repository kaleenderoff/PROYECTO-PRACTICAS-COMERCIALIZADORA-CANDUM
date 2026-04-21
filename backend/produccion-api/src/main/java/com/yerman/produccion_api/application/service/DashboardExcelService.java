package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.dto.response.DashboardProduccionPorSkuResponse;
import com.yerman.produccion_api.application.dto.response.DashboardProduccionVsEmpaqueResponse;
import com.yerman.produccion_api.application.dto.response.DashboardTrazabilidadDetalleResponse;
import com.yerman.produccion_api.application.dto.response.DashboardTrazabilidadEmpaqueResponse;
import com.yerman.produccion_api.application.dto.response.DashboardTrazabilidadLoteResponse;
import com.yerman.produccion_api.application.dto.response.DashboardValidacionPendienteResponse;
import com.yerman.produccion_api.application.dto.response.DashboardValidacionResponse;
import com.yerman.produccion_api.domain.port.in.GestionDashboardUseCase;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class DashboardExcelService {

    private final GestionDashboardUseCase gestionDashboardUseCase;

    public DashboardExcelService(GestionDashboardUseCase gestionDashboardUseCase) {
        this.gestionDashboardUseCase = gestionDashboardUseCase;
    }

    public byte[] exportarValidaciones() throws IOException {
        List<DashboardValidacionResponse> validaciones = gestionDashboardUseCase.obtenerValidaciones();

        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Validaciones");

            crearEncabezadoValidaciones(sheet);
            llenarFilasValidaciones(sheet, validaciones);
            autoAjustarColumnas(sheet, 8);

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public byte[] exportarValidacionesPendientes() throws IOException {
        List<DashboardValidacionPendienteResponse> pendientes = gestionDashboardUseCase.obtenerValidacionesPendientes();

        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("ValidacionesPendientes");

            crearEncabezadoValidacionesPendientes(sheet);
            llenarFilasValidacionesPendientes(sheet, pendientes);
            autoAjustarColumnas(sheet, 11);

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public byte[] exportarProduccionPorSku() throws IOException {
        List<DashboardProduccionPorSkuResponse> data = gestionDashboardUseCase.obtenerProduccionPorSku();

        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("ProduccionPorSKU");

            crearEncabezadoProduccionPorSku(sheet);
            llenarFilasProduccionPorSku(sheet, data);
            autoAjustarColumnas(sheet, 7);

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public byte[] exportarProduccionVsEmpaque() throws IOException {
        List<DashboardProduccionVsEmpaqueResponse> data = gestionDashboardUseCase.obtenerProduccionVsEmpaque();

        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("ProduccionVsEmpaque");

            crearEncabezadoProduccionVsEmpaque(sheet);
            llenarFilasProduccionVsEmpaque(sheet, data);
            autoAjustarColumnas(sheet, 12);

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    public byte[] exportarTrazabilidadPorLote(String lote) throws IOException {
        DashboardTrazabilidadLoteResponse trazabilidad = gestionDashboardUseCase.obtenerTrazabilidadPorLote(lote);

        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet resumen = workbook.createSheet("ResumenLote");
            Sheet detalles = workbook.createSheet("Detalles");
            Sheet empaques = workbook.createSheet("Empaques");

            llenarResumenLote(resumen, trazabilidad);
            llenarDetallesLote(detalles, trazabilidad.getDetalles());
            llenarEmpaquesLote(empaques, trazabilidad.getDetalles());

            autoAjustarColumnas(resumen, 5);
            autoAjustarColumnas(detalles, 9);
            autoAjustarColumnas(empaques, 8);

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void crearEncabezadoValidaciones(Sheet sheet) {
        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("ID Validación");
        header.createCell(1).setCellValue("ID Detalle Producción");
        header.createCell(2).setCellValue("ID Producción");
        header.createCell(3).setCellValue("Número Lote");
        header.createCell(4).setCellValue("Estado Validación");
        header.createCell(5).setCellValue("Observación");
        header.createCell(6).setCellValue("ID Validador");
        header.createCell(7).setCellValue("Nombre Validador");
        header.createCell(8).setCellValue("Fecha Validación");
    }

    private void llenarFilasValidaciones(Sheet sheet, List<DashboardValidacionResponse> validaciones) {
        int rowIndex = 1;

        for (DashboardValidacionResponse item : validaciones) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(valorLong(item.getIdValidacion()));
            row.createCell(1).setCellValue(valorLong(item.getIdDetalleProduccion()));
            row.createCell(2).setCellValue(valorLong(item.getIdProduccion()));
            row.createCell(3).setCellValue(valorTexto(item.getNumeroLote()));
            row.createCell(4).setCellValue(valorTexto(item.getEstadoValidacion()));
            row.createCell(5).setCellValue(valorTexto(item.getObservacion()));
            row.createCell(6).setCellValue(valorLong(item.getIdValidador()));
            row.createCell(7).setCellValue(valorTexto(item.getNombreValidador()));
            row.createCell(8)
                    .setCellValue(item.getFechaValidacion() != null ? item.getFechaValidacion().toString() : "");
        }
    }

    private void crearEncabezadoValidacionesPendientes(Sheet sheet) {
        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("ID Detalle Producción");
        header.createCell(1).setCellValue("ID Producción");
        header.createCell(2).setCellValue("Número Lote");
        header.createCell(3).setCellValue("Fecha Producción");
        header.createCell(4).setCellValue("Estado Producción");
        header.createCell(5).setCellValue("ID Producto");
        header.createCell(6).setCellValue("Nombre Producto");
        header.createCell(7).setCellValue("Num Batch");
        header.createCell(8).setCellValue("Kg Programados");
        header.createCell(9).setCellValue("Kg Batch");
        header.createCell(10).setCellValue("Unidades Reales");
        header.createCell(11).setCellValue("Rendimiento %");
    }

    private void llenarFilasValidacionesPendientes(
            Sheet sheet,
            List<DashboardValidacionPendienteResponse> pendientes) {

        int rowIndex = 1;

        for (DashboardValidacionPendienteResponse item : pendientes) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(valorLong(item.getIdDetalleProduccion()));
            row.createCell(1).setCellValue(valorLong(item.getIdProduccion()));
            row.createCell(2).setCellValue(valorTexto(item.getNumeroLote()));
            row.createCell(3)
                    .setCellValue(item.getFechaProduccion() != null ? item.getFechaProduccion().toString() : "");
            row.createCell(4).setCellValue(valorTexto(item.getEstadoProduccion()));
            row.createCell(5).setCellValue(valorLong(item.getIdProducto()));
            row.createCell(6).setCellValue(valorTexto(item.getNombreProducto()));
            row.createCell(7).setCellValue(valorInteger(item.getNumBatch()));
            row.createCell(8).setCellValue(valorDouble(item.getKgProgramados()));
            row.createCell(9).setCellValue(valorDouble(item.getKgBatch()));
            row.createCell(10).setCellValue(valorInteger(item.getUnidadesReales()));
            row.createCell(11).setCellValue(valorDouble(item.getRendimientoPct()));
        }
    }

    private void crearEncabezadoProduccionPorSku(Sheet sheet) {
        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("ID Producto Terminado");
        header.createCell(1).setCellValue("SKU");
        header.createCell(2).setCellValue("Nombre Comercial");
        header.createCell(3).setCellValue("Referencia");
        header.createCell(4).setCellValue("Total Unidades");
        header.createCell(5).setCellValue("Total Cajas");
        header.createCell(6).setCellValue("Total Peso Kg");
        header.createCell(7).setCellValue("Total Registros Empaque");
    }

    private void llenarFilasProduccionPorSku(Sheet sheet, List<DashboardProduccionPorSkuResponse> data) {
        int rowIndex = 1;

        for (DashboardProduccionPorSkuResponse item : data) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(valorLong(item.getIdProductoTerminado()));
            row.createCell(1).setCellValue(valorTexto(item.getSku()));
            row.createCell(2).setCellValue(valorTexto(item.getNombreComercial()));
            row.createCell(3).setCellValue(valorTexto(item.getReferencia()));
            row.createCell(4).setCellValue(item.getTotalUnidades());
            row.createCell(5).setCellValue(item.getTotalCajas());
            row.createCell(6).setCellValue(valorDouble(item.getTotalPesoKg()));
            row.createCell(7).setCellValue(item.getTotalRegistrosEmpaque());
        }
    }

    private void crearEncabezadoProduccionVsEmpaque(Sheet sheet) {
        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("ID Detalle Producción");
        header.createCell(1).setCellValue("ID Producción");
        header.createCell(2).setCellValue("Número Lote Producción");
        header.createCell(3).setCellValue("ID Producto");
        header.createCell(4).setCellValue("Nombre Producto");
        header.createCell(5).setCellValue("Num Batch");
        header.createCell(6).setCellValue("Kg Programados");
        header.createCell(7).setCellValue("Kg Batch");
        header.createCell(8).setCellValue("Unidades Reales");
        header.createCell(9).setCellValue("Unidades Empacadas");
        header.createCell(10).setCellValue("Cajas Empacadas");
        header.createCell(11).setCellValue("Peso Empacado Kg");
        header.createCell(12).setCellValue("Unidades Pendientes");
    }

    private void llenarFilasProduccionVsEmpaque(
            Sheet sheet,
            List<DashboardProduccionVsEmpaqueResponse> data) {

        int rowIndex = 1;

        for (DashboardProduccionVsEmpaqueResponse item : data) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(valorLong(item.getIdDetalleProduccion()));
            row.createCell(1).setCellValue(valorLong(item.getIdProduccion()));
            row.createCell(2).setCellValue(valorTexto(item.getNumeroLoteProduccion()));
            row.createCell(3).setCellValue(valorLong(item.getIdProducto()));
            row.createCell(4).setCellValue(valorTexto(item.getNombreProducto()));
            row.createCell(5).setCellValue(valorInteger(item.getNumBatch()));
            row.createCell(6).setCellValue(valorDouble(item.getKgProgramados()));
            row.createCell(7).setCellValue(valorDouble(item.getKgBatch()));
            row.createCell(8).setCellValue(item.getUnidadesReales());
            row.createCell(9).setCellValue(item.getUnidadesEmpacadas());
            row.createCell(10).setCellValue(item.getCajasEmpacadas());
            row.createCell(11).setCellValue(valorDouble(item.getPesoEmpacadoKg()));
            row.createCell(12).setCellValue(item.getUnidadesPendientesPorEmpacar());
        }
    }

    private void llenarResumenLote(Sheet sheet, DashboardTrazabilidadLoteResponse trazabilidad) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Campo");
        header.createCell(1).setCellValue("Valor");

        crearFilaResumen(sheet, 1, "ID Producción", String.valueOf(valorLong(trazabilidad.getIdProduccion())));
        crearFilaResumen(sheet, 2, "Número Lote", valorTexto(trazabilidad.getNumeroLote()));
        crearFilaResumen(sheet, 3, "Fecha Producción",
                trazabilidad.getFechaProduccion() != null ? trazabilidad.getFechaProduccion().toString() : "");
        crearFilaResumen(sheet, 4, "Fecha Vencimiento",
                trazabilidad.getFechaVencimiento() != null ? trazabilidad.getFechaVencimiento().toString() : "");
        crearFilaResumen(sheet, 5, "Estado Producción", valorTexto(trazabilidad.getEstadoProduccion()));
        crearFilaResumen(sheet, 6, "Tipo Turno", valorTexto(trazabilidad.getTipoTurno()));
        crearFilaResumen(sheet, 7, "Línea Producción", valorTexto(trazabilidad.getLineaProduccion()));
    }

    private void crearFilaResumen(Sheet sheet, int rowIndex, String campo, String valor) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(campo);
        row.createCell(1).setCellValue(valor);
    }

    private void llenarDetallesLote(Sheet sheet, List<DashboardTrazabilidadDetalleResponse> detalles) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID Detalle");
        header.createCell(1).setCellValue("ID Producto");
        header.createCell(2).setCellValue("Nombre Producto");
        header.createCell(3).setCellValue("Num Batch");
        header.createCell(4).setCellValue("Kg Programados");
        header.createCell(5).setCellValue("Kg Batch");
        header.createCell(6).setCellValue("Unidades Reales");
        header.createCell(7).setCellValue("Rendimiento %");
        header.createCell(8).setCellValue("Estado Validación");
        header.createCell(9).setCellValue("Observación Validación");

        int rowIndex = 1;
        for (DashboardTrazabilidadDetalleResponse item : detalles) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(valorLong(item.getIdDetalleProduccion()));
            row.createCell(1).setCellValue(valorLong(item.getIdProducto()));
            row.createCell(2).setCellValue(valorTexto(item.getNombreProducto()));
            row.createCell(3).setCellValue(valorInteger(item.getNumBatch()));
            row.createCell(4).setCellValue(valorDouble(item.getKgProgramados()));
            row.createCell(5).setCellValue(valorDouble(item.getKgBatch()));
            row.createCell(6).setCellValue(valorInteger(item.getUnidadesReales()));
            row.createCell(7).setCellValue(valorDouble(item.getRendimientoPct()));
            row.createCell(8).setCellValue(valorTexto(item.getEstadoValidacion()));
            row.createCell(9).setCellValue(valorTexto(item.getObservacionValidacion()));
        }
    }

    private void llenarEmpaquesLote(
            Sheet sheet,
            List<DashboardTrazabilidadDetalleResponse> detalles) {

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID Detalle");
        header.createCell(1).setCellValue("ID Empaque");
        header.createCell(2).setCellValue("Lote Empaque");
        header.createCell(3).setCellValue("Fecha Empaque");
        header.createCell(4).setCellValue("Fecha Vencimiento");
        header.createCell(5).setCellValue("Estado Empaque");
        header.createCell(6).setCellValue("Cantidad Unidades");
        header.createCell(7).setCellValue("Cantidad Cajas");
        header.createCell(8).setCellValue("ID Producto Terminado");
        header.createCell(9).setCellValue("SKU");
        header.createCell(10).setCellValue("Nombre Comercial");

        int rowIndex = 1;

        for (DashboardTrazabilidadDetalleResponse detalle : detalles) {
            for (DashboardTrazabilidadEmpaqueResponse empaque : detalle.getEmpaques()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(valorLong(detalle.getIdDetalleProduccion()));
                row.createCell(1).setCellValue(valorLong(empaque.getIdEmpaque()));
                row.createCell(2).setCellValue(valorTexto(empaque.getLoteEmpaque()));
                row.createCell(3)
                        .setCellValue(empaque.getFechaEmpaque() != null ? empaque.getFechaEmpaque().toString() : "");
                row.createCell(4).setCellValue(
                        empaque.getFechaVencimiento() != null ? empaque.getFechaVencimiento().toString() : "");
                row.createCell(5).setCellValue(valorTexto(empaque.getEstadoEmpaque()));
                row.createCell(6).setCellValue(valorInteger(empaque.getCantidadUnidades()));
                row.createCell(7).setCellValue(valorInteger(empaque.getCantidadCajas()));
                row.createCell(8).setCellValue(valorLong(empaque.getIdProductoTerminado()));
                row.createCell(9).setCellValue(valorTexto(empaque.getSku()));
                row.createCell(10).setCellValue(valorTexto(empaque.getNombreComercial()));
            }
        }
    }

    private void autoAjustarColumnas(Sheet sheet, int lastColumnIndex) {
        for (int i = 0; i <= lastColumnIndex; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private double valorLong(Long value) {
        return value != null ? value : 0L;
    }

    private double valorDouble(Number value) {
        return value != null ? value.doubleValue() : 0D;
    }

    private int valorInteger(Integer value) {
        return value != null ? value : 0;
    }

    private String valorTexto(String value) {
        return value != null ? value : "";
    }
}
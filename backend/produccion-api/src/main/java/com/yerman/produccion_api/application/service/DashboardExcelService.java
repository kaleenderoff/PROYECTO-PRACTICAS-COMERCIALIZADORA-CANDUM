package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.dto.response.DashboardProduccionPorSkuResponse;
import com.yerman.produccion_api.application.dto.response.DashboardProduccionVsEmpaqueResponse;
import com.yerman.produccion_api.application.dto.response.DashboardValidacionResponse;
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

    private final DashboardService dashboardService;

    public DashboardExcelService(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    public byte[] exportarValidaciones() throws IOException {
        List<DashboardValidacionResponse> validaciones = dashboardService.obtenerValidaciones();

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

    public byte[] exportarProduccionPorSku() throws IOException {
        List<DashboardProduccionPorSkuResponse> data = dashboardService.obtenerProduccionPorSku();

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
        List<DashboardProduccionVsEmpaqueResponse> data = dashboardService.obtenerProduccionVsEmpaque();

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
            row.createCell(8).setCellValue(
                    item.getFechaValidacion() != null ? item.getFechaValidacion().toString() : "");
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
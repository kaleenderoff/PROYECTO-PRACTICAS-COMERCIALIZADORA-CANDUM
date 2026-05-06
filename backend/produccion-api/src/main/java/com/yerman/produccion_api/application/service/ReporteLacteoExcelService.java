package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.dto.response.ReporteConsumoInsumosLacteoResponse;
import com.yerman.produccion_api.application.dto.response.ReporteConsumoInsumosLacteoResponse.DetalleConsumoInsumoResponse;
import com.yerman.produccion_api.application.dto.response.ReporteRecepcionDescremadoLacteoResponse;
import com.yerman.produccion_api.application.dto.response.ReporteRecepcionDescremadoLacteoResponse.DescremadoRecepcionDetalleResponse;
import com.yerman.produccion_api.application.dto.response.ReporteRecepcionDescremadoLacteoResponse.PesajeRecepcionResponse;
import com.yerman.produccion_api.application.dto.response.ReporteRecepcionDescremadoLacteoResponse.RecepcionDescremadoDetalleResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class ReporteLacteoExcelService {

    private final ReporteLacteoService reporteLacteoService;

    public ReporteLacteoExcelService(ReporteLacteoService reporteLacteoService) {
        this.reporteLacteoService = reporteLacteoService;
    }

    public byte[] generarExcelConsumoInsumos(LocalDate fecha) {
        ReporteConsumoInsumosLacteoResponse reporte = reporteLacteoService.consultarConsumoInsumos(fecha);

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            CellStyle headerStyle = crearHeaderStyle(workbook);

            crearHojaResumenConsumo(workbook, headerStyle, reporte);
            crearHojaDetalleConsumo(workbook, headerStyle, reporte);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando Excel de consumo de insumos lacteos", e);
        }
    }

    public byte[] generarExcelRecepcionDescremado(LocalDate fecha) {
        ReporteRecepcionDescremadoLacteoResponse reporte = reporteLacteoService.consultarRecepcionDescremado(fecha);

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            CellStyle headerStyle = crearHeaderStyle(workbook);

            crearHojaResumenRecepcionDescremado(workbook, headerStyle, reporte);
            crearHojaRecepciones(workbook, headerStyle, reporte);
            crearHojaDescremados(workbook, headerStyle, reporte);
            crearHojaPesajes(workbook, headerStyle, reporte);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando Excel de recepcion y descremado lacteo", e);
        }
    }

    private void crearHojaResumenConsumo(
            XSSFWorkbook workbook,
            CellStyle headerStyle,
            ReporteConsumoInsumosLacteoResponse reporte) {
        Sheet sheet = workbook.createSheet("Resumen");

        Row title = sheet.createRow(0);
        title.createCell(0).setCellValue("Reporte consumo de insumos lacteos");
        title.getCell(0).setCellStyle(headerStyle);

        Row fecha = sheet.createRow(2);
        fecha.createCell(0).setCellValue("Fecha");
        fecha.createCell(1).setCellValue(reporte.fecha().toString());

        Row header = sheet.createRow(4);
        header.createCell(0).setCellValue("Registros");
        header.createCell(1).setCellValue("Producciones");
        header.createCell(2).setCellValue("Batches");
        header.createCell(3).setCellValue("Cantidad requerida total");
        header.createCell(4).setCellValue("Cantidad usada total");
        header.createCell(5).setCellValue("Diferencia total");
        aplicarEstilo(header, headerStyle, 6);

        Row values = sheet.createRow(5);
        values.createCell(0).setCellValue(reporte.totales().registros());
        values.createCell(1).setCellValue(reporte.totales().producciones());
        values.createCell(2).setCellValue(reporte.totales().batches());
        values.createCell(3).setCellValue(numero(reporte.totales().cantidadRequeridaTotal()));
        values.createCell(4).setCellValue(numero(reporte.totales().cantidadUsadaTotal()));
        values.createCell(5).setCellValue(numero(reporte.totales().diferenciaTotal()));

        autoSize(sheet, 6);
    }

    private void crearHojaDetalleConsumo(
            XSSFWorkbook workbook,
            CellStyle headerStyle,
            ReporteConsumoInsumosLacteoResponse reporte) {
        Sheet sheet = workbook.createSheet("Detalle");

        Row header = sheet.createRow(0);
        String[] headers = {
                "Fecha produccion",
                "Id produccion",
                "Producto",
                "Id batch",
                "Batch",
                "Codigo insumo",
                "Insumo",
                "Tipo insumo",
                "Lote insumo",
                "Cantidad requerida",
                "Cantidad usada",
                "Diferencia",
                "Unidad",
                "Fecha hora registro",
                "Usuario",
                "Observaciones"
        };
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }
        aplicarEstilo(header, headerStyle, headers.length);

        int rowIndex = 1;
        for (DetalleConsumoInsumoResponse item : reporte.detalles()) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(item.fechaProduccion().toString());
            row.createCell(1).setCellValue(item.idProduccion());
            row.createCell(2).setCellValue(item.producto());
            if (item.idBatch() != null) {
                row.createCell(3).setCellValue(item.idBatch());
            }
            if (item.numeroBatch() != null) {
                row.createCell(4).setCellValue(item.numeroBatch());
            }
            row.createCell(5).setCellValue(texto(item.codigoInsumo()));
            row.createCell(6).setCellValue(item.insumo());
            row.createCell(7).setCellValue(item.tipoInsumo());
            row.createCell(8).setCellValue(texto(item.loteInsumo()));
            row.createCell(9).setCellValue(numero(item.cantidadRequerida()));
            row.createCell(10).setCellValue(numero(item.cantidadUsada()));
            row.createCell(11).setCellValue(numero(item.diferencia()));
            row.createCell(12).setCellValue(item.unidadMedida());
            row.createCell(13).setCellValue(item.fechaHoraRegistro().toString());
            row.createCell(14).setCellValue(item.usuario());
            row.createCell(15).setCellValue(texto(item.observaciones()));
        }

        autoSize(sheet, headers.length);
    }

    private void crearHojaResumenRecepcionDescremado(
            XSSFWorkbook workbook,
            CellStyle headerStyle,
            ReporteRecepcionDescremadoLacteoResponse reporte) {
        Sheet sheet = workbook.createSheet("Resumen");

        Row title = sheet.createRow(0);
        title.createCell(0).setCellValue("Reporte recepcion y descremado lacteo");
        title.getCell(0).setCellStyle(headerStyle);

        Row fecha = sheet.createRow(2);
        fecha.createCell(0).setCellValue("Fecha");
        fecha.createCell(1).setCellValue(reporte.fecha().toString());

        Row header = sheet.createRow(4);
        String[] headers = {
                "Recepciones",
                "Proveedores",
                "Descremados",
                "Litros recibidos",
                "Litros remision",
                "Litros descremados",
                "Crema obtenida kg",
                "Unidades crema"
        };
        crearHeader(header, headers);
        aplicarEstilo(header, headerStyle, headers.length);

        Row values = sheet.createRow(5);
        values.createCell(0).setCellValue(reporte.totales().recepciones());
        values.createCell(1).setCellValue(reporte.totales().proveedores());
        values.createCell(2).setCellValue(reporte.totales().descremados());
        values.createCell(3).setCellValue(numero(reporte.totales().litrosRecibidos()));
        values.createCell(4).setCellValue(numero(reporte.totales().litrosRemision()));
        values.createCell(5).setCellValue(numero(reporte.totales().litrosDescremados()));
        values.createCell(6).setCellValue(numero(reporte.totales().cremaObtenidaKg()));
        values.createCell(7).setCellValue(reporte.totales().unidadesCrema());

        autoSize(sheet, headers.length);
    }

    private void crearHojaRecepciones(
            XSSFWorkbook workbook,
            CellStyle headerStyle,
            ReporteRecepcionDescremadoLacteoResponse reporte) {
        Sheet sheet = workbook.createSheet("Recepciones");
        String[] headers = {
                "Id recepcion",
                "Fecha",
                "Proveedor",
                "Tipo materia prima",
                "Litros recibidos",
                "Litros remision",
                "Numero remision",
                "Tanque recepcion",
                "Recibido por",
                "Observaciones"
        };
        Row header = sheet.createRow(0);
        crearHeader(header, headers);
        aplicarEstilo(header, headerStyle, headers.length);

        int rowIndex = 1;
        for (RecepcionDescremadoDetalleResponse item : reporte.detalles()) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(item.idRecepcion());
            row.createCell(1).setCellValue(item.fechaRecepcion().toString());
            row.createCell(2).setCellValue(item.proveedor());
            row.createCell(3).setCellValue(item.tipoMateriaPrima());
            row.createCell(4).setCellValue(numero(item.litrosRecibidos()));
            row.createCell(5).setCellValue(numero(item.litrosRemision()));
            row.createCell(6).setCellValue(texto(item.numeroRemision()));
            row.createCell(7).setCellValue(item.tanqueRecepcion());
            row.createCell(8).setCellValue(texto(item.recibidoPor()));
            row.createCell(9).setCellValue(texto(item.observacionesRecepcion()));
        }

        autoSize(sheet, headers.length);
    }

    private void crearHojaDescremados(
            XSSFWorkbook workbook,
            CellStyle headerStyle,
            ReporteRecepcionDescremadoLacteoResponse reporte) {
        Sheet sheet = workbook.createSheet("Descremados");
        String[] headers = {
                "Id recepcion",
                "Proveedor",
                "Id descremado",
                "Litros descremados",
                "Crema obtenida kg",
                "SKU crema",
                "Unidades crema",
                "Kg por unidad crema",
                "Lote crema",
                "Tanque destino",
                "Movimiento salida",
                "Movimiento entrada",
                "Observaciones"
        };
        Row header = sheet.createRow(0);
        crearHeader(header, headers);
        aplicarEstilo(header, headerStyle, headers.length);

        int rowIndex = 1;
        for (RecepcionDescremadoDetalleResponse recepcion : reporte.detalles()) {
            for (DescremadoRecepcionDetalleResponse item : recepcion.descremados()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(recepcion.idRecepcion());
                row.createCell(1).setCellValue(recepcion.proveedor());
                row.createCell(2).setCellValue(item.idDescremado());
                row.createCell(3).setCellValue(numero(item.litrosDescremados()));
                row.createCell(4).setCellValue(numero(item.cremaObtenidaKg()));
                row.createCell(5).setCellValue(texto(item.skuCrema()));
                row.createCell(6).setCellValue(item.unidadesCrema() != null ? item.unidadesCrema() : 0);
                row.createCell(7).setCellValue(numero(item.kgPorUnidadCrema()));
                row.createCell(8).setCellValue(texto(item.loteCrema()));
                row.createCell(9).setCellValue(texto(item.tanqueDestino()));
                row.createCell(10).setCellValue(item.movimientoSalida() != null ? item.movimientoSalida().id() : 0);
                row.createCell(11).setCellValue(item.movimientoEntrada() != null ? item.movimientoEntrada().id() : 0);
                row.createCell(12).setCellValue(texto(item.observaciones()));
            }
        }

        autoSize(sheet, headers.length);
    }

    private void crearHojaPesajes(
            XSSFWorkbook workbook,
            CellStyle headerStyle,
            ReporteRecepcionDescremadoLacteoResponse reporte) {
        Sheet sheet = workbook.createSheet("Pesajes");
        String[] headers = {
                "Id recepcion",
                "Proveedor",
                "Id pesaje",
                "Numero pesaje",
                "Peso bruto kg",
                "Tara kg",
                "Peso neto kg",
                "Observaciones"
        };
        Row header = sheet.createRow(0);
        crearHeader(header, headers);
        aplicarEstilo(header, headerStyle, headers.length);

        int rowIndex = 1;
        for (RecepcionDescremadoDetalleResponse recepcion : reporte.detalles()) {
            for (PesajeRecepcionResponse item : recepcion.pesajes()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(recepcion.idRecepcion());
                row.createCell(1).setCellValue(recepcion.proveedor());
                row.createCell(2).setCellValue(item.id());
                row.createCell(3).setCellValue(item.numeroPesaje());
                row.createCell(4).setCellValue(numero(item.pesoBrutoKg()));
                row.createCell(5).setCellValue(numero(item.taraKg()));
                row.createCell(6).setCellValue(numero(item.pesoNetoKg()));
                row.createCell(7).setCellValue(texto(item.observaciones()));
            }
        }

        autoSize(sheet, headers.length);
    }

    private void crearHeader(Row row, String[] headers) {
        for (int i = 0; i < headers.length; i++) {
            row.createCell(i).setCellValue(headers[i]);
        }
    }

    private CellStyle crearHeaderStyle(XSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    private void aplicarEstilo(Row row, CellStyle style, int columnas) {
        for (int i = 0; i < columnas; i++) {
            row.getCell(i).setCellStyle(style);
        }
    }

    private void autoSize(Sheet sheet, int columnas) {
        for (int i = 0; i < columnas; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private double numero(BigDecimal valor) {
        return valor != null ? valor.doubleValue() : 0;
    }

    private String texto(String valor) {
        return valor != null ? valor : "";
    }
}

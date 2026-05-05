package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.dto.response.DashboardProduccionVsEmpaqueResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class DashboardExcelService {

    private final GestionDashboardService dashboardService;

    public DashboardExcelService(GestionDashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    public byte[] generarExcelProduccionVsEmpaque() {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Produccion vs Empaque");

            // Header
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Fecha");
            header.createCell(1).setCellValue("Total Producido (kg)");
            header.createCell(2).setCellValue("Total Empacado (kg)");
            header.createCell(3).setCellValue("Diferencia (kg)");

            // Data
            List<DashboardProduccionVsEmpaqueResponse> data = dashboardService.obtenerProduccionVsEmpaque();

            int rowIndex = 1;

            for (DashboardProduccionVsEmpaqueResponse item : data) {
                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(item.getFecha().toString());
                row.createCell(1).setCellValue(item.getTotalProducido().doubleValue());
                row.createCell(2).setCellValue(item.getTotalEmpaquetado().doubleValue());
                row.createCell(3).setCellValue(item.getDiferencia().doubleValue());
            }

            // Auto size
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando Excel", e);
        }
    }
}
package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.application.dto.response.DashboardGerencialResponse;
import com.yerman.produccion_api.application.dto.response.DashboardProduccionSkuResponse;
import com.yerman.produccion_api.application.dto.response.DashboardProduccionVsEmpaqueResponse;
import com.yerman.produccion_api.application.dto.response.DashboardTrazabilidadLoteResponse;
import com.yerman.produccion_api.application.dto.response.RendimientoAnualResponse;

import java.util.List;

public interface DashboardRepositoryPort {

    List<DashboardProduccionVsEmpaqueResponse> obtenerProduccionVsEmpaque();

    DashboardTrazabilidadLoteResponse obtenerTrazabilidadPorLote(String lote);

    List<DashboardProduccionSkuResponse> obtenerProduccionSkuMensual(int mes, int anio);

    List<RendimientoAnualResponse> obtenerRendimientoAnual(int anio);

    DashboardGerencialResponse.ResumenMesGerencial obtenerResumenMensualGerencial(int mes, int anio);

    List<DashboardGerencialResponse.SemanaGerencialResponse> obtenerDetalleSemanalGerencial(int mes, int anio);
}

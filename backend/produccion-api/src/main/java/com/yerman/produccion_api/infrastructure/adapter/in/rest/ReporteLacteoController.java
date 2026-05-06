package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.response.ReporteConsumoInsumosLacteoResponse;
import com.yerman.produccion_api.application.dto.response.ReporteConsumoInsumosLacteoResponse.DetalleConsumoInsumoResponse;
import com.yerman.produccion_api.application.dto.response.ReporteConsumoInsumosLacteoResponse.TotalesConsumoInsumosResponse;
import com.yerman.produccion_api.infrastructure.entity.ProduccionLacteaBatchEntity;
import com.yerman.produccion_api.infrastructure.entity.RegistroInsumoLacteoEntity;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.repository.RegistroInsumoLacteoJpaRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/reportes/lacteos")
public class ReporteLacteoController {

    private static final BigDecimal CERO = BigDecimal.ZERO;

    private final RegistroInsumoLacteoJpaRepository registroInsumoRepository;

    public ReporteLacteoController(RegistroInsumoLacteoJpaRepository registroInsumoRepository) {
        this.registroInsumoRepository = registroInsumoRepository;
    }

    @GetMapping("/consumo-insumos")
    @Transactional(readOnly = true)
    public ReporteConsumoInsumosLacteoResponse consultarConsumoInsumos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDate fechaConsulta = fecha != null ? fecha : LocalDate.now();
        List<RegistroInsumoLacteoEntity> registros =
                registroInsumoRepository.findByProduccionLacteaFechaProduccionOrderByProduccionLacteaIdAscFechaHoraRegistroAsc(
                        fechaConsulta);
        List<DetalleConsumoInsumoResponse> detalles = registros.stream()
                .map(this::toDetalle)
                .toList();

        BigDecimal cantidadRequeridaTotal = detalles.stream()
                .map(DetalleConsumoInsumoResponse::cantidadRequerida)
                .map(this::valor)
                .reduce(CERO, BigDecimal::add);
        BigDecimal cantidadUsadaTotal = detalles.stream()
                .map(DetalleConsumoInsumoResponse::cantidadUsada)
                .map(this::valor)
                .reduce(CERO, BigDecimal::add);

        TotalesConsumoInsumosResponse totales = new TotalesConsumoInsumosResponse(
                detalles.size(),
                (int) detalles.stream().map(DetalleConsumoInsumoResponse::idProduccion).distinct().count(),
                (int) detalles.stream().map(DetalleConsumoInsumoResponse::idBatch).filter(Objects::nonNull).distinct().count(),
                cantidadRequeridaTotal,
                cantidadUsadaTotal,
                cantidadUsadaTotal.subtract(cantidadRequeridaTotal));

        return new ReporteConsumoInsumosLacteoResponse(fechaConsulta, totales, detalles);
    }

    private DetalleConsumoInsumoResponse toDetalle(RegistroInsumoLacteoEntity entity) {
        ProduccionLacteaBatchEntity batch = entity.getProduccionLacteaBatch();

        return new DetalleConsumoInsumoResponse(
                entity.getId(),
                entity.getProduccionLactea().getFechaProduccion(),
                entity.getProduccionLactea().getId(),
                entity.getProduccionLactea().getProducto(),
                batch != null ? batch.getId() : null,
                batch != null ? batch.getNumeroBatch() : null,
                entity.getInsumo().getId(),
                entity.getInsumo().getCodigo(),
                entity.getInsumo().getNombre(),
                entity.getInsumo().getTipo().name(),
                entity.getLoteInsumo(),
                entity.getCantidadRequerida(),
                entity.getCantidadUsada(),
                valor(entity.getCantidadUsada()).subtract(valor(entity.getCantidadRequerida())),
                entity.getUnidadMedida(),
                entity.getFechaHoraRegistro(),
                entity.getUsuario().getIdUsuario(),
                nombreUsuario(entity.getUsuario()),
                entity.getObservaciones());
    }

    private BigDecimal valor(BigDecimal valor) {
        return valor != null ? valor : CERO;
    }

    private String nombreUsuario(UsuarioEntity usuario) {
        String segundoNombre = usuario.getSegundoNombre() != null ? " " + usuario.getSegundoNombre() : "";
        String segundoApellido = usuario.getSegundoApellido() != null ? " " + usuario.getSegundoApellido() : "";
        return (usuario.getPrimerNombre() + segundoNombre + " " + usuario.getPrimerApellido() + segundoApellido).trim();
    }
}

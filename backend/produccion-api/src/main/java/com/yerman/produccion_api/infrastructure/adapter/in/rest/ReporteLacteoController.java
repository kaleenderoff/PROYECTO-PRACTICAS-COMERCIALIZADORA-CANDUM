package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.response.ReporteConsumoInsumosLacteoResponse;
import com.yerman.produccion_api.application.dto.response.ReporteConsumoInsumosLacteoResponse.DetalleConsumoInsumoResponse;
import com.yerman.produccion_api.application.dto.response.ReporteConsumoInsumosLacteoResponse.TotalesConsumoInsumosResponse;
import com.yerman.produccion_api.application.dto.response.ReporteRecepcionDescremadoLacteoResponse;
import com.yerman.produccion_api.application.dto.response.ReporteRecepcionDescremadoLacteoResponse.DescremadoRecepcionDetalleResponse;
import com.yerman.produccion_api.application.dto.response.ReporteRecepcionDescremadoLacteoResponse.MovimientoLecheReporteResponse;
import com.yerman.produccion_api.application.dto.response.ReporteRecepcionDescremadoLacteoResponse.PesajeRecepcionResponse;
import com.yerman.produccion_api.application.dto.response.ReporteRecepcionDescremadoLacteoResponse.RecepcionDescremadoDetalleResponse;
import com.yerman.produccion_api.application.dto.response.ReporteRecepcionDescremadoLacteoResponse.TotalesRecepcionDescremadoResponse;
import com.yerman.produccion_api.infrastructure.entity.DescremadoRecepcionEntity;
import com.yerman.produccion_api.infrastructure.entity.MovimientoLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.ProduccionLacteaBatchEntity;
import com.yerman.produccion_api.infrastructure.entity.RecepcionLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.RecepcionLechePesajeEntity;
import com.yerman.produccion_api.infrastructure.entity.RegistroInsumoLacteoEntity;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.repository.DescremadoRecepcionJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.RecepcionLecheJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.RecepcionLechePesajeJpaRepository;
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
    private final RecepcionLecheJpaRepository recepcionLecheRepository;
    private final RecepcionLechePesajeJpaRepository recepcionLechePesajeRepository;
    private final DescremadoRecepcionJpaRepository descremadoRecepcionRepository;

    public ReporteLacteoController(
            RegistroInsumoLacteoJpaRepository registroInsumoRepository,
            RecepcionLecheJpaRepository recepcionLecheRepository,
            RecepcionLechePesajeJpaRepository recepcionLechePesajeRepository,
            DescremadoRecepcionJpaRepository descremadoRecepcionRepository) {
        this.registroInsumoRepository = registroInsumoRepository;
        this.recepcionLecheRepository = recepcionLecheRepository;
        this.recepcionLechePesajeRepository = recepcionLechePesajeRepository;
        this.descremadoRecepcionRepository = descremadoRecepcionRepository;
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

    @GetMapping("/recepcion-descremado")
    @Transactional(readOnly = true)
    public ReporteRecepcionDescremadoLacteoResponse consultarRecepcionDescremado(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDate fechaConsulta = fecha != null ? fecha : LocalDate.now();
        List<RecepcionLecheEntity> recepciones =
                recepcionLecheRepository.findByFechaRecepcionOrderByIdDesc(fechaConsulta);
        List<RecepcionDescremadoDetalleResponse> detalles = recepciones.stream()
                .map(this::toRecepcionDescremadoDetalle)
                .toList();

        BigDecimal litrosRecibidos = detalles.stream()
                .map(RecepcionDescremadoDetalleResponse::litrosRecibidos)
                .map(this::valor)
                .reduce(CERO, BigDecimal::add);
        BigDecimal litrosRemision = detalles.stream()
                .map(RecepcionDescremadoDetalleResponse::litrosRemision)
                .map(this::valor)
                .reduce(CERO, BigDecimal::add);
        BigDecimal litrosDescremados = detalles.stream()
                .flatMap(detalle -> detalle.descremados().stream())
                .map(DescremadoRecepcionDetalleResponse::litrosDescremados)
                .map(this::valor)
                .reduce(CERO, BigDecimal::add);
        BigDecimal cremaObtenidaKg = detalles.stream()
                .flatMap(detalle -> detalle.descremados().stream())
                .map(DescremadoRecepcionDetalleResponse::cremaObtenidaKg)
                .map(this::valor)
                .reduce(CERO, BigDecimal::add);
        int unidadesCrema = detalles.stream()
                .flatMap(detalle -> detalle.descremados().stream())
                .map(DescremadoRecepcionDetalleResponse::unidadesCrema)
                .mapToInt(this::valor)
                .sum();
        int descremados = detalles.stream()
                .mapToInt(detalle -> detalle.descremados().size())
                .sum();

        TotalesRecepcionDescremadoResponse totales = new TotalesRecepcionDescremadoResponse(
                detalles.size(),
                (int) detalles.stream().map(RecepcionDescremadoDetalleResponse::proveedor).distinct().count(),
                descremados,
                litrosRecibidos,
                litrosRemision,
                litrosDescremados,
                cremaObtenidaKg,
                unidadesCrema);

        return new ReporteRecepcionDescremadoLacteoResponse(fechaConsulta, totales, detalles);
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

    private RecepcionDescremadoDetalleResponse toRecepcionDescremadoDetalle(RecepcionLecheEntity entity) {
        List<PesajeRecepcionResponse> pesajes = recepcionLechePesajeRepository
                .findByRecepcionLecheIdOrderByNumeroPesajeAsc(entity.getId())
                .stream()
                .map(this::toPesajeRecepcion)
                .toList();
        List<DescremadoRecepcionDetalleResponse> descremados = descremadoRecepcionRepository
                .findByRecepcionLecheIdOrderByIdDesc(entity.getId())
                .stream()
                .map(this::toDescremadoRecepcionDetalle)
                .toList();

        return new RecepcionDescremadoDetalleResponse(
                entity.getId(),
                entity.getFechaRecepcion(),
                entity.getProveedor(),
                entity.getTipoMateriaPrima(),
                entity.getCantidadRecibidaLitros(),
                entity.getCantidadRemisionLitros(),
                entity.getNumeroRemision(),
                entity.getTanque().getId(),
                entity.getTanque().getNombre(),
                entity.getRecibidoPor(),
                entity.getObservaciones(),
                pesajes,
                descremados);
    }

    private PesajeRecepcionResponse toPesajeRecepcion(RecepcionLechePesajeEntity entity) {
        return new PesajeRecepcionResponse(
                entity.getId(),
                entity.getNumeroPesaje(),
                entity.getPesoBrutoKg(),
                entity.getTaraKg(),
                entity.getPesoNetoKg(),
                entity.getObservaciones());
    }

    private DescremadoRecepcionDetalleResponse toDescremadoRecepcionDetalle(DescremadoRecepcionEntity entity) {
        return new DescremadoRecepcionDetalleResponse(
                entity.getId(),
                entity.getLitrosDescremados(),
                entity.getCremaObtenidaKg(),
                entity.getSkuCrema() != null ? entity.getSkuCrema().getId() : null,
                entity.getSkuCrema() != null ? entity.getSkuCrema().getDescripcion() : null,
                entity.getUnidadesCrema(),
                entity.getKgPorUnidadCrema(),
                entity.getLoteCrema(),
                entity.getTanqueDestino() != null ? entity.getTanqueDestino().getId() : null,
                entity.getTanqueDestino() != null ? entity.getTanqueDestino().getNombre() : null,
                toMovimientoLeche(entity.getMovimientoSalida()),
                toMovimientoLeche(entity.getMovimientoEntrada()),
                entity.getObservaciones());
    }

    private MovimientoLecheReporteResponse toMovimientoLeche(MovimientoLecheEntity entity) {
        if (entity == null) {
            return null;
        }
        return new MovimientoLecheReporteResponse(
                entity.getId(),
                entity.getTanque().getId(),
                entity.getTanque().getNombre(),
                entity.getTipoMovimiento().name(),
                entity.getFechaHora(),
                entity.getCantidadLitros(),
                entity.getSaldoResultanteLitros(),
                entity.getReferencia(),
                entity.getObservaciones());
    }

    private BigDecimal valor(BigDecimal valor) {
        return valor != null ? valor : CERO;
    }

    private int valor(Integer valor) {
        return valor != null ? valor : 0;
    }

    private String nombreUsuario(UsuarioEntity usuario) {
        String segundoNombre = usuario.getSegundoNombre() != null ? " " + usuario.getSegundoNombre() : "";
        String segundoApellido = usuario.getSegundoApellido() != null ? " " + usuario.getSegundoApellido() : "";
        return (usuario.getPrimerNombre() + segundoNombre + " " + usuario.getPrimerApellido() + segundoApellido).trim();
    }
}

package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.response.DashboardOperativoLacteoResponse;
import com.yerman.produccion_api.application.dto.response.DashboardOperativoLacteoResponse.BatchOperativoResponse;
import com.yerman.produccion_api.application.dto.response.DashboardOperativoLacteoResponse.DescremadoOperativoResponse;
import com.yerman.produccion_api.application.dto.response.DashboardOperativoLacteoResponse.EmpaqueOperativoResponse;
import com.yerman.produccion_api.application.dto.response.DashboardOperativoLacteoResponse.MedicionCalidadOperativaResponse;
import com.yerman.produccion_api.application.dto.response.DashboardOperativoLacteoResponse.MovimientoLecheOperativoResponse;
import com.yerman.produccion_api.application.dto.response.DashboardOperativoLacteoResponse.ProduccionOperativaResponse;
import com.yerman.produccion_api.application.dto.response.DashboardOperativoLacteoResponse.ProductoTerminadoOperativoResponse;
import com.yerman.produccion_api.application.dto.response.DashboardOperativoLacteoResponse.RecepcionOperativaResponse;
import com.yerman.produccion_api.application.dto.response.DashboardOperativoLacteoResponse.RegistroInsumoOperativoResponse;
import com.yerman.produccion_api.application.dto.response.DashboardOperativoLacteoResponse.SaldoTanqueResponse;
import com.yerman.produccion_api.application.dto.response.DashboardOperativoLacteoResponse.TotalesOperativosResponse;
import com.yerman.produccion_api.domain.model.SaldoTanqueLeche;
import com.yerman.produccion_api.domain.port.in.ConsultaSaldoTanqueLecheUseCase;
import com.yerman.produccion_api.infrastructure.entity.DescremadoRecepcionEntity;
import com.yerman.produccion_api.infrastructure.entity.EmpaqueLacteoEntity;
import com.yerman.produccion_api.infrastructure.entity.MedicionCalidadLacteaEntity;
import com.yerman.produccion_api.infrastructure.entity.MovimientoLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.ProduccionLacteaBatchEntity;
import com.yerman.produccion_api.infrastructure.entity.ProduccionLacteaEntity;
import com.yerman.produccion_api.infrastructure.entity.ProductoTerminadoLacteoEntity;
import com.yerman.produccion_api.infrastructure.entity.RecepcionLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.RegistroInsumoLacteoEntity;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.repository.DescremadoRecepcionJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.EmpaqueLacteoJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.MedicionCalidadLacteaJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.MovimientoLecheJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ProduccionLacteaBatchJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ProduccionLacteaJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ProductoTerminadoLacteoJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.RecepcionLecheJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.RegistroInsumoLacteoJpaRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/dashboard-operativo")
public class DashboardOperativoLacteoController {

    private static final BigDecimal CERO = BigDecimal.ZERO;

    private final ConsultaSaldoTanqueLecheUseCase consultaSaldoTanqueLecheUseCase;
    private final RecepcionLecheJpaRepository recepcionRepository;
    private final DescremadoRecepcionJpaRepository descremadoRepository;
    private final ProduccionLacteaJpaRepository produccionRepository;
    private final ProduccionLacteaBatchJpaRepository batchRepository;
    private final MedicionCalidadLacteaJpaRepository medicionRepository;
    private final RegistroInsumoLacteoJpaRepository registroInsumoRepository;
    private final ProductoTerminadoLacteoJpaRepository productoTerminadoRepository;
    private final EmpaqueLacteoJpaRepository empaqueRepository;
    private final MovimientoLecheJpaRepository movimientoLecheRepository;

    public DashboardOperativoLacteoController(
            ConsultaSaldoTanqueLecheUseCase consultaSaldoTanqueLecheUseCase,
            RecepcionLecheJpaRepository recepcionRepository,
            DescremadoRecepcionJpaRepository descremadoRepository,
            ProduccionLacteaJpaRepository produccionRepository,
            ProduccionLacteaBatchJpaRepository batchRepository,
            MedicionCalidadLacteaJpaRepository medicionRepository,
            RegistroInsumoLacteoJpaRepository registroInsumoRepository,
            ProductoTerminadoLacteoJpaRepository productoTerminadoRepository,
            EmpaqueLacteoJpaRepository empaqueRepository,
            MovimientoLecheJpaRepository movimientoLecheRepository) {
        this.consultaSaldoTanqueLecheUseCase = consultaSaldoTanqueLecheUseCase;
        this.recepcionRepository = recepcionRepository;
        this.descremadoRepository = descremadoRepository;
        this.produccionRepository = produccionRepository;
        this.batchRepository = batchRepository;
        this.medicionRepository = medicionRepository;
        this.registroInsumoRepository = registroInsumoRepository;
        this.productoTerminadoRepository = productoTerminadoRepository;
        this.empaqueRepository = empaqueRepository;
        this.movimientoLecheRepository = movimientoLecheRepository;
    }

    @GetMapping("/lacteos")
    @Transactional(readOnly = true)
    public DashboardOperativoLacteoResponse consultarLacteos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDate fechaConsulta = fecha != null ? fecha : LocalDate.now();

        List<RecepcionLecheEntity> recepciones = recepcionRepository.findByFechaRecepcionOrderByIdDesc(fechaConsulta);
        List<DescremadoRecepcionEntity> descremados =
                descremadoRepository.findByRecepcionLecheFechaRecepcionOrderByIdDesc(fechaConsulta);
        List<ProduccionLacteaEntity> producciones = produccionRepository.findByFechaProduccion(fechaConsulta);
        List<ProductoTerminadoLacteoEntity> productosTerminados =
                productoTerminadoRepository.findByProduccionLacteaBatch_Produccion_FechaProduccionOrderByIdDesc(
                        fechaConsulta);
        List<EmpaqueLacteoEntity> empaques = empaqueRepository.findByFechaEmpaqueOrderByIdDesc(fechaConsulta);
        List<MovimientoLecheEntity> movimientos = movimientoLecheRepository.findByFechaHoraBetweenOrderByFechaHoraDescIdDesc(
                fechaConsulta.atStartOfDay(), fechaConsulta.atTime(LocalTime.MAX));

        List<SaldoTanqueResponse> saldos = consultaSaldoTanqueLecheUseCase.listarSaldos().stream()
                .map(this::toSaldoResponse)
                .toList();
        List<ProduccionOperativaResponse> produccionesResponse = producciones.stream()
                .map(this::toProduccionResponse)
                .toList();

        TotalesOperativosResponse totales = new TotalesOperativosResponse(
                sumarRecepciones(recepciones),
                sumarDescremadosLitros(descremados),
                sumarDescremadosCrema(descremados),
                produccionesResponse.stream().map(ProduccionOperativaResponse::litrosConsumidos).reduce(CERO, BigDecimal::add),
                produccionesResponse.stream().map(ProduccionOperativaResponse::kilosProducidos).reduce(CERO, BigDecimal::add),
                productosTerminados.stream().map(ProductoTerminadoLacteoEntity::getKilosProducidos)
                        .map(this::valor).reduce(CERO, BigDecimal::add),
                empaques.stream().map(EmpaqueLacteoEntity::getUnidades).mapToInt(this::valor).sum(),
                recepciones.size(),
                producciones.size(),
                produccionesResponse.stream().mapToInt(produccion -> produccion.insumos().size()).sum(),
                produccionesResponse.stream().mapToInt(produccion -> produccion.medicionesCalidad().size()).sum());

        return new DashboardOperativoLacteoResponse(
                fechaConsulta,
                totales,
                saldos,
                recepciones.stream().map(this::toRecepcionResponse).toList(),
                descremados.stream().map(this::toDescremadoResponse).toList(),
                produccionesResponse,
                productosTerminados.stream().map(this::toProductoTerminadoResponse).toList(),
                empaques.stream().map(this::toEmpaqueResponse).toList(),
                movimientos.stream().map(this::toMovimientoResponse).toList());
    }

    private ProduccionOperativaResponse toProduccionResponse(ProduccionLacteaEntity entity) {
        List<ProduccionLacteaBatchEntity> batches = batchRepository.findByProduccionId(entity.getId());
        List<MedicionCalidadLacteaEntity> mediciones =
                medicionRepository.findByProduccionLacteaIdOrderByFechaHoraMedicionDesc(entity.getId());
        List<RegistroInsumoLacteoEntity> insumos =
                registroInsumoRepository.findByProduccionLacteaIdOrderByFechaHoraRegistroDesc(entity.getId());

        BigDecimal litrosConsumidos = batches.stream()
                .map(ProduccionLacteaBatchEntity::getLitrosConsumidos)
                .map(this::valor)
                .reduce(CERO, BigDecimal::add);
        BigDecimal kilosProducidos = batches.stream()
                .map(ProduccionLacteaBatchEntity::getKilosProducidos)
                .map(this::valor)
                .reduce(CERO, BigDecimal::add);

        return new ProduccionOperativaResponse(
                entity.getId(),
                entity.getFechaProduccion(),
                entity.getProducto(),
                entity.getTanque().getId(),
                entity.getTanque().getNombre(),
                entity.getUsuario().getIdUsuario(),
                nombreUsuario(entity.getUsuario()),
                litrosConsumidos,
                kilosProducidos,
                batches.stream().map(this::toBatchResponse).toList(),
                mediciones.stream().map(this::toMedicionResponse).toList(),
                insumos.stream().map(this::toRegistroInsumoResponse).toList(),
                entity.getObservaciones());
    }

    private SaldoTanqueResponse toSaldoResponse(SaldoTanqueLeche saldo) {
        return new SaldoTanqueResponse(
                saldo.getIdTanque(),
                saldo.getNombre(),
                saldo.getTipo().name(),
                saldo.getSaldoLitros(),
                saldo.getActivo());
    }

    private RecepcionOperativaResponse toRecepcionResponse(RecepcionLecheEntity entity) {
        return new RecepcionOperativaResponse(
                entity.getId(),
                entity.getFechaRecepcion(),
                entity.getProveedor(),
                entity.getTipoMateriaPrima(),
                entity.getCantidadRecibidaLitros(),
                entity.getCantidadRemisionLitros(),
                entity.getTanque().getId(),
                entity.getTanque().getNombre(),
                entity.getNumeroRemision(),
                entity.getRecibidoPor(),
                entity.getObservaciones());
    }

    private DescremadoOperativoResponse toDescremadoResponse(DescremadoRecepcionEntity entity) {
        return new DescremadoOperativoResponse(
                entity.getId(),
                entity.getRecepcionLeche().getId(),
                entity.getRecepcionLeche().getProveedor(),
                entity.getLitrosDescremados(),
                entity.getCremaObtenidaKg(),
                entity.getTanqueDestino() != null ? entity.getTanqueDestino().getId() : null,
                entity.getTanqueDestino() != null ? entity.getTanqueDestino().getNombre() : null,
                entity.getSkuCrema() != null ? entity.getSkuCrema().getId() : null,
                entity.getSkuCrema() != null ? entity.getSkuCrema().getDescripcion() : null,
                entity.getUnidadesCrema(),
                entity.getKgPorUnidadCrema(),
                entity.getLoteCrema(),
                entity.getObservaciones());
    }

    private BatchOperativoResponse toBatchResponse(ProduccionLacteaBatchEntity entity) {
        return new BatchOperativoResponse(
                entity.getId(),
                entity.getNumeroBatch(),
                entity.getMarmita().getId(),
                entity.getMarmita().getNombre(),
                entity.getLitrosConsumidos(),
                entity.getKilosProducidos(),
                entity.getRendimiento(),
                entity.getObservaciones());
    }

    private MedicionCalidadOperativaResponse toMedicionResponse(MedicionCalidadLacteaEntity entity) {
        return new MedicionCalidadOperativaResponse(
                entity.getId(),
                entity.getProduccionLacteaBatch() != null ? entity.getProduccionLacteaBatch().getId() : null,
                entity.getTipoMedicion().name(),
                entity.getReferencia(),
                entity.getBrix(),
                entity.getPh(),
                entity.getFechaHoraMedicion(),
                entity.getObservaciones());
    }

    private RegistroInsumoOperativoResponse toRegistroInsumoResponse(RegistroInsumoLacteoEntity entity) {
        return new RegistroInsumoOperativoResponse(
                entity.getId(),
                entity.getProduccionLacteaBatch() != null ? entity.getProduccionLacteaBatch().getId() : null,
                entity.getInsumo().getId(),
                entity.getInsumo().getNombre(),
                entity.getLoteInsumo(),
                entity.getCantidadRequerida(),
                entity.getCantidadUsada(),
                entity.getUnidadMedida(),
                entity.getFechaHoraRegistro(),
                entity.getObservaciones());
    }

    private ProductoTerminadoOperativoResponse toProductoTerminadoResponse(ProductoTerminadoLacteoEntity entity) {
        return new ProductoTerminadoOperativoResponse(
                entity.getId(),
                entity.getProduccionLacteaBatch().getId(),
                entity.getProducto(),
                entity.getLote(),
                entity.getKilosProducidos(),
                entity.getKilosDisponibles(),
                entity.getEstado().name(),
                entity.getObservaciones());
    }

    private EmpaqueOperativoResponse toEmpaqueResponse(EmpaqueLacteoEntity entity) {
        return new EmpaqueOperativoResponse(
                entity.getId(),
                entity.getProductoTerminadoLacteo().getId(),
                entity.getBatch().getId(),
                entity.getSku() != null ? entity.getSku().getId() : null,
                entity.getSku() != null ? entity.getSku().getDescripcion() : null,
                entity.getLoteEmpaque(),
                entity.getFechaEmpaque(),
                entity.getFechaVencimiento(),
                entity.getKilosUtilizados(),
                entity.getUnidades(),
                entity.getCajas(),
                entity.getPesoTotalKg(),
                entity.getEstado(),
                entity.getObservaciones());
    }

    private MovimientoLecheOperativoResponse toMovimientoResponse(MovimientoLecheEntity entity) {
        return new MovimientoLecheOperativoResponse(
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

    private BigDecimal sumarRecepciones(List<RecepcionLecheEntity> recepciones) {
        return recepciones.stream()
                .map(RecepcionLecheEntity::getCantidadRecibidaLitros)
                .map(this::valor)
                .reduce(CERO, BigDecimal::add);
    }

    private BigDecimal sumarDescremadosLitros(List<DescremadoRecepcionEntity> descremados) {
        return descremados.stream()
                .map(DescremadoRecepcionEntity::getLitrosDescremados)
                .map(this::valor)
                .reduce(CERO, BigDecimal::add);
    }

    private BigDecimal sumarDescremadosCrema(List<DescremadoRecepcionEntity> descremados) {
        return descremados.stream()
                .map(DescremadoRecepcionEntity::getCremaObtenidaKg)
                .map(this::valor)
                .reduce(CERO, BigDecimal::add);
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

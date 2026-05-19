package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.dto.response.DashboardGerencialResponse;
import com.yerman.produccion_api.application.dto.response.DashboardProduccionSkuResponse;
import com.yerman.produccion_api.application.dto.response.DashboardProduccionVsEmpaqueResponse;
import com.yerman.produccion_api.application.dto.response.DashboardTrazabilidadLoteResponse;
import com.yerman.produccion_api.application.dto.response.RendimientoAnualResponse;
import com.yerman.produccion_api.domain.model.TipoMovimientoLeche;
import com.yerman.produccion_api.domain.port.out.DashboardRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.DescremadoRecepcionEntity;
import com.yerman.produccion_api.infrastructure.entity.MovimientoLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.RecepcionLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.ReporteProduccionDiariaEntity;
import com.yerman.produccion_api.infrastructure.repository.DashboardJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.DescremadoRecepcionJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.MovimientoLecheJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.RecepcionLecheJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ReporteProduccionDiariaJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardProduccionVsEmpaqueProjection;
import com.yerman.produccion_api.infrastructure.repository.projection.DashboardTrazabilidadLoteProjection;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
public class DashboardJpaAdapter implements DashboardRepositoryPort {

    private static final BigDecimal CERO = BigDecimal.ZERO;
    private static final BigDecimal CIEN = new BigDecimal("100");
    private static final BigDecimal LITROS_POR_BOLSA_CREMA = new BigDecimal("18");

    private final DashboardJpaRepository repository;
    private final ReporteProduccionDiariaJpaRepository reporteRepo;
    private final RecepcionLecheJpaRepository recepcionRepo;
    private final DescremadoRecepcionJpaRepository descremadoRepo;
    private final MovimientoLecheJpaRepository movimientoRepo;

    public DashboardJpaAdapter(
            DashboardJpaRepository repository,
            ReporteProduccionDiariaJpaRepository reporteRepo,
            RecepcionLecheJpaRepository recepcionRepo,
            DescremadoRecepcionJpaRepository descremadoRepo,
            MovimientoLecheJpaRepository movimientoRepo) {
        this.repository = repository;
        this.reporteRepo = reporteRepo;
        this.recepcionRepo = recepcionRepo;
        this.descremadoRepo = descremadoRepo;
        this.movimientoRepo = movimientoRepo;
    }

    @Override
    public List<DashboardProduccionVsEmpaqueResponse> obtenerProduccionVsEmpaque() {
        List<DashboardProduccionVsEmpaqueProjection> real = repository.obtenerProduccionVsEmpaque();
        return real.stream()
                .map(p -> {
                    BigDecimal prod = valor(p.getTotalProducido());
                    BigDecimal emp = valor(p.getTotalEmpaquetado());
                    return new DashboardProduccionVsEmpaqueResponse(
                            p.getFecha(),
                            prod,
                            emp,
                            prod.subtract(emp));
                })
                .toList();
    }

    @Override
    public DashboardTrazabilidadLoteResponse obtenerTrazabilidadPorLote(String lote) {
        DashboardTrazabilidadLoteProjection p = repository
                .obtenerTrazabilidadPorLote(lote)
                .orElseThrow(() -> new RuntimeException("Lote no encontrado"));

        return new DashboardTrazabilidadLoteResponse(
                p.getLote(),
                p.getProducto(),
                p.getFechaProduccion(),
                p.getNumeroBatch(),
                p.getKilosProducidos(),
                p.getKilosDisponibles(),
                p.getKilosEmpacados(),
                p.getEstadoProductoTerminado());
    }

    @Override
    public List<DashboardProduccionSkuResponse> obtenerProduccionSkuMensual(int mes, int anio) {
        LocalDate inicio = LocalDate.of(anio, mes, 1);
        LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());

        List<ReporteProduccionDiariaEntity> registros = reporteRepo.findByFechaBetweenOrderByFechaAsc(inicio, fin);
        Map<ReporteProduccionDiariaEntity.TipoProducto, Map<String, List<ReporteProduccionDiariaEntity>>> agrupado =
                registros.stream()
                        .collect(Collectors.groupingBy(
                                ReporteProduccionDiariaEntity::getTipoProducto,
                                Collectors.groupingBy(ReporteProduccionDiariaEntity::getSkuDescripcion, TreeMap::new,
                                        Collectors.toList())));

        List<DashboardProduccionSkuResponse> respuesta = new ArrayList<>();
        for (ReporteProduccionDiariaEntity.TipoProducto tipo : List.of(
                ReporteProduccionDiariaEntity.TipoProducto.LC,
                ReporteProduccionDiariaEntity.TipoProducto.DL,
                ReporteProduccionDiariaEntity.TipoProducto.OTRO)) {
            Map<String, List<ReporteProduccionDiariaEntity>> porSku = agrupado.get(tipo);
            if (porSku == null || porSku.isEmpty()) {
                continue;
            }

            List<DashboardProduccionSkuResponse.SkuProduccionMensual> items = porSku.entrySet().stream()
                    .map(entry -> construirSkuMensual(entry.getKey(), entry.getValue()))
                    .sorted(Comparator.comparing(DashboardProduccionSkuResponse.SkuProduccionMensual::kilosMes)
                            .reversed())
                    .toList();
            respuesta.add(new DashboardProduccionSkuResponse(tipo.name(), items));
        }

        return respuesta;
    }

    @Override
    public List<RendimientoAnualResponse> obtenerRendimientoAnual(int anio) {
        List<RendimientoAnualResponse> resultado = new ArrayList<>();

        for (int mes = 1; mes <= 12; mes++) {
            DatosMes datos = cargarDatosMes(mes, anio);
            BigDecimal lecheBase = baseLecheProcesada(datos.lecheProcesada(), datos.lecheRecibida());

            resultado.add(new RendimientoAnualResponse(
                    mes,
                    datos.ptDulceLeche().setScale(1, RoundingMode.HALF_UP),
                    datos.ptLecheCondensada().setScale(1, RoundingMode.HALF_UP),
                    datos.lecheRecibida().setScale(0, RoundingMode.HALF_UP),
                    calcularRendimiento(datos.ptDulceLeche(), lecheBase),
                    calcularRendimiento(datos.ptLecheCondensada(), lecheBase)));
        }

        return resultado;
    }

    @Override
    public DashboardGerencialResponse.ResumenMesGerencial obtenerResumenMensualGerencial(int mes, int anio) {
        DatosMes datos = cargarDatosMes(mes, anio);
        BigDecimal kgCremaEsperada = datos.lecheRecibida()
                .divide(new BigDecimal("3000"), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("131"))
                .setScale(2, RoundingMode.HALF_UP);

        return new DashboardGerencialResponse.ResumenMesGerencial(
                datos.lecheRecibida().setScale(1, RoundingMode.HALF_UP),
                datos.kgCrema().setScale(1, RoundingMode.HALF_UP),
                kgCremaEsperada,
                datos.ptDulceLeche().setScale(1, RoundingMode.HALF_UP),
                datos.ptLecheCondensada().setScale(1, RoundingMode.HALF_UP));
    }

    @Override
    public List<DashboardGerencialResponse.SemanaGerencialResponse> obtenerDetalleSemanalGerencial(int mes, int anio) {
        LocalDate inicio = LocalDate.of(anio, mes, 1);
        LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());
        Map<Integer, SemanaAcumulada> semanas = inicializarSemanas();

        List<RecepcionLecheEntity> recepciones =
                recepcionRepo.findByFechaRecepcionBetweenOrderByFechaRecepcionAscIdAsc(inicio, fin);
        recepciones.forEach(r -> {
            int semana = semanaDeMes(r.getFechaRecepcion());
            SemanaAcumulada acumulada = semanas.get(semana);
            acumulada.lecheRecibida = acumulada.lecheRecibida.add(valor(r.getCantidadRecibidaLitros()));
            acumulada.kgCrema = acumulada.kgCrema.add(extraerKgCremaHistorica(r.getObservaciones()));
        });

        List<DescremadoRecepcionEntity> descremados =
                descremadoRepo.findByRecepcionLecheFechaRecepcionBetweenOrderByRecepcionLecheFechaRecepcionAscIdAsc(
                        inicio, fin);
        descremados.forEach(d -> {
            int semana = semanaDeMes(d.getRecepcionLeche().getFechaRecepcion());
            SemanaAcumulada acumulada = semanas.get(semana);
            acumulada.kgCrema = acumulada.kgCrema.add(valor(d.getCremaObtenidaKg()));
            acumulada.litrosCrema = acumulada.litrosCrema.add(calcularLitrosCrema(d));
        });

        List<MovimientoLecheEntity> movimientos = movimientoRepo.findByFechaHoraBetweenOrderByFechaHoraDescIdDesc(
                inicio.atStartOfDay(), LocalDateTime.of(fin, LocalTime.MAX));
        movimientos.stream()
                .filter(m -> m.getTipoMovimiento() == TipoMovimientoLeche.SALIDA_PRODUCCION)
                .forEach(m -> {
                    int semana = semanaDeMes(m.getFechaHora().toLocalDate());
                    SemanaAcumulada acumulada = semanas.get(semana);
                    acumulada.lecheProcesada = acumulada.lecheProcesada.add(valor(m.getCantidadLitros()));
                });

        List<ReporteProduccionDiariaEntity> reportes = reporteRepo.findByFechaBetweenOrderByFechaAsc(inicio, fin);
        reportes.forEach(r -> {
            int semana = semanaDeMes(r.getFecha());
            SemanaAcumulada acumulada = semanas.get(semana);
            if (r.getTipoProducto() == ReporteProduccionDiariaEntity.TipoProducto.DL) {
                acumulada.ptDulceLeche = acumulada.ptDulceLeche.add(valor(r.getKgPtReales()));
            } else if (r.getTipoProducto() == ReporteProduccionDiariaEntity.TipoProducto.LC) {
                acumulada.ptLecheCondensada = acumulada.ptLecheCondensada.add(valor(r.getKgPtReales()));
            }
        });

        repository.obtenerReprocesoSemanalGerencial(mes, anio)
                .forEach(row -> {
                    int semana = ((Number) row[0]).intValue();
                    if (semanas.containsKey(semana)) {
                        semanas.get(semana).kgReproceso = valor(row[1]);
                    }
                });

        boolean sinMovimientosProduccion = semanas.values().stream()
                .map(s -> s.lecheProcesada)
                .reduce(CERO, BigDecimal::add)
                .compareTo(CERO) == 0;

        return semanas.entrySet().stream()
                .map(entry -> construirSemana(entry.getKey(), entry.getValue(), inicio, sinMovimientosProduccion))
                .filter(s -> tieneDatosSemana(s))
                .toList();
    }

    private DashboardProduccionSkuResponse.SkuProduccionMensual construirSkuMensual(
            String sku,
            List<ReporteProduccionDiariaEntity> registros) {
        int pesoNeto = registros.stream()
                .map(ReporteProduccionDiariaEntity::getPesoNetoGr)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(0);
        int unidades = registros.stream()
                .map(ReporteProduccionDiariaEntity::getUnidadesReales)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();
        BigDecimal kilos = registros.stream()
                .map(ReporteProduccionDiariaEntity::getKgPtReales)
                .map(DashboardJpaAdapter::valor)
                .reduce(CERO, BigDecimal::add);
        List<DashboardProduccionSkuResponse.ProduccionDiariaSku> detalle = registros.stream()
                .sorted(Comparator.comparing(ReporteProduccionDiariaEntity::getFecha))
                .map(r -> new DashboardProduccionSkuResponse.ProduccionDiariaSku(
                        r.getFecha(),
                        r.getUnidadesReales(),
                        valor(r.getKgPtReales())))
                .toList();

        return new DashboardProduccionSkuResponse.SkuProduccionMensual(
                sku,
                BigDecimal.valueOf(pesoNeto),
                unidades,
                kilos.setScale(1, RoundingMode.HALF_UP),
                detalle);
    }

    private DatosMes cargarDatosMes(int mes, int anio) {
        LocalDate inicio = LocalDate.of(anio, mes, 1);
        LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());

        List<RecepcionLecheEntity> recepciones =
                recepcionRepo.findByFechaRecepcionBetweenOrderByFechaRecepcionAscIdAsc(inicio, fin);
        List<DescremadoRecepcionEntity> descremados =
                descremadoRepo.findByRecepcionLecheFechaRecepcionBetweenOrderByRecepcionLecheFechaRecepcionAscIdAsc(
                        inicio, fin);
        List<MovimientoLecheEntity> movimientos = movimientoRepo.findByFechaHoraBetweenOrderByFechaHoraDescIdDesc(
                inicio.atStartOfDay(), LocalDateTime.of(fin, LocalTime.MAX));
        List<ReporteProduccionDiariaEntity> reportes = reporteRepo.findByFechaBetweenOrderByFechaAsc(inicio, fin);

        BigDecimal lecheRecibida = recepciones.stream()
                .map(RecepcionLecheEntity::getCantidadRecibidaLitros)
                .map(DashboardJpaAdapter::valor)
                .reduce(CERO, BigDecimal::add);
        BigDecimal kgCrema = descremados.stream()
                .map(DescremadoRecepcionEntity::getCremaObtenidaKg)
                .map(DashboardJpaAdapter::valor)
                .reduce(CERO, BigDecimal::add)
                .add(recepciones.stream()
                        .map(RecepcionLecheEntity::getObservaciones)
                        .map(this::extraerKgCremaHistorica)
                        .reduce(CERO, BigDecimal::add));
        BigDecimal litrosCrema = descremados.stream()
                .map(this::calcularLitrosCrema)
                .reduce(CERO, BigDecimal::add);
        BigDecimal lecheProcesada = movimientos.stream()
                .filter(m -> m.getTipoMovimiento() == TipoMovimientoLeche.SALIDA_PRODUCCION)
                .map(MovimientoLecheEntity::getCantidadLitros)
                .map(DashboardJpaAdapter::valor)
                .reduce(CERO, BigDecimal::add);
        BigDecimal ptDL = reportes.stream()
                .filter(r -> r.getTipoProducto() == ReporteProduccionDiariaEntity.TipoProducto.DL)
                .map(ReporteProduccionDiariaEntity::getKgPtReales)
                .map(DashboardJpaAdapter::valor)
                .reduce(CERO, BigDecimal::add);
        BigDecimal ptLC = reportes.stream()
                .filter(r -> r.getTipoProducto() == ReporteProduccionDiariaEntity.TipoProducto.LC)
                .map(ReporteProduccionDiariaEntity::getKgPtReales)
                .map(DashboardJpaAdapter::valor)
                .reduce(CERO, BigDecimal::add);
        BigDecimal reproceso = valor(repository.obtenerReprocesoMensualGerencial(mes, anio));

        return new DatosMes(lecheRecibida, litrosCrema, kgCrema, lecheProcesada, ptDL, reproceso, ptLC);
    }

    private DashboardGerencialResponse.SemanaGerencialResponse construirSemana(
            Integer numeroSemana,
            SemanaAcumulada s,
            LocalDate inicioMes,
            boolean usarRecepcionComoHistorico) {
        BigDecimal lecheBase = usarRecepcionComoHistorico ? s.lecheRecibida : s.lecheProcesada;
        BigDecimal rend1DL = calcularRendimiento(s.ptDulceLeche, lecheBase);
        BigDecimal rend2DL = calcularRendimiento(s.ptDulceLeche, lecheBase.add(s.kgReproceso));
        BigDecimal rendLC = calcularRendimiento(s.ptLecheCondensada, lecheBase);

        return new DashboardGerencialResponse.SemanaGerencialResponse(
                numeroSemana,
                calcularRangoSemana(numeroSemana, inicioMes),
                s.lecheRecibida.setScale(0, RoundingMode.HALF_UP),
                s.litrosCrema.setScale(1, RoundingMode.HALF_UP),
                s.kgCrema.setScale(1, RoundingMode.HALF_UP),
                lecheBase.setScale(0, RoundingMode.HALF_UP),
                s.ptDulceLeche.setScale(1, RoundingMode.HALF_UP),
                s.kgReproceso.setScale(1, RoundingMode.HALF_UP),
                rend1DL,
                rend2DL,
                s.ptLecheCondensada.setScale(1, RoundingMode.HALF_UP),
                rendLC);
    }

    private boolean tieneDatosSemana(DashboardGerencialResponse.SemanaGerencialResponse s) {
        return s.lecheRecibida().compareTo(CERO) > 0
                || s.lecheProcesada().compareTo(CERO) > 0
                || s.kgCrema().compareTo(CERO) > 0
                || s.ptDulceLeche().compareTo(CERO) > 0
                || s.ptLecheCondensada().compareTo(CERO) > 0;
    }

    private BigDecimal baseLecheProcesada(BigDecimal lecheProcesada, BigDecimal lecheRecibida) {
        return lecheProcesada.compareTo(CERO) > 0 ? lecheProcesada : lecheRecibida;
    }

    private BigDecimal calcularRendimiento(BigDecimal produccionKg, BigDecimal lecheBase) {
        if (lecheBase == null || lecheBase.compareTo(CERO) <= 0) {
            return CERO;
        }
        return valor(produccionKg).multiply(CIEN).divide(lecheBase, 2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularLitrosCrema(DescremadoRecepcionEntity descremado) {
        if (descremado.getUnidadesCrema() == null || descremado.getUnidadesCrema() <= 0) {
            return CERO;
        }
        return BigDecimal.valueOf(descremado.getUnidadesCrema()).multiply(LITROS_POR_BOLSA_CREMA);
    }

    private BigDecimal extraerKgCremaHistorica(String observaciones) {
        if (observaciones == null || !observaciones.contains("kg_crema=")) {
            return CERO;
        }
        try {
            String valor = observaciones.substring(observaciones.indexOf("kg_crema=") + "kg_crema=".length());
            int coma = valor.indexOf(',');
            if (coma >= 0) {
                valor = valor.substring(0, coma);
            }
            return new BigDecimal(valor.trim());
        } catch (RuntimeException ex) {
            return CERO;
        }
    }

    private Map<Integer, SemanaAcumulada> inicializarSemanas() {
        Map<Integer, SemanaAcumulada> semanas = new TreeMap<>();
        for (int i = 1; i <= 5; i++) {
            semanas.put(i, new SemanaAcumulada());
        }
        return semanas;
    }

    private int semanaDeMes(LocalDate fecha) {
        int dia = fecha.getDayOfMonth();
        if (dia <= 7) {
            return 1;
        }
        if (dia <= 14) {
            return 2;
        }
        if (dia <= 21) {
            return 3;
        }
        if (dia <= 28) {
            return 4;
        }
        return 5;
    }

    private String calcularRangoSemana(int semana, LocalDate inicioMes) {
        int diaInicio = (semana - 1) * 7 + 1;
        int diaFin = Math.min(semana * 7, inicioMes.lengthOfMonth());
        return inicioMes.withDayOfMonth(diaInicio) + " al " + inicioMes.withDayOfMonth(diaFin);
    }

    private static BigDecimal valor(BigDecimal valor) {
        return valor != null ? valor : CERO;
    }

    private static BigDecimal valor(Object valor) {
        return valor != null ? new BigDecimal(valor.toString()) : CERO;
    }

    private record DatosMes(
            BigDecimal lecheRecibida,
            BigDecimal litrosCrema,
            BigDecimal kgCrema,
            BigDecimal lecheProcesada,
            BigDecimal ptDulceLeche,
            BigDecimal kgReproceso,
            BigDecimal ptLecheCondensada) {
    }

    private static class SemanaAcumulada {
        private BigDecimal lecheRecibida = CERO;
        private BigDecimal litrosCrema = CERO;
        private BigDecimal kgCrema = CERO;
        private BigDecimal lecheProcesada = CERO;
        private BigDecimal ptDulceLeche = CERO;
        private BigDecimal kgReproceso = CERO;
        private BigDecimal ptLecheCondensada = CERO;
    }
}

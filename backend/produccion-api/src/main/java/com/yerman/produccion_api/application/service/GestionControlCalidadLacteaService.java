package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.dto.request.CalidadRecepcionLecheRequest;
import com.yerman.produccion_api.application.dto.request.ControlCalidadProcesoRequest;
import com.yerman.produccion_api.application.dto.request.ControlPesoMuestraRequest;
import com.yerman.produccion_api.application.dto.request.ControlPesoProductoRequest;
import com.yerman.produccion_api.application.dto.response.CalidadRecepcionLecheResponse;
import com.yerman.produccion_api.application.dto.response.ControlCalidadProcesoResponse;
import com.yerman.produccion_api.application.dto.response.ControlPesoMuestraResponse;
import com.yerman.produccion_api.application.dto.response.ControlPesoProductoResponse;
import com.yerman.produccion_api.application.dto.response.EstadoCalidadRecepcionResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.infrastructure.entity.CalidadRecepcionLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.CatalogoSkuEntity;
import com.yerman.produccion_api.infrastructure.entity.ControlCalidadProcesoEntity;
import com.yerman.produccion_api.infrastructure.entity.ControlPesoMuestraEntity;
import com.yerman.produccion_api.infrastructure.entity.ControlPesoProductoEntity;
import com.yerman.produccion_api.infrastructure.entity.EjecucionBatchEntity;
import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.RecepcionLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.repository.CalidadRecepcionLecheJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ControlCalidadProcesoJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ControlPesoProductoJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class GestionControlCalidadLacteaService {

    private final ControlCalidadProcesoJpaRepository procesoRepository;
    private final ControlPesoProductoJpaRepository pesoRepository;
    private final CalidadRecepcionLecheJpaRepository calidadRecepcionRepository;
    private final EntityManager entityManager;
    private final ValidacionOrdenProduccionGuardService validacionGuardService;

    public GestionControlCalidadLacteaService(
            ControlCalidadProcesoJpaRepository procesoRepository,
            ControlPesoProductoJpaRepository pesoRepository,
            CalidadRecepcionLecheJpaRepository calidadRecepcionRepository,
            EntityManager entityManager,
            ValidacionOrdenProduccionGuardService validacionGuardService) {
        this.procesoRepository = procesoRepository;
        this.pesoRepository = pesoRepository;
        this.calidadRecepcionRepository = calidadRecepcionRepository;
        this.entityManager = entityManager;
        this.validacionGuardService = validacionGuardService;
    }

    @Transactional
    public CalidadRecepcionLecheResponse registrarRecepcion(CalidadRecepcionLecheRequest request) {
        if (request.idRecepcionLeche() == null) {
            throw new ReglaNegocioException("La recepcion de leche es obligatoria.");
        }

        RecepcionLecheEntity recepcion = entityManager.find(RecepcionLecheEntity.class, request.idRecepcionLeche());
        if (recepcion == null) {
            throw new RecursoNoEncontradoException(
                    "No existe una recepcion de leche con ID: " + request.idRecepcionLeche());
        }

        if (calidadRecepcionRepository.existsByRecepcionLecheId(request.idRecepcionLeche())) {
            throw new ReglaNegocioException("La recepcion ya tiene control de calidad registrado.");
        }

        CalidadRecepcionLecheEntity entity = new CalidadRecepcionLecheEntity();
        entity.setRecepcionLeche(recepcion);
        entity.setFechaControl(request.fechaControl() != null ? request.fechaControl() : java.time.LocalDateTime.now());
        entity.setPruebaAlcoholOk(request.pruebaAlcoholOk());
        entity.setLactoscanOk(request.lactoscanOk());
        entity.setAcidez(request.acidez());
        entity.setDensidad(request.densidad());
        entity.setGrasa(request.grasa());
        entity.setAguaPct(request.aguaPct());
        entity.setTemperatura(request.temperatura());
        entity.setPh(request.ph());
        entity.setAprobado(!Boolean.FALSE.equals(request.aprobado()));
        entity.setRetenido(Boolean.TRUE.equals(request.retenido()));
        entity.setRealizadoPor(ref(UsuarioEntity.class, request.idRealizadoPor()));
        entity.setObservaciones(request.observaciones());

        return toResponse(calidadRecepcionRepository.save(entity));
    }

    @Transactional
    public CalidadRecepcionLecheResponse actualizarRecepcion(Long id, CalidadRecepcionLecheRequest request) {
        CalidadRecepcionLecheEntity entity = calidadRecepcionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un control de calidad de recepcion con ID: " + id));

        entity.setFechaControl(request.fechaControl() != null ? request.fechaControl() : entity.getFechaControl());
        entity.setPruebaAlcoholOk(request.pruebaAlcoholOk());
        entity.setLactoscanOk(request.lactoscanOk());
        entity.setAcidez(request.acidez());
        entity.setDensidad(request.densidad());
        entity.setGrasa(request.grasa());
        entity.setAguaPct(request.aguaPct());
        entity.setTemperatura(request.temperatura());
        entity.setPh(request.ph());
        entity.setAprobado(!Boolean.FALSE.equals(request.aprobado()));
        entity.setRetenido(Boolean.TRUE.equals(request.retenido()));
        entity.setRealizadoPor(ref(UsuarioEntity.class, request.idRealizadoPor()));
        entity.setObservaciones(request.observaciones());

        return toResponse(calidadRecepcionRepository.save(entity));
    }

    @Transactional
    public void eliminarRecepcion(Long id) {
        if (!calidadRecepcionRepository.existsById(id)) {
            throw new RecursoNoEncontradoException(
                    "No existe un control de calidad de recepcion con ID: " + id);
        }
        calidadRecepcionRepository.deleteById(id);
    }

    public List<CalidadRecepcionLecheResponse> listarRecepcion(Long idRecepcionLeche) {
        if (idRecepcionLeche == null) {
            throw new ReglaNegocioException("La recepcion de leche es obligatoria.");
        }

        return calidadRecepcionRepository.findByRecepcionLecheIdOrderByFechaControlDescIdDesc(idRecepcionLeche)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ControlCalidadProcesoResponse registrarProceso(ControlCalidadProcesoRequest request) {
        validarOrdenYBatch(request.idOrdenProduccion(), request.idEjecucionBatch());
        validarControlProceso(request);

        ControlCalidadProcesoEntity entity = new ControlCalidadProcesoEntity();
        aplicarProceso(entity, request);

        return toResponse(procesoRepository.save(entity));
    }

    @Transactional
    public ControlCalidadProcesoResponse actualizarProceso(Long id, ControlCalidadProcesoRequest request) {
        ControlCalidadProcesoEntity entity = procesoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un control de proceso con ID: " + id));

        validarOrdenYBatch(request.idOrdenProduccion(), request.idEjecucionBatch());
        validarControlProceso(request);
        aplicarProceso(entity, request);

        return toResponse(procesoRepository.save(entity));
    }

    @Transactional
    public void eliminarProceso(Long id) {
        ControlCalidadProcesoEntity entity = procesoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un control de proceso con ID: " + id));
        validacionGuardService.validarOrdenNoAprobada(entity.getOrdenProduccion().getId());
        procesoRepository.delete(entity);
    }

    public List<ControlCalidadProcesoResponse> listarProcesosPorOrden(Long idOrdenProduccion) {
        if (idOrdenProduccion == null) {
            throw new ReglaNegocioException("La orden de produccion es obligatoria.");
        }
        return procesoRepository.findByOrdenProduccionIdOrderByFechaProduccionDescIdDesc(idOrdenProduccion)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ControlPesoProductoResponse registrarPeso(ControlPesoProductoRequest request) {
        validarOrdenYBatch(request.idOrdenProduccion(), request.idEjecucionBatch());
        validarControlPeso(request);

        ControlPesoProductoEntity entity = new ControlPesoProductoEntity();
        aplicarPeso(entity, request);

        return toResponse(pesoRepository.save(entity));
    }

    @Transactional
    public ControlPesoProductoResponse actualizarPeso(Long id, ControlPesoProductoRequest request) {
        ControlPesoProductoEntity entity = pesoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un control de peso con ID: " + id));

        validarOrdenYBatch(request.idOrdenProduccion(), request.idEjecucionBatch());
        validarControlPeso(request);
        aplicarPeso(entity, request);

        return toResponse(pesoRepository.save(entity));
    }

    @Transactional
    public void eliminarPeso(Long id) {
        ControlPesoProductoEntity entity = pesoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un control de peso con ID: " + id));
        validacionGuardService.validarOrdenNoAprobada(entity.getOrdenProduccion().getId());
        pesoRepository.delete(entity);
    }

    public List<ControlPesoProductoResponse> listarPesosPorOrden(Long idOrdenProduccion) {
        if (idOrdenProduccion == null) {
            throw new ReglaNegocioException("La orden de produccion es obligatoria.");
        }
        return pesoRepository.findByOrdenProduccionIdOrderByFechaControlDescIdDesc(idOrdenProduccion)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void aplicarProceso(ControlCalidadProcesoEntity entity, ControlCalidadProcesoRequest request) {
        entity.setOrdenProduccion(ref(OrdenProduccionEntity.class, request.idOrdenProduccion()));
        entity.setEjecucionBatch(request.idEjecucionBatch() != null
                ? ref(EjecucionBatchEntity.class, request.idEjecucionBatch())
                : null);
        entity.setFechaProduccion(request.fechaProduccion());
        entity.setTipoProducto(request.tipoProducto());
        entity.setProducto(request.producto());
        entity.setLote(request.lote());
        entity.setNumeroMarmita(request.numeroMarmita());
        entity.setProductoEnProceso(request.productoEnProceso());
        entity.setPhLeche(request.phLeche());
        entity.setAcidezLeche(request.acidezLeche());
        entity.setDensidadLeche(request.densidadLeche());
        entity.setGrasaLeche(request.grasaLeche());
        entity.setHoraInicioHidrolisis(request.horaInicioHidrolisis());
        entity.setPhInicial(request.phInicial());
        entity.setHoraFinHidrolisis(request.horaFinHidrolisis());
        entity.setTemperaturaInicial(request.temperaturaInicial());
        entity.setTemperaturaFinal(request.temperaturaFinal());
        entity.setAcidezInicial(request.acidezInicial());
        entity.setAcidezFinal(request.acidezFinal());
        entity.setPhFinal(request.phFinal());
        entity.setBrixInicial(request.brixInicial());
        entity.setBrixFinal(request.brixFinal());
        entity.setPresion(request.presion());
        entity.setTemperaturaCoccion(request.temperaturaCoccion());
        entity.setTemperaturaEnvasado(request.temperaturaEnvasado());
        entity.setColorVisual(request.colorVisual());
        entity.setSaborVisual(request.saborVisual());
        entity.setTexturaVisual(request.texturaVisual());
        entity.setPresentacionEnvasado(request.presentacionEnvasado());
        entity.setFechaVencimiento(request.fechaVencimiento());
        entity.setLiberado(Boolean.TRUE.equals(request.liberado()));
        entity.setRetenido(Boolean.TRUE.equals(request.retenido()));
        entity.setRealizadoPor(ref(UsuarioEntity.class, request.idRealizadoPor()));
        entity.setVerificadoPor(request.idVerificadoPor() != null
                ? ref(UsuarioEntity.class, request.idVerificadoPor())
                : null);
        entity.setObservaciones(request.observaciones());
    }

    private void aplicarPeso(ControlPesoProductoEntity entity, ControlPesoProductoRequest request) {
        entity.setOrdenProduccion(ref(OrdenProduccionEntity.class, request.idOrdenProduccion()));
        entity.setEjecucionBatch(request.idEjecucionBatch() != null
                ? ref(EjecucionBatchEntity.class, request.idEjecucionBatch())
                : null);
        entity.setSku(request.idSku() != null ? ref(CatalogoSkuEntity.class, request.idSku()) : null);
        entity.setFechaControl(request.fechaControl());
        entity.setProducto(request.producto());
        entity.setMarca(request.marca());
        entity.setLote(request.lote());
        entity.setFechaVencimiento(request.fechaVencimiento());
        entity.setPresentacion(request.presentacion());
        entity.setNumeroTanda(request.numeroTanda());
        entity.setRangoBatches(request.rangoBatches());
        entity.setPesoBrutoPromedio(request.pesoBrutoPromedio());
        entity.setTaraPromedio(request.taraPromedio());
        entity.setPesoNetoPromedio(resolvePesoNetoPromedio(request));
        entity.setAparienciaOk(request.aparienciaOk());
        entity.setEtiquetadoOk(request.etiquetadoOk());
        entity.setTapadoOk(request.tapadoOk());
        entity.setCantidadPorCaja(request.cantidadPorCaja());
        entity.setLiberado(Boolean.TRUE.equals(request.liberado()));
        entity.setRetenido(Boolean.TRUE.equals(request.retenido()));
        entity.setRealizadoPor(ref(UsuarioEntity.class, request.idRealizadoPor()));
        entity.setVerificadoPor(request.idVerificadoPor() != null
                ? ref(UsuarioEntity.class, request.idVerificadoPor())
                : null);
        entity.setObservaciones(request.observaciones());

        entity.getMuestras().clear();
        if (request.muestras() != null) {
            request.muestras().forEach(muestraRequest -> {
                ControlPesoMuestraEntity muestra = new ControlPesoMuestraEntity();
                muestra.setControlPesoProducto(entity);
                muestra.setNumeroMuestra(muestraRequest.numeroMuestra());
                muestra.setPesoBruto(muestraRequest.pesoBruto());
                muestra.setTara(muestraRequest.tara());
                muestra.setPesoNeto(muestraRequest.pesoNeto());
                entity.getMuestras().add(muestra);
            });
        }
    }

    private void validarOrdenYBatch(Long idOrdenProduccion, Long idEjecucionBatch) {
        if (idOrdenProduccion == null) {
            throw new ReglaNegocioException("La orden de produccion es obligatoria.");
        }

        OrdenProduccionEntity orden = entityManager.find(OrdenProduccionEntity.class, idOrdenProduccion);
        if (orden == null) {
            throw new RecursoNoEncontradoException("No existe una orden de produccion con ID: " + idOrdenProduccion);
        }

        validacionGuardService.validarOrdenNoAprobada(idOrdenProduccion);

        if (idEjecucionBatch != null) {
            EjecucionBatchEntity batch = entityManager.find(EjecucionBatchEntity.class, idEjecucionBatch);
            if (batch == null) {
                throw new RecursoNoEncontradoException("No existe un batch con ID: " + idEjecucionBatch);
            }
            if (!idOrdenProduccion.equals(batch.getOrdenProduccion().getId())) {
                throw new ReglaNegocioException("El batch no pertenece a la orden de produccion indicada.");
            }
        }
    }

    private void validarControlProceso(ControlCalidadProcesoRequest request) {
        if (request.fechaProduccion() == null) {
            throw new ReglaNegocioException("La fecha de produccion es obligatoria.");
        }

        if (request.idEjecucionBatch() == null) {
            throw new ReglaNegocioException("Debe seleccionar el batch / marmita.");
        }

        validarTexto(request.producto(), "Debe registrar el producto.");
        validarTexto(request.lote(), "Debe registrar el lote.");

        validarNumero(request.phLeche(), "Debe registrar el pH de la leche.");
        validarNumero(request.acidezLeche(), "Debe registrar la acidez de la leche.");
        validarNumero(request.densidadLeche(), "Debe registrar la densidad de la leche.");
        validarNumero(request.grasaLeche(), "Debe registrar la grasa de la leche.");

        if (request.horaInicioHidrolisis() == null) {
            throw new ReglaNegocioException("Debe registrar la hora de inicio de hidrolisis.");
        }

        validarNumero(request.phInicial(), "Debe registrar el pH inicial.");

        if (request.horaFinHidrolisis() == null) {
            throw new ReglaNegocioException("Debe registrar la hora de fin de hidrolisis.");
        }

        validarNumero(request.phFinal(), "Debe registrar el pH final.");
        validarNumero(request.brixInicial(), "Debe registrar el Brix inicial.");
        validarNumero(request.brixFinal(), "Debe registrar el Brix final.");
        validarNumero(request.temperaturaCoccion(), "Debe registrar la temperatura de coccion.");
        validarNumero(request.temperaturaEnvasado(), "Debe registrar la temperatura de envasado.");

        validarTexto(request.colorVisual(), "Debe registrar el color visual.");
        validarTexto(request.saborVisual(), "Debe registrar el sabor.");
        validarTexto(request.texturaVisual(), "Debe registrar la textura.");

        if (request.fechaVencimiento() == null) {
            throw new ReglaNegocioException("Debe registrar la fecha de vencimiento.");
        }

        validarTexto(request.presentacionEnvasado(), "Debe registrar la presentacion de envasado.");
        validarLiberadoRetenido(request.liberado(), request.retenido(), "producto en proceso");
    }

    private void validarControlPeso(ControlPesoProductoRequest request) {
        if (request.fechaControl() == null) {
            throw new ReglaNegocioException("La fecha de control es obligatoria.");
        }

        validarTexto(request.producto(), "Debe registrar el producto.");
        validarTexto(request.marca(), "Debe registrar la marca.");
        validarTexto(request.lote(), "Debe registrar el lote.");

        if (request.fechaVencimiento() == null) {
            throw new ReglaNegocioException("Debe registrar la fecha de vencimiento.");
        }

        validarTexto(request.presentacion(), "Debe registrar la presentacion.");
        validarTexto(request.numeroTanda(), "Debe registrar el numero de tanda.");
        validarTexto(request.rangoBatches(), "Debe registrar el rango de batches.");

        if (request.muestras() == null || request.muestras().size() < 10) {
            throw new ReglaNegocioException("Debe registrar el peso neto de las 10 muestras.");
        }

        for (ControlPesoMuestraRequest muestra : request.muestras()) {
            if (muestra.numeroMuestra() == null) {
                throw new ReglaNegocioException("Cada muestra debe tener numero de muestra.");
            }

            validarNumero(muestra.pesoNeto(), "Debe registrar el peso neto de todas las muestras.");
        }

        BigDecimal promedio = resolvePesoNetoPromedio(request);
        validarNumero(promedio, "Debe registrar el promedio de peso neto.");

        if (request.cantidadPorCaja() == null || request.cantidadPorCaja() <= 0) {
            throw new ReglaNegocioException("Debe registrar la cantidad por caja.");
        }

        validarLiberadoRetenido(request.liberado(), request.retenido(), "producto terminado");

        if (Boolean.TRUE.equals(request.liberado())
                && (Boolean.FALSE.equals(request.aparienciaOk())
                        || Boolean.FALSE.equals(request.etiquetadoOk())
                        || Boolean.FALSE.equals(request.tapadoOk()))) {
            throw new ReglaNegocioException(
                    "No puede liberar producto terminado si apariencia, etiquetado o tapado no estan conformes.");
        }
    }

    private void validarLiberadoRetenido(Boolean liberado, Boolean retenido, String contexto) {
        boolean estaLiberado = Boolean.TRUE.equals(liberado);
        boolean estaRetenido = Boolean.TRUE.equals(retenido);

        if (!estaLiberado && !estaRetenido) {
            throw new ReglaNegocioException("Debe marcar si el " + contexto + " queda liberado o retenido.");
        }

        if (estaLiberado && estaRetenido) {
            throw new ReglaNegocioException("No puede marcar Liberado y Retenido al mismo tiempo.");
        }
    }

    private void validarTexto(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new ReglaNegocioException(mensaje);
        }
    }

    private void validarNumero(BigDecimal valor, String mensaje) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException(mensaje);
        }
    }

    private BigDecimal resolvePesoNetoPromedio(ControlPesoProductoRequest request) {
        if (request.pesoNetoPromedio() != null) {
            return request.pesoNetoPromedio();
        }

        if (request.muestras() == null || request.muestras().isEmpty()) {
            return null;
        }

        BigDecimal total = request.muestras().stream()
                .map(muestra -> muestra.pesoNeto() == null ? BigDecimal.ZERO : muestra.pesoNeto())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(request.muestras().size()), 3, java.math.RoundingMode.HALF_UP);
    }

    private ControlCalidadProcesoResponse toResponse(ControlCalidadProcesoEntity entity) {
        return new ControlCalidadProcesoResponse(
                entity.getId(),
                entity.getOrdenProduccion().getId(),
                entity.getEjecucionBatch() != null ? entity.getEjecucionBatch().getId() : null,
                entity.getFechaProduccion(),
                entity.getTipoProducto(),
                entity.getProducto(),
                entity.getLote(),
                entity.getNumeroMarmita(),
                entity.getProductoEnProceso(),
                entity.getPhLeche(),
                entity.getAcidezLeche(),
                entity.getDensidadLeche(),
                entity.getGrasaLeche(),
                entity.getHoraInicioHidrolisis(),
                entity.getPhInicial(),
                entity.getHoraFinHidrolisis(),
                entity.getTemperaturaInicial(),
                entity.getTemperaturaFinal(),
                entity.getAcidezInicial(),
                entity.getAcidezFinal(),
                entity.getPhFinal(),
                entity.getBrixInicial(),
                entity.getBrixFinal(),
                entity.getPresion(),
                entity.getTemperaturaCoccion(),
                entity.getTemperaturaEnvasado(),
                entity.getColorVisual(),
                entity.getSaborVisual(),
                entity.getTexturaVisual(),
                entity.getPresentacionEnvasado(),
                entity.getFechaVencimiento(),
                entity.getLiberado(),
                entity.getRetenido(),
                entity.getRealizadoPor().getIdUsuario(),
                entity.getVerificadoPor() != null ? entity.getVerificadoPor().getIdUsuario() : null,
                entity.getObservaciones(),
                entity.getCreatedAt());
    }

    private CalidadRecepcionLecheResponse toResponse(CalidadRecepcionLecheEntity entity) {
        return new CalidadRecepcionLecheResponse(
                entity.getId(),
                entity.getRecepcionLeche().getId(),
                entity.getFechaControl(),
                entity.getPruebaAlcoholOk(),
                entity.getLactoscanOk(),
                entity.getAcidez(),
                entity.getDensidad(),
                entity.getGrasa(),
                entity.getAguaPct(),
                entity.getTemperatura(),
                entity.getPh(),
                entity.getAprobado(),
                entity.getRetenido(),
                entity.getRealizadoPor().getIdUsuario(),
                entity.getObservaciones(),
                entity.getCreatedAt());
    }

    private ControlPesoProductoResponse toResponse(ControlPesoProductoEntity entity) {
        return new ControlPesoProductoResponse(
                entity.getId(),
                entity.getOrdenProduccion().getId(),
                entity.getEjecucionBatch() != null ? entity.getEjecucionBatch().getId() : null,
                entity.getSku() != null ? entity.getSku().getId() : null,
                entity.getFechaControl(),
                entity.getProducto(),
                entity.getMarca(),
                entity.getLote(),
                entity.getFechaVencimiento(),
                entity.getPresentacion(),
                entity.getNumeroTanda(),
                entity.getRangoBatches(),
                entity.getPesoBrutoPromedio(),
                entity.getTaraPromedio(),
                entity.getPesoNetoPromedio(),
                entity.getAparienciaOk(),
                entity.getEtiquetadoOk(),
                entity.getTapadoOk(),
                entity.getCantidadPorCaja(),
                entity.getLiberado(),
                entity.getRetenido(),
                entity.getRealizadoPor().getIdUsuario(),
                entity.getVerificadoPor() != null ? entity.getVerificadoPor().getIdUsuario() : null,
                entity.getObservaciones(),
                entity.getCreatedAt(),
                entity.getMuestras().stream()
                        .map(muestra -> new ControlPesoMuestraResponse(
                                muestra.getId(),
                                muestra.getNumeroMuestra(),
                                muestra.getPesoBruto(),
                                muestra.getTara(),
                                muestra.getPesoNeto()))
                        .toList());
    }

    @Transactional
    public List<EstadoCalidadRecepcionResponse> listarEstadosRecepcion() {
        Map<Long, EstadoCalidadRecepcionResponse> estados = new LinkedHashMap<>();

        calidadRecepcionRepository.findAllOrdenadasParaEstadoRecepcion()
                .forEach(control -> {
                    Long idRecepcion = control.getRecepcionLeche().getId();
                    estados.computeIfAbsent(idRecepcion, ignored -> new EstadoCalidadRecepcionResponse(
                            idRecepcion,
                            estadoRecepcion(control.getAprobado(), control.getRetenido())));
                });

        return List.copyOf(estados.values());
    }

    private String estadoRecepcion(Boolean aprobado, Boolean retenido) {
        if (Boolean.TRUE.equals(retenido)) {
            return "RETENIDA";
        }
        if (Boolean.TRUE.equals(aprobado)) {
            return "APROBADA";
        }
        return "NO_APROBADA";
    }

    private <T> T ref(Class<T> type, Long id) {
        return entityManager.getReference(type, id);
    }
}
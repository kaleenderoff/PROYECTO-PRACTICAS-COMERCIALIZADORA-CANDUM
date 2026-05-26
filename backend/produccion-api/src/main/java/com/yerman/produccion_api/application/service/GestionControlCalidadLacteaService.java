package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.dto.request.ControlCalidadProcesoRequest;
import com.yerman.produccion_api.application.dto.request.CalidadRecepcionLecheRequest;
import com.yerman.produccion_api.application.dto.request.ControlPesoProductoRequest;
import com.yerman.produccion_api.application.dto.response.CalidadRecepcionLecheResponse;
import com.yerman.produccion_api.application.dto.response.ControlCalidadProcesoResponse;
import com.yerman.produccion_api.application.dto.response.ControlPesoMuestraResponse;
import com.yerman.produccion_api.application.dto.response.ControlPesoProductoResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.infrastructure.entity.*;
import com.yerman.produccion_api.infrastructure.repository.CalidadRecepcionLecheJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ControlCalidadProcesoJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ControlPesoProductoJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

        ControlCalidadProcesoEntity entity = new ControlCalidadProcesoEntity();
        entity.setOrdenProduccion(ref(OrdenProduccionEntity.class, request.idOrdenProduccion()));
        if (request.idEjecucionBatch() != null) {
            entity.setEjecucionBatch(ref(EjecucionBatchEntity.class, request.idEjecucionBatch()));
        }
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
        if (request.idVerificadoPor() != null) {
            entity.setVerificadoPor(ref(UsuarioEntity.class, request.idVerificadoPor()));
        }
        entity.setObservaciones(request.observaciones());

        return toResponse(procesoRepository.save(entity));
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

        ControlPesoProductoEntity entity = new ControlPesoProductoEntity();
        entity.setOrdenProduccion(ref(OrdenProduccionEntity.class, request.idOrdenProduccion()));
        if (request.idEjecucionBatch() != null) {
            entity.setEjecucionBatch(ref(EjecucionBatchEntity.class, request.idEjecucionBatch()));
        }
        if (request.idSku() != null) {
            entity.setSku(ref(CatalogoSkuEntity.class, request.idSku()));
        }
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
        if (request.idVerificadoPor() != null) {
            entity.setVerificadoPor(ref(UsuarioEntity.class, request.idVerificadoPor()));
        }
        entity.setObservaciones(request.observaciones());

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

        return toResponse(pesoRepository.save(entity));
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

    private <T> T ref(Class<T> type, Long id) {
        return entityManager.getReference(type, id);
    }
}

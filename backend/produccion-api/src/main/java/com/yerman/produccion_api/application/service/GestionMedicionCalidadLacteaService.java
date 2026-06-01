package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.MedicionCalidadLactea;
import com.yerman.produccion_api.domain.model.TipoMedicionCalidadLactea;
import com.yerman.produccion_api.domain.port.in.GestionMedicionCalidadLacteaUseCase;
import com.yerman.produccion_api.domain.port.in.GestionProduccionLacteaUseCase;
import com.yerman.produccion_api.domain.port.out.EjecucionBatchRepositoryPort;
import com.yerman.produccion_api.domain.port.out.MedicionCalidadLacteaRepositoryPort;
import com.yerman.produccion_api.domain.port.out.OrdenProduccionRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ProduccionLacteaBatchRepositoryPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GestionMedicionCalidadLacteaService implements GestionMedicionCalidadLacteaUseCase {

    private static final BigDecimal BRIX_MAXIMO = new BigDecimal("100.00");
    private static final BigDecimal PH_MAXIMO = new BigDecimal("14.00");

    private final MedicionCalidadLacteaRepositoryPort repository;
    private final GestionProduccionLacteaUseCase produccionLacteaUseCase;
    private final ProduccionLacteaBatchRepositoryPort batchRepositoryPort;
    private final OrdenProduccionRepositoryPort ordenRepositoryPort;
    private final EjecucionBatchRepositoryPort ejecucionBatchRepositoryPort;
    private final ValidacionOrdenProduccionGuardService validacionGuardService;

    public GestionMedicionCalidadLacteaService(
            MedicionCalidadLacteaRepositoryPort repository,
            GestionProduccionLacteaUseCase produccionLacteaUseCase,
            ProduccionLacteaBatchRepositoryPort batchRepositoryPort,
            OrdenProduccionRepositoryPort ordenRepositoryPort,
            EjecucionBatchRepositoryPort ejecucionBatchRepositoryPort,
            ValidacionOrdenProduccionGuardService validacionGuardService) {
        this.repository = repository;
        this.produccionLacteaUseCase = produccionLacteaUseCase;
        this.batchRepositoryPort = batchRepositoryPort;
        this.ordenRepositoryPort = ordenRepositoryPort;
        this.ejecucionBatchRepositoryPort = ejecucionBatchRepositoryPort;
        this.validacionGuardService = validacionGuardService;
    }

    @Override
    @Transactional
    public MedicionCalidadLactea registrar(MedicionCalidadLactea medicion) {
        validarMedicion(medicion);
        validarDuplicados(medicion);
        return repository.guardar(medicion);
    }

    @Override
    @Transactional
    public MedicionCalidadLactea actualizar(Long id, MedicionCalidadLactea medicion) {
        MedicionCalidadLactea actual = obtenerPorId(id);

        medicion.setId(id);

        if (medicion.getFechaHoraMedicion() == null) {
            medicion.setFechaHoraMedicion(actual.getFechaHoraMedicion());
        }

        validarMedicion(medicion);
        validarDuplicadosEnActualizacion(actual, medicion);

        return repository.guardar(medicion);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        MedicionCalidadLactea actual = obtenerPorId(id);

        if (actual.getIdOrdenProduccion() != null) {
            validacionGuardService.validarOrdenNoAprobada(actual.getIdOrdenProduccion());
        }

        repository.eliminar(id);
    }

    @Override
    @Transactional
    public MedicionCalidadLactea obtenerPorId(Long id) {
        return repository.obtenerPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontro la medicion de calidad lactea con ID: " + id));
    }

    @Override
    @Transactional
    public List<MedicionCalidadLactea> listarPorProduccion(Long idProduccionLactea) {
        if (idProduccionLactea == null) {
            throw new ReglaNegocioException("La produccion lactea es obligatoria.");
        }

        produccionLacteaUseCase.obtenerPorId(idProduccionLactea);
        return repository.listarPorProduccion(idProduccionLactea);
    }

    @Override
    @Transactional
    public List<MedicionCalidadLactea> listarPorOrden(Long idOrdenProduccion) {
        if (idOrdenProduccion == null) {
            throw new ReglaNegocioException("La orden de produccion es obligatoria.");
        }

        ordenRepositoryPort.obtenerPorId(idOrdenProduccion)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe una orden de produccion con ID: " + idOrdenProduccion));

        return repository.listarPorOrden(idOrdenProduccion);
    }

    private void validarMedicion(MedicionCalidadLactea medicion) {
        if (medicion == null) {
            throw new ReglaNegocioException("La medicion de calidad es obligatoria.");
        }

        boolean tieneProduccionLactea = medicion.getIdProduccionLactea() != null;
        boolean tieneOrdenProduccion = medicion.getIdOrdenProduccion() != null;

        if (!tieneProduccionLactea && !tieneOrdenProduccion) {
            throw new ReglaNegocioException(
                    "Debe asociar la medicion a una produccion lactea o a una orden de produccion.");
        }

        if (tieneProduccionLactea) {
            produccionLacteaUseCase.obtenerPorId(medicion.getIdProduccionLactea());
        }

        if (medicion.getIdProduccionLacteaBatch() != null
                && !batchRepositoryPort.existePorIdYProduccion(
                        medicion.getIdProduccionLacteaBatch(),
                        medicion.getIdProduccionLactea())) {
            throw new ReglaNegocioException("El batch no pertenece a la produccion lactea indicada.");
        }

        if (tieneOrdenProduccion) {
            ordenRepositoryPort.obtenerPorId(medicion.getIdOrdenProduccion())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe una orden de produccion con ID: " + medicion.getIdOrdenProduccion()));

            validacionGuardService.validarOrdenNoAprobada(medicion.getIdOrdenProduccion());
        }

        if (medicion.getTipoMedicion() == null) {
            throw new ReglaNegocioException("El tipo de medicion es obligatorio.");
        }

        if (medicion.getReferencia() == null || medicion.getReferencia().isBlank()) {
            throw new ReglaNegocioException("La referencia de la medicion es obligatoria.");
        }

        validarBatchSiAplica(medicion, tieneOrdenProduccion);
        validarConsistenciaTipoReferenciaYBatch(medicion);
        validarBrixYPhObligatorios(medicion);
        validarValorNoNegativo(medicion.getBrix(), "Brix");
        validarValorNoNegativo(medicion.getPh(), "pH");
        validarRangoBrix(medicion.getBrix());
        validarRangoPh(medicion.getPh());

        if (medicion.getIdUsuarioCalidad() == null) {
            throw new ReglaNegocioException("El usuario de calidad es obligatorio.");
        }

        if (medicion.getFechaHoraMedicion() == null) {
            medicion.setFechaHoraMedicion(LocalDateTime.now());
        }
    }

    private void validarBatchSiAplica(MedicionCalidadLactea medicion, boolean tieneOrdenProduccion) {
        if (medicion.getIdEjecucionBatch() == null) {
            return;
        }

        var batch = ejecucionBatchRepositoryPort.obtenerPorId(medicion.getIdEjecucionBatch())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un batch de ejecucion con ID: " + medicion.getIdEjecucionBatch()));

        if (tieneOrdenProduccion && !medicion.getIdOrdenProduccion().equals(batch.getIdOrdenProduccion())) {
            throw new ReglaNegocioException("El batch no pertenece a la orden de produccion indicada.");
        }
    }

    private void validarConsistenciaTipoReferenciaYBatch(MedicionCalidadLactea medicion) {
        TipoMedicionCalidadLactea tipo = medicion.getTipoMedicion();
        String referencia = medicion.getReferencia() == null ? "" : medicion.getReferencia().trim();

        if (tipo == TipoMedicionCalidadLactea.BACHE) {
            if (medicion.getIdEjecucionBatch() == null) {
                throw new ReglaNegocioException("Debe asociar la medicion de batch a un batch de ejecucion.");
            }

            if (!referencia.startsWith("B.")) {
                throw new ReglaNegocioException("La referencia de batch debe iniciar con B.");
            }

            return;
        }

        if (tipo == TipoMedicionCalidadLactea.MEZCLA) {
            if (medicion.getIdEjecucionBatch() != null) {
                throw new ReglaNegocioException("La medicion de mezcla no debe asociarse a un batch.");
            }

            if (!"Mezcla".equals(referencia)) {
                throw new ReglaNegocioException(
                        "La referencia de mezcla debe ser generada automaticamente como Mezcla.");
            }

            return;
        }

        if (tipo == TipoMedicionCalidadLactea.TANDA) {
            if (medicion.getIdEjecucionBatch() != null) {
                throw new ReglaNegocioException("La medicion de tanda no debe asociarse a un batch.");
            }

            if (!referencia.startsWith("Tanda ")) {
                throw new ReglaNegocioException(
                        "La referencia de tanda debe ser generada automaticamente como Tanda 1, Tanda 2, etc.");
            }
        }
    }

    private void validarBrixYPhObligatorios(MedicionCalidadLactea medicion) {
        String nombreTipo = obtenerNombreTipoMedicion(medicion.getTipoMedicion());

        if (medicion.getBrix() == null) {
            throw new ReglaNegocioException("Debe registrar el Brix de " + nombreTipo + ".");
        }

        if (medicion.getPh() == null) {
            throw new ReglaNegocioException("Debe registrar el pH de " + nombreTipo + ".");
        }
    }

    private String obtenerNombreTipoMedicion(TipoMedicionCalidadLactea tipoMedicion) {
        if (tipoMedicion == TipoMedicionCalidadLactea.BACHE) {
            return "batch";
        }

        if (tipoMedicion == TipoMedicionCalidadLactea.MEZCLA) {
            return "la mezcla";
        }

        if (tipoMedicion == TipoMedicionCalidadLactea.TANDA) {
            return "la tanda";
        }

        return "la medicion";
    }

    private void validarDuplicados(MedicionCalidadLactea medicion) {
        validarDuplicadoBatch(medicion);
        validarDuplicadoMezcla(medicion);
    }

    private void validarDuplicadosEnActualizacion(
            MedicionCalidadLactea actual,
            MedicionCalidadLactea nueva) {

        if (actual.getTipoMedicion() == nueva.getTipoMedicion()) {
            if (nueva.getTipoMedicion() == TipoMedicionCalidadLactea.BACHE
                    && mismosValores(actual.getIdOrdenProduccion(), nueva.getIdOrdenProduccion())
                    && mismosValores(actual.getIdEjecucionBatch(), nueva.getIdEjecucionBatch())) {
                return;
            }

            if (nueva.getTipoMedicion() == TipoMedicionCalidadLactea.MEZCLA
                    && mismosValores(actual.getIdOrdenProduccion(), nueva.getIdOrdenProduccion())) {
                return;
            }
        }

        validarDuplicados(nueva);
    }

    private void validarDuplicadoBatch(MedicionCalidadLactea medicion) {
        if (medicion.getTipoMedicion() != TipoMedicionCalidadLactea.BACHE) {
            return;
        }

        if (medicion.getIdOrdenProduccion() == null || medicion.getIdEjecucionBatch() == null) {
            return;
        }

        boolean existe = repository.existeMedicionPorOrdenBatchYTipo(
                medicion.getIdOrdenProduccion(),
                medicion.getIdEjecucionBatch(),
                TipoMedicionCalidadLactea.BACHE);

        if (existe) {
            throw new ReglaNegocioException(
                    "Este batch ya tiene medicion de calidad registrada. Use editar si necesita corregirla.");
        }
    }

    private void validarDuplicadoMezcla(MedicionCalidadLactea medicion) {
        if (medicion.getTipoMedicion() != TipoMedicionCalidadLactea.MEZCLA) {
            return;
        }

        if (medicion.getIdOrdenProduccion() == null) {
            return;
        }

        boolean existe = repository.existeMedicionPorOrdenYTipo(
                medicion.getIdOrdenProduccion(),
                TipoMedicionCalidadLactea.MEZCLA);

        if (existe) {
            throw new ReglaNegocioException(
                    "Esta orden ya tiene medicion de mezcla registrada. Use editar si necesita corregirla.");
        }
    }

    private boolean mismosValores(Long valorA, Long valorB) {
        if (valorA == null && valorB == null) {
            return true;
        }

        if (valorA == null || valorB == null) {
            return false;
        }

        return valorA.equals(valorB);
    }

    private void validarValorNoNegativo(BigDecimal valor, String nombreCampo) {
        if (valor != null && valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new ReglaNegocioException(nombreCampo + " no puede ser negativo.");
        }
    }

    private void validarRangoBrix(BigDecimal brix) {
        if (brix != null && brix.compareTo(BRIX_MAXIMO) > 0) {
            throw new ReglaNegocioException("El Brix no puede ser mayor a 100.");
        }
    }

    private void validarRangoPh(BigDecimal ph) {
        if (ph != null && ph.compareTo(PH_MAXIMO) > 0) {
            throw new ReglaNegocioException("El pH no puede ser mayor a 14.");
        }
    }
}
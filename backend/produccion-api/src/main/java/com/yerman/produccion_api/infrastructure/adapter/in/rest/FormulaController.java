package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.ActualizarFormulaDetalleRequest;
import com.yerman.produccion_api.application.dto.request.ActualizarFormulaVersionRequest;
import com.yerman.produccion_api.application.dto.request.CrearFormulaDetalleRequest;
import com.yerman.produccion_api.application.dto.request.CrearFormulaRequest;
import com.yerman.produccion_api.application.dto.request.CrearFormulaVersionRequest;
import com.yerman.produccion_api.application.dto.response.FormulaProduccionResponse;
import com.yerman.produccion_api.application.exception.RecursoDuplicadoException;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.application.service.FormulaCalculoUtil;
import com.yerman.produccion_api.domain.model.TipoCalculoInsumo;
import com.yerman.produccion_api.infrastructure.entity.*;
import com.yerman.produccion_api.infrastructure.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/formulas")
@Transactional(readOnly = true)
public class FormulaController {

    private final FormulaJpaRepository formulaRepository;
    private final FormulaVersionJpaRepository formulaVersionRepository;
    private final FormulaDetalleJpaRepository formulaDetalleRepository;
    private final CatalogoProductoJpaRepository productoRepository;
    private final InsumoJpaRepository insumoRepository;
    private final UsuarioJpaRepository usuarioRepository;

    public FormulaController(
            FormulaJpaRepository formulaRepository,
            FormulaVersionJpaRepository formulaVersionRepository,
            FormulaDetalleJpaRepository formulaDetalleRepository,
            CatalogoProductoJpaRepository productoRepository,
            InsumoJpaRepository insumoRepository,
            UsuarioJpaRepository usuarioRepository) {
        this.formulaRepository = formulaRepository;
        this.formulaVersionRepository = formulaVersionRepository;
        this.formulaDetalleRepository = formulaDetalleRepository;
        this.productoRepository = productoRepository;
        this.insumoRepository = insumoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/producto/{idProducto}")
    public List<FormulaResumenResponse> listarFormulasPorProducto(@PathVariable Long idProducto) {
        return formulaRepository.findByProductoIdAndActivoTrue(idProducto)
                .stream()
                .map(this::toFormulaResumenResponse)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public FormulaResumenResponse crearFormula(@Valid @RequestBody CrearFormulaRequest request) {
        if (formulaRepository.existsByProductoIdAndNombreIgnoreCase(request.idProducto(), limpiar(request.nombre()))) {
            throw new RecursoDuplicadoException("Ya existe una fórmula con ese nombre para el producto indicado");
        }

        CatalogoProductoEntity producto = buscarProducto(request.idProducto());

        FormulaEntity formula = new FormulaEntity();
        formula.setNombre(limpiar(request.nombre()));
        formula.setProducto(producto);
        formula.setActivo(true);
        formula.setVersiones(new ArrayList<>());

        return toFormulaResumenResponse(formulaRepository.save(formula));
    }

    @GetMapping("/{idFormula}/versiones")
    public List<FormulaProduccionResponse> listarVersiones(@PathVariable Long idFormula) {
        buscarFormula(idFormula);

        return formulaVersionRepository.findByFormulaIdOrderByFechaInicioVigenciaDesc(idFormula)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping("/{idFormula}/versiones")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public FormulaProduccionResponse crearVersion(
            @PathVariable Long idFormula,
            @Valid @RequestBody CrearFormulaVersionRequest request) {

        FormulaEntity formula = buscarFormula(idFormula);

        if (formulaVersionRepository.existsByFormulaIdAndVersionIgnoreCase(idFormula, limpiar(request.version()))) {
            throw new RecursoDuplicadoException("Ya existe esa versión para la fórmula indicada");
        }

        UsuarioEntity usuario = buscarUsuario(request.idCreadoPor());

        FormulaVersionEntity version = new FormulaVersionEntity();
        version.setFormula(formula);
        version.setVersion(limpiar(request.version()));
        version.setFechaInicioVigencia(request.fechaInicioVigencia());
        version.setKgBatchTotal(request.kgBatchTotal());
        version.setReduccionEvaporacionPct(request.reduccionEvaporacionPct());
        version.setRendimientoTeoricoPct(request.rendimientoTeoricoPct());
        version.setBrixObjetivoMin(request.brixObjetivoMin());
        version.setBrixObjetivoMax(request.brixObjetivoMax());
        version.setAprobadoPor(limpiar(request.aprobadoPor()));
        version.setDocumentoAprobacion(limpiar(request.documentoAprobacion()));
        version.setObservacionesTecnicas(limpiar(request.observacionesTecnicas()));
        version.setCreadoPor(usuario);
        version.setEstado(FormulaVersionEntity.EstadoFormula.BORRADOR);
        version.setDetalles(new ArrayList<>());

        return toResponse(formulaVersionRepository.save(version));
    }

    @PostMapping("/versiones/{idFormulaVersion}/detalles")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public FormulaProduccionResponse agregarDetalle(
            @PathVariable Long idFormulaVersion,
            @Valid @RequestBody CrearFormulaDetalleRequest request) {

        FormulaVersionEntity version = buscarVersion(idFormulaVersion);
        validarVersionEditable(version);

        if (formulaDetalleRepository.existsByFormulaVersionIdAndInsumoId(idFormulaVersion, request.idInsumo())) {
            throw new RecursoDuplicadoException("El insumo ya existe en esta versión de fórmula");
        }

        InsumoEntity insumo = buscarInsumo(request.idInsumo());

        TipoCalculoInsumo tipoCalculo = request.tipoCalculo() != null
                ? request.tipoCalculo()
                : TipoCalculoInsumo.PORCENTAJE_BATCH;

        validarDetalleFormula(tipoCalculo, request.cantidadKg(), request.porcentaje(), version.getKgBatchTotal());

        FormulaDetalleEntity detalle = new FormulaDetalleEntity();
        detalle.setFormulaVersion(version);
        detalle.setInsumo(insumo);
        detalle.setTipoCalculo(tipoCalculo);
        detalle.setCantidadKg(calcularCantidadKgReferencia(tipoCalculo, request.cantidadKg(), request.porcentaje(),
                version.getKgBatchTotal()));
        detalle.setPorcentaje(calcularPorcentajeReferencia(tipoCalculo, request.cantidadKg(), request.porcentaje(),
                version.getKgBatchTotal()));
        detalle.setEsCritico(request.esCritico() != null ? request.esCritico() : false);
        detalle.setOrdenAdicion(request.ordenAdicion() != null ? request.ordenAdicion() : 1);

        formulaDetalleRepository.save(detalle);

        return toResponse(buscarVersion(idFormulaVersion));
    }

    @PutMapping("/versiones/{idFormulaVersion}")
    @Transactional
    public FormulaProduccionResponse actualizarVersion(
            @PathVariable Long idFormulaVersion,
            @Valid @RequestBody ActualizarFormulaVersionRequest request) {

        FormulaVersionEntity version = buscarVersion(idFormulaVersion);
        validarVersionEditable(version);

        if (request.fechaInicioVigencia() != null) {
            version.setFechaInicioVigencia(request.fechaInicioVigencia());
        }

        if (request.fechaFinVigencia() != null) {
            version.setFechaFinVigencia(request.fechaFinVigencia());
        }

        if (request.kgBatchTotal() != null) {
            version.setKgBatchTotal(request.kgBatchTotal());
            recalcularDetallesPorCambioKgBatch(version);
        }

        if (request.reduccionEvaporacionPct() != null) {
            version.setReduccionEvaporacionPct(request.reduccionEvaporacionPct());
        }

        if (request.rendimientoTeoricoPct() != null) {
            version.setRendimientoTeoricoPct(request.rendimientoTeoricoPct());
        }

        if (request.brixObjetivoMin() != null) {
            version.setBrixObjetivoMin(request.brixObjetivoMin());
        }

        if (request.brixObjetivoMax() != null) {
            version.setBrixObjetivoMax(request.brixObjetivoMax());
        }

        if (request.aprobadoPor() != null) {
            version.setAprobadoPor(limpiar(request.aprobadoPor()));
        }

        if (request.documentoAprobacion() != null) {
            version.setDocumentoAprobacion(limpiar(request.documentoAprobacion()));
        }

        if (request.observacionesTecnicas() != null) {
            version.setObservacionesTecnicas(limpiar(request.observacionesTecnicas()));
        }

        return toResponse(formulaVersionRepository.save(version));
    }

    @PutMapping("/detalles/{idDetalle}")
    @Transactional
    public FormulaProduccionResponse actualizarDetalle(
            @PathVariable Long idDetalle,
            @Valid @RequestBody ActualizarFormulaDetalleRequest request) {

        FormulaDetalleEntity detalle = formulaDetalleRepository.findById(idDetalle)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Detalle de fórmula no encontrado con id: " + idDetalle));

        FormulaVersionEntity version = detalle.getFormulaVersion();
        validarVersionEditable(version);

        TipoCalculoInsumo tipoCalculo = request.tipoCalculo() != null
                ? request.tipoCalculo()
                : detalle.getTipoCalculo();

        BigDecimal cantidadKgBase = request.cantidadKg() != null
                ? request.cantidadKg()
                : detalle.getCantidadKg();

        BigDecimal porcentajeBase = request.porcentaje() != null
                ? request.porcentaje()
                : detalle.getPorcentaje();

        validarDetalleFormula(tipoCalculo, cantidadKgBase, porcentajeBase, version.getKgBatchTotal());

        detalle.setTipoCalculo(tipoCalculo);
        detalle.setCantidadKg(
                calcularCantidadKgReferencia(tipoCalculo, cantidadKgBase, porcentajeBase, version.getKgBatchTotal()));
        detalle.setPorcentaje(
                calcularPorcentajeReferencia(tipoCalculo, cantidadKgBase, porcentajeBase, version.getKgBatchTotal()));

        if (request.esCritico() != null) {
            detalle.setEsCritico(request.esCritico());
        }

        if (request.ordenAdicion() != null) {
            detalle.setOrdenAdicion(request.ordenAdicion());
        }

        formulaDetalleRepository.save(detalle);

        return toResponse(buscarVersion(version.getId()));
    }

    @DeleteMapping("/detalles/{idDetalle}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void eliminarDetalle(@PathVariable Long idDetalle) {
        FormulaDetalleEntity detalle = formulaDetalleRepository.findById(idDetalle)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Detalle de fórmula no encontrado con id: " + idDetalle));

        validarVersionEditable(detalle.getFormulaVersion());

        formulaDetalleRepository.delete(detalle);
    }

    @PatchMapping("/versiones/{idFormulaVersion}/vigente")
    @Transactional
    public FormulaProduccionResponse marcarVersionVigente(
            @PathVariable Long idFormulaVersion) {

        FormulaVersionEntity version = buscarVersion(idFormulaVersion);

        if (version.getDetalles() == null || version.getDetalles().isEmpty()) {
            throw new ReglaNegocioException("La fórmula debe tener al menos un insumo");
        }

        if (version.getKgBatchTotal() == null || version.getKgBatchTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("La fórmula debe tener kg batch total");
        }

        if (version.getRendimientoTeoricoPct() == null
                || version.getRendimientoTeoricoPct().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("La fórmula debe tener rendimiento teórico");
        }

        validarDetallesAntesDeVigente(version);

        List<FormulaVersionEntity> versionesVigentes = formulaVersionRepository
                .findByFormulaIdAndEstado(
                        version.getFormula().getId(),
                        FormulaVersionEntity.EstadoFormula.VIGENTE);

        for (FormulaVersionEntity vigente : versionesVigentes) {
            vigente.setEstado(FormulaVersionEntity.EstadoFormula.REEMPLAZADA);
            formulaVersionRepository.save(vigente);
        }

        version.setEstado(FormulaVersionEntity.EstadoFormula.VIGENTE);

        return toResponse(formulaVersionRepository.save(version));
    }

    @GetMapping("/producto/{idProducto}/vigente")
    public FormulaProduccionResponse obtenerFormulaVigentePorProducto(@PathVariable Long idProducto) {
        FormulaVersionEntity version = formulaVersionRepository
                .findFirstByFormulaProductoIdAndEstadoOrderByFechaInicioVigenciaDesc(
                        idProducto,
                        FormulaVersionEntity.EstadoFormula.VIGENTE)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe fórmula vigente para el producto con ID: " + idProducto));

        return toResponse(version);
    }

    private void validarDetalleFormula(
            TipoCalculoInsumo tipoCalculo,
            BigDecimal cantidadKg,
            BigDecimal porcentaje,
            BigDecimal kgBatchTotal) {

        TipoCalculoInsumo tipoSeguro = tipoCalculo != null
                ? tipoCalculo
                : TipoCalculoInsumo.PORCENTAJE_BATCH;

        if (tipoSeguro == TipoCalculoInsumo.FIJO) {
            if (cantidadKg == null || cantidadKg.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ReglaNegocioException("El ingrediente fijo debe tener cantidad kg mayor a cero");
            }
            return;
        }

        if (porcentaje == null || porcentaje.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException("El ingrediente porcentual debe tener porcentaje mayor a cero");
        }

        if (kgBatchTotal == null || kgBatchTotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaNegocioException(
                    "La versión de fórmula debe tener kg batch total para calcular ingredientes porcentuales");
        }
    }

    private BigDecimal calcularCantidadKgReferencia(
            TipoCalculoInsumo tipoCalculo,
            BigDecimal cantidadKg,
            BigDecimal porcentaje,
            BigDecimal kgBatchTotal) {

        return FormulaCalculoUtil.calcularCantidadPorBatch(
                tipoCalculo,
                cantidadKg,
                porcentaje,
                kgBatchTotal);
    }

    private BigDecimal calcularPorcentajeReferencia(
            TipoCalculoInsumo tipoCalculo,
            BigDecimal cantidadKg,
            BigDecimal porcentaje,
            BigDecimal kgBatchTotal) {

        TipoCalculoInsumo tipoSeguro = tipoCalculo != null
                ? tipoCalculo
                : TipoCalculoInsumo.PORCENTAJE_BATCH;

        if (tipoSeguro == TipoCalculoInsumo.PORCENTAJE_BATCH) {
            return porcentaje != null
                    ? porcentaje.setScale(6, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO.setScale(6, RoundingMode.HALF_UP);
        }

        return FormulaCalculoUtil.calcularPorcentajeInformativo(cantidadKg, kgBatchTotal);
    }

    private void recalcularDetallesPorCambioKgBatch(FormulaVersionEntity version) {
        if (version.getDetalles() == null || version.getDetalles().isEmpty()) {
            return;
        }

        for (FormulaDetalleEntity detalle : version.getDetalles()) {
            TipoCalculoInsumo tipoCalculo = detalle.getTipoCalculo() != null
                    ? detalle.getTipoCalculo()
                    : TipoCalculoInsumo.PORCENTAJE_BATCH;

            if (tipoCalculo == TipoCalculoInsumo.PORCENTAJE_BATCH) {
                detalle.setCantidadKg(FormulaCalculoUtil.calcularCantidadPorBatch(
                        tipoCalculo,
                        detalle.getCantidadKg(),
                        detalle.getPorcentaje(),
                        version.getKgBatchTotal()));
            } else {
                detalle.setPorcentaje(FormulaCalculoUtil.calcularPorcentajeInformativo(
                        detalle.getCantidadKg(),
                        version.getKgBatchTotal()));
            }

            formulaDetalleRepository.save(detalle);
        }
    }

    private void validarDetallesAntesDeVigente(FormulaVersionEntity version) {
        for (FormulaDetalleEntity detalle : version.getDetalles()) {
            TipoCalculoInsumo tipoCalculo = detalle.getTipoCalculo() != null
                    ? detalle.getTipoCalculo()
                    : TipoCalculoInsumo.PORCENTAJE_BATCH;

            validarDetalleFormula(
                    tipoCalculo,
                    detalle.getCantidadKg(),
                    detalle.getPorcentaje(),
                    version.getKgBatchTotal());
        }
    }

    private void validarVersionEditable(FormulaVersionEntity version) {
        if (version.getEstado() == FormulaVersionEntity.EstadoFormula.VIGENTE
                || version.getEstado() == FormulaVersionEntity.EstadoFormula.REEMPLAZADA
                || version.getEstado() == FormulaVersionEntity.EstadoFormula.INACTIVA) {
            throw new ReglaNegocioException(
                    "No se puede modificar una versión de fórmula en estado " + version.getEstado());
        }
    }

    private FormulaEntity buscarFormula(Long id) {
        return formulaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Fórmula no encontrada con id: " + id));
    }

    private FormulaVersionEntity buscarVersion(Long id) {
        return formulaVersionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Versión de fórmula no encontrada con id: " + id));
    }

    private CatalogoProductoEntity buscarProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + id));
    }

    private InsumoEntity buscarInsumo(Long id) {
        return insumoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Insumo no encontrado con id: " + id));
    }

    private UsuarioEntity buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));
    }

    private FormulaResumenResponse toFormulaResumenResponse(FormulaEntity formula) {
        return new FormulaResumenResponse(
                formula.getId(),
                formula.getNombre(),
                formula.getProducto().getId(),
                formula.getProducto().getNombre(),
                formula.getActivo());
    }

    private FormulaProduccionResponse toResponse(FormulaVersionEntity version) {
        var formula = version.getFormula();
        var producto = formula.getProducto();

        List<FormulaProduccionResponse.FormulaDetalleResponse> detalles = version.getDetalles() == null
                ? List.of()
                : version.getDetalles()
                        .stream()
                        .sorted(Comparator.comparing(
                                FormulaDetalleEntity::getOrdenAdicion,
                                Comparator.nullsLast(Integer::compareTo)))
                        .map(detalle -> {
                            TipoCalculoInsumo tipoCalculo = detalle.getTipoCalculo() != null
                                    ? detalle.getTipoCalculo()
                                    : TipoCalculoInsumo.PORCENTAJE_BATCH;

                            return new FormulaProduccionResponse.FormulaDetalleResponse(
                                    detalle.getId(),
                                    detalle.getInsumo().getId(),
                                    detalle.getInsumo().getCodigo(),
                                    detalle.getInsumo().getNombre(),
                                    detalle.getInsumo().getUnidadMedida(),
                                    detalle.getCantidadKg(),
                                    detalle.getPorcentaje(),
                                    tipoCalculo.name(),
                                    Boolean.TRUE.equals(detalle.getEsCritico()),
                                    detalle.getOrdenAdicion());
                        })
                        .toList();

        return new FormulaProduccionResponse(
                version.getId(),
                formula.getId(),
                formula.getNombre(),
                producto.getId(),
                producto.getNombre(),
                version.getVersion(),
                version.getFechaInicioVigencia(),
                version.getFechaFinVigencia(),
                version.getKgBatchTotal(),
                version.getReduccionEvaporacionPct(),
                version.getRendimientoTeoricoPct(),
                version.getBrixObjetivoMin(),
                version.getBrixObjetivoMax(),
                version.getEstado().name(),
                version.getAprobadoPor(),
                version.getDocumentoAprobacion(),
                version.getObservacionesTecnicas(),
                detalles);
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }

    public record FormulaResumenResponse(
            Long idFormula,
            String nombreFormula,
            Long idProducto,
            String nombreProducto,
            Boolean activo) {
    }
}
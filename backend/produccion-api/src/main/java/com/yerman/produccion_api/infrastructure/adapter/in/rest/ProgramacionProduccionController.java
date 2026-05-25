package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.ProgramacionProduccionRequest;
import com.yerman.produccion_api.application.dto.request.ProgramacionSkuRequest;
import com.yerman.produccion_api.application.dto.request.SimularProgramacionRequest;
import com.yerman.produccion_api.application.dto.response.ProgramacionProduccionResponse;
import com.yerman.produccion_api.application.dto.response.SimularProgramacionResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.application.mapper.ProgramacionProduccionMapper;
import com.yerman.produccion_api.application.service.SimulacionProgramacionService;
import com.yerman.produccion_api.domain.model.EstadoOrdenProduccion;
import com.yerman.produccion_api.domain.model.ProgramacionProduccion;
import com.yerman.produccion_api.domain.port.in.GestionProgramacionProduccionUseCase;
import com.yerman.produccion_api.infrastructure.entity.CatalogoSkuEntity;
import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionDetalleEntity;
import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.ProgramacionProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.ProgramacionSkuEntity;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.repository.CatalogoSkuJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.OrdenProduccionDetalleJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.OrdenProduccionJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ProgramacionProduccionJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ProgramacionSkuJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.UsuarioJpaRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/programaciones")
public class ProgramacionProduccionController {

    private final GestionProgramacionProduccionUseCase useCase;

    private final ProgramacionProduccionJpaRepository programacionRepository;

    private final ProgramacionSkuJpaRepository programacionSkuRepository;

    private final SimulacionProgramacionService simulacionService;

    private final UsuarioJpaRepository usuarioRepository;

    private final CatalogoSkuJpaRepository catalogoSkuRepository;

    private final OrdenProduccionJpaRepository ordenRepository;

    private final OrdenProduccionDetalleJpaRepository ordenDetalleRepository;

    public ProgramacionProduccionController(
            GestionProgramacionProduccionUseCase useCase,
            ProgramacionProduccionJpaRepository programacionRepository,
            ProgramacionSkuJpaRepository programacionSkuRepository,
            SimulacionProgramacionService simulacionService,
            UsuarioJpaRepository usuarioRepository,
            CatalogoSkuJpaRepository catalogoSkuRepository,
            OrdenProduccionJpaRepository ordenRepository,
            OrdenProduccionDetalleJpaRepository ordenDetalleRepository) {

        this.useCase = useCase;
        this.programacionRepository = programacionRepository;
        this.programacionSkuRepository = programacionSkuRepository;
        this.simulacionService = simulacionService;
        this.usuarioRepository = usuarioRepository;
        this.catalogoSkuRepository = catalogoSkuRepository;
        this.ordenRepository = ordenRepository;
        this.ordenDetalleRepository = ordenDetalleRepository;
    }

    @PostMapping
    @Transactional
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('JEFE_PRODUCCION')")
    public ResponseEntity<ProgramacionProduccionResponse> crear(
            @Valid @RequestBody ProgramacionProduccionRequest request) {

        UsuarioEntity usuarioAutenticado = obtenerUsuarioAutenticado();

        ProgramacionProduccion programacion = new ProgramacionProduccion();

        programacion.setFechaProduccion(
                request.getFechaProduccion());

        programacion.setIdLinea(
                request.getIdLinea());

        programacion.setIdProducto(
                request.getIdProducto());

        programacion.setIdTurno(
                request.getIdTurno());

        programacion.setNumBachesPlan(
                request.getNumBachesPlan());

        programacion.setKgBachePlan(
                request.getKgBachePlan());

        programacion.setIdFormulaVersion(
                request.getIdFormulaVersion());

        programacion.setIdJefeProduccion(
                usuarioAutenticado.getIdUsuario());

        programacion.setObservaciones(
                request.getObservaciones());

        ProgramacionProduccion creada = useCase.crear(programacion);

        ProgramacionProduccionEntity entity = buscarProgramacionEntity(creada.getId());

        guardarSkusProgramados(
                entity,
                request.getSkus());

        crearOrdenProduccionDesdeProgramacion(
                entity,
                request.getIdJefeLineaEjecutor());

        List<ProgramacionSkuEntity> skus = buscarSkus(entity.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ProgramacionProduccionMapper
                                .toResponse(entity, skus));
    }

    @PostMapping("/simular")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('JEFE_PRODUCCION')")
    public ResponseEntity<SimularProgramacionResponse> simular(
            @Valid @RequestBody SimularProgramacionRequest request) {

        SimularProgramacionResponse response = simulacionService.simular(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramacionProduccionResponse> obtenerPorId(
            @PathVariable Long id) {

        ProgramacionProduccionEntity entity = buscarProgramacionEntity(id);

        List<ProgramacionSkuEntity> skus = buscarSkus(id);

        return ResponseEntity.ok(
                ProgramacionProduccionMapper.toResponse(entity, skus));
    }

    @GetMapping
    public ResponseEntity<List<ProgramacionProduccionResponse>> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        List<ProgramacionProduccion> programaciones = fecha != null
                ? useCase.listarPorFecha(fecha)
                : useCase.listarTodas();

        List<ProgramacionProduccionResponse> response = programaciones.stream()
                .map(programacion -> {

                    ProgramacionProduccionEntity entity = buscarProgramacionEntity(
                            programacion.getId());

                    List<ProgramacionSkuEntity> skus = buscarSkus(entity.getId());

                    return ProgramacionProduccionMapper
                            .toResponse(entity, skus);

                })
                .toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('JEFE_PRODUCCION')")
    public ResponseEntity<ProgramacionProduccionResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {

        ProgramacionProduccion actualizada = useCase.cambiarEstado(id, estado);

        ProgramacionProduccionEntity entity = buscarProgramacionEntity(actualizada.getId());

        List<ProgramacionSkuEntity> skus = buscarSkus(entity.getId());

        return ResponseEntity.ok(
                ProgramacionProduccionMapper.toResponse(entity, skus));
    }

    private UsuarioEntity obtenerUsuarioAutenticado() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String ccUsuario = authentication.getName();

        return usuarioRepository.findByCc(ccUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario autenticado no encontrado"));
    }

    private void guardarSkusProgramados(
            ProgramacionProduccionEntity programacion,
            List<ProgramacionSkuRequest> skusRequest) {

        if (skusRequest == null || skusRequest.isEmpty()) {

            throw new ReglaNegocioException(
                    "Debe agregar al menos un SKU a la programación");
        }

        for (ProgramacionSkuRequest skuRequest : skusRequest) {

            boolean yaExiste = programacionSkuRepository
                    .existsByProgramacionIdAndSkuId(
                            programacion.getId(),
                            skuRequest.getIdSku());

            if (yaExiste) {

                throw new ReglaNegocioException(
                        "El SKU "
                                + skuRequest.getIdSku()
                                + " ya fue agregado a la programación");
            }

            CatalogoSkuEntity sku = catalogoSkuRepository
                    .findById(skuRequest.getIdSku())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "SKU no encontrado con id: "
                                    + skuRequest.getIdSku()));

            ProgramacionSkuEntity programacionSku = new ProgramacionSkuEntity();

            programacionSku.setProgramacion(programacion);

            programacionSku.setSku(sku);

            programacionSku.setUnidadesObjetivo(
                    skuRequest.getUnidadesObjetivo());

            programacionSku.setObservaciones(
                    skuRequest.getObservaciones());

            programacionSkuRepository.save(programacionSku);
        }
    }

    private void crearOrdenProduccionDesdeProgramacion(
            ProgramacionProduccionEntity programacion,
            Long idJefeLineaEjecutor) {

        if (ordenRepository.existsByProgramacionId(programacion.getId())) {
            return;
        }

        UsuarioEntity jefeLinea = usuarioRepository
                .findById(idJefeLineaEjecutor)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Jefe de línea no encontrado"));

        if (jefeLinea.getRol() != UsuarioEntity.Rol.JEFE_LINEA) {

            throw new ReglaNegocioException(
                    "El usuario seleccionado no tiene rol JEFE_LINEA");
        }

        OrdenProduccionEntity orden = new OrdenProduccionEntity();

        orden.setNumeroOrden(
                generarNumeroOrden());

        orden.setProgramacion(
                programacion);

        orden.setLinea(
                programacion.getLinea());

        orden.setProducto(
                programacion.getProducto());

        orden.setTurno(
                programacion.getTurno());

        orden.setJefeLineaEjecutor(
                jefeLinea);

        orden.setCreadaPor(
                programacion.getJefeProduccion());

        orden.setFechaProduccion(
                programacion.getFechaProduccion());

        orden.setEstado(
                EstadoOrdenProduccion.PROGRAMADA);

        orden.setObservaciones(
                "Orden generada automáticamente desde programación "
                        + programacion.getCodigoProgramacion());

        OrdenProduccionEntity ordenGuardada = ordenRepository.save(orden);

        List<ProgramacionSkuEntity> skusProgramados = buscarSkus(programacion.getId());

        int prioridad = 1;

        for (ProgramacionSkuEntity programacionSku : skusProgramados) {

            OrdenProduccionDetalleEntity detalle = new OrdenProduccionDetalleEntity();

            detalle.setOrden(
                    ordenGuardada);

            detalle.setProgramacionSku(
                    programacionSku);

            detalle.setSku(
                    programacionSku.getSku());

            detalle.setCantidadProgramada(
                    BigDecimal.valueOf(
                            programacionSku.getUnidadesObjetivo()));

            detalle.setUnidadProgramada(
                    "UNIDADES");

            detalle.setPrioridad(
                    prioridad++);

            detalle.setObservaciones(
                    programacionSku.getObservaciones());

            ordenDetalleRepository.save(detalle);
        }
    }

    private ProgramacionProduccionEntity buscarProgramacionEntity(Long id) {

        return programacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Programación no encontrada con id: " + id));
    }

    private List<ProgramacionSkuEntity> buscarSkus(Long idProgramacion) {

        return programacionSkuRepository
                .findByProgramacionIdOrderByIdAsc(idProgramacion);
    }

    private String generarNumeroOrden() {

        return "OP-"
                + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}
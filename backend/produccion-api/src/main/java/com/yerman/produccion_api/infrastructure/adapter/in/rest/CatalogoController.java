package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.CatalogoRequests.*;
import com.yerman.produccion_api.application.dto.response.CatalogoResponses.*;
import com.yerman.produccion_api.application.exception.RecursoDuplicadoException;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.infrastructure.entity.*;
import com.yerman.produccion_api.infrastructure.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogos")
@Transactional(readOnly = true)
public class CatalogoController {

    private final TurnoJpaRepository turnoRepository;
    private final ProveedorJpaRepository proveedorRepository;
    private final MarcaJpaRepository marcaRepository;
    private final CatalogoLineaJpaRepository lineaRepository;
    private final CatalogoProductoJpaRepository productoRepository;
    private final CatalogoSkuJpaRepository skuRepository;
    private final InsumoJpaRepository insumoRepository;
    private final MarmitaJpaRepository marmitaRepository;

    public CatalogoController(
            TurnoJpaRepository turnoRepository,
            ProveedorJpaRepository proveedorRepository,
            MarcaJpaRepository marcaRepository,
            CatalogoLineaJpaRepository lineaRepository,
            CatalogoProductoJpaRepository productoRepository,
            CatalogoSkuJpaRepository skuRepository,
            InsumoJpaRepository insumoRepository,
            MarmitaJpaRepository marmitaRepository) {
        this.turnoRepository = turnoRepository;
        this.proveedorRepository = proveedorRepository;
        this.marcaRepository = marcaRepository;
        this.lineaRepository = lineaRepository;
        this.productoRepository = productoRepository;
        this.skuRepository = skuRepository;
        this.insumoRepository = insumoRepository;
        this.marmitaRepository = marmitaRepository;
    }

    @GetMapping("/turnos")
    public List<TurnoResponse> listarTurnos(@RequestParam(defaultValue = "true") boolean activos) {
        return (activos ? turnoRepository.findByActivoTrue() : turnoRepository.findAll())
                .stream().map(this::toTurnoResponse).toList();
    }

    @GetMapping("/turnos/{id}")
    public TurnoResponse obtenerTurnoPorId(@PathVariable Long id) {
        return toTurnoResponse(buscarTurno(id));
    }

    @PostMapping("/turnos")
    @PreAuthorize("hasAnyRole('ADMIN','JEFE_PRODUCCION')")
    @Transactional
    public ResponseEntity<TurnoResponse> crearTurno(@Valid @RequestBody TurnoRequest request) {
        validarNombreNuevoTurno(request.nombre());
        TurnoEntity entity = new TurnoEntity();
        aplicarTurno(entity, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toTurnoResponse(turnoRepository.save(entity)));
    }

    @PutMapping("/turnos/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','JEFE_PRODUCCION')")
    @Transactional
    public TurnoResponse actualizarTurno(@PathVariable Long id, @Valid @RequestBody TurnoRequest request) {
        TurnoEntity entity = buscarTurno(id);
        if (!entity.getNombre().equalsIgnoreCase(limpiar(request.nombre()))) {
            validarNombreNuevoTurno(request.nombre());
        }
        aplicarTurno(entity, request);
        return toTurnoResponse(turnoRepository.save(entity));
    }

    @GetMapping("/proveedores")
    public List<ProveedorResponse> listarProveedores(@RequestParam(defaultValue = "true") boolean activos) {
        return (activos ? proveedorRepository.findByActivoTrue() : proveedorRepository.findAll())
                .stream().map(this::toProveedorResponse).toList();
    }

    @GetMapping("/proveedores/{id}")
    public ProveedorResponse obtenerProveedorPorId(@PathVariable Long id) {
        return toProveedorResponse(buscarProveedor(id));
    }

    @PostMapping("/proveedores")
    @PreAuthorize("hasAnyRole('ADMIN','JEFE_PRODUCCION')")
    @Transactional
    public ResponseEntity<ProveedorResponse> crearProveedor(@Valid @RequestBody ProveedorRequest request) {
        validarNombreNuevoProveedor(request.nombre());
        ProveedorEntity entity = new ProveedorEntity();
        aplicarProveedor(entity, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toProveedorResponse(proveedorRepository.save(entity)));
    }

    @PutMapping("/proveedores/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','JEFE_PRODUCCION')")
    @Transactional
    public ProveedorResponse actualizarProveedor(@PathVariable Long id, @Valid @RequestBody ProveedorRequest request) {
        ProveedorEntity entity = buscarProveedor(id);
        if (!entity.getNombre().equalsIgnoreCase(limpiar(request.nombre()))) {
            validarNombreNuevoProveedor(request.nombre());
        }
        aplicarProveedor(entity, request);
        return toProveedorResponse(proveedorRepository.save(entity));
    }

    @DeleteMapping("/proveedores/{id}")
    @PreAuthorize("hasRole('JEFE_PRODUCCION')")
    @Transactional
    public ResponseEntity<Void> eliminarProveedor(@PathVariable Long id) {
        ProveedorEntity entity = buscarProveedor(id);
        proveedorRepository.delete(entity);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/marcas")
    public List<MarcaResponse> listarMarcas(@RequestParam(defaultValue = "true") boolean activos) {
        return (activos ? marcaRepository.findByActivoTrue() : marcaRepository.findAll())
                .stream().map(this::toMarcaResponse).toList();
    }

    @GetMapping("/marcas/{id}")
    public MarcaResponse obtenerMarcaPorId(@PathVariable Long id) {
        return toMarcaResponse(buscarMarca(id));
    }

    @PostMapping("/marcas")
    @PreAuthorize("hasAnyRole('ADMIN','JEFE_PRODUCCION')")
    @Transactional
    public ResponseEntity<MarcaResponse> crearMarca(@Valid @RequestBody MarcaRequest request) {
        validarNombreNuevoMarca(request.nombre());
        MarcaEntity entity = new MarcaEntity();
        aplicarMarca(entity, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toMarcaResponse(marcaRepository.save(entity)));
    }

    @PutMapping("/marcas/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','JEFE_PRODUCCION')")
    @Transactional
    public MarcaResponse actualizarMarca(@PathVariable Long id, @Valid @RequestBody MarcaRequest request) {
        MarcaEntity entity = buscarMarca(id);
        if (!entity.getNombre().equalsIgnoreCase(limpiar(request.nombre()))) {
            validarNombreNuevoMarca(request.nombre());
        }
        aplicarMarca(entity, request);
        return toMarcaResponse(marcaRepository.save(entity));
    }

    @GetMapping("/lineas")
    public List<LineaResponse> listarLineas(@RequestParam(defaultValue = "true") boolean activos) {
        return (activos ? lineaRepository.findByActivoTrue() : lineaRepository.findAll())
                .stream().map(this::toLineaResponse).toList();
    }

    @GetMapping("/lineas/{id}")
    public LineaResponse obtenerLineaPorId(@PathVariable Long id) {
        return toLineaResponse(buscarLinea(id));
    }

    @PostMapping("/lineas")
    @PreAuthorize("hasAnyRole('ADMIN','JEFE_PRODUCCION')")
    @Transactional
    public ResponseEntity<LineaResponse> crearLinea(@Valid @RequestBody LineaRequest request) {
        validarNombreNuevoLinea(request.nombre());
        CatalogoLineaEntity entity = new CatalogoLineaEntity();
        aplicarLinea(entity, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toLineaResponse(lineaRepository.save(entity)));
    }

    @PutMapping("/lineas/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','JEFE_PRODUCCION')")
    @Transactional
    public LineaResponse actualizarLinea(@PathVariable Long id, @Valid @RequestBody LineaRequest request) {
        CatalogoLineaEntity entity = buscarLinea(id);
        if (!entity.getNombre().equalsIgnoreCase(limpiar(request.nombre()))) {
            validarNombreNuevoLinea(request.nombre());
        }
        aplicarLinea(entity, request);
        return toLineaResponse(lineaRepository.save(entity));
    }

    @GetMapping("/productos")
    public List<ProductoResponse> listarProductos(
            @RequestParam(defaultValue = "true") boolean activos,
            @RequestParam(required = false) Long idLinea) {
        List<CatalogoProductoEntity> productos = idLinea != null
                ? productoRepository.findByLineaId(idLinea)
                : (activos ? productoRepository.findByActivoTrue() : productoRepository.findAll());
        return productos.stream().map(this::toProductoResponse).toList();
    }

    @GetMapping("/productos/{id}")
    public ProductoResponse obtenerProductoPorId(@PathVariable Long id) {
        return toProductoResponse(buscarProducto(id));
    }

    @PostMapping("/productos")
    @PreAuthorize("hasAnyRole('ADMIN','JEFE_PRODUCCION')")
    @Transactional
    public ResponseEntity<ProductoResponse> crearProducto(@Valid @RequestBody ProductoRequest request) {
        validarNombreNuevoProducto(request.idLinea(), request.nombre());
        CatalogoProductoEntity entity = new CatalogoProductoEntity();
        aplicarProducto(entity, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toProductoResponse(productoRepository.save(entity)));
    }

    @PutMapping("/productos/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','JEFE_PRODUCCION')")
    @Transactional
    public ProductoResponse actualizarProducto(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        CatalogoProductoEntity entity = buscarProducto(id);
        boolean cambiaNombre = !entity.getNombre().equalsIgnoreCase(limpiar(request.nombre()));
        boolean cambiaLinea = !entity.getLinea().getId().equals(request.idLinea());
        if (cambiaNombre || cambiaLinea) {
            validarNombreNuevoProducto(request.idLinea(), request.nombre());
        }
        aplicarProducto(entity, request);
        return toProductoResponse(productoRepository.save(entity));
    }

    @GetMapping("/skus")
    public List<SkuResponse> listarSkus(
            @RequestParam(defaultValue = "true") boolean activos,
            @RequestParam(required = false) Long idProducto) {
        List<CatalogoSkuEntity> skus = idProducto != null
                ? skuRepository.findByProductoId(idProducto)
                : (activos ? skuRepository.findByActivoTrue() : skuRepository.findAll());
        return skus.stream().map(this::toSkuResponse).toList();
    }

    @GetMapping("/skus/{codigoSku}")
    public SkuResponse obtenerSkuPorCodigo(@PathVariable String codigoSku) {
        return toSkuResponse(skuRepository.findByCodigoSku(codigoSku)
                .orElseThrow(() -> new RecursoNoEncontradoException("SKU no encontrado: " + codigoSku)));
    }

    @PostMapping("/skus")
    @PreAuthorize("hasAnyRole('ADMIN','JEFE_PRODUCCION')")
    @Transactional
    public ResponseEntity<SkuResponse> crearSku(@Valid @RequestBody SkuRequest request) {
        validarCodigoSkuNuevo(request.codigoSku());
        CatalogoSkuEntity entity = new CatalogoSkuEntity();
        aplicarSku(entity, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toSkuResponse(skuRepository.save(entity)));
    }

    @PutMapping("/skus/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','JEFE_PRODUCCION')")
    @Transactional
    public SkuResponse actualizarSku(@PathVariable Long id, @Valid @RequestBody SkuRequest request) {
        CatalogoSkuEntity entity = buscarSku(id);
        if (!entity.getCodigoSku().equalsIgnoreCase(limpiar(request.codigoSku()))) {
            validarCodigoSkuNuevo(request.codigoSku());
        }
        aplicarSku(entity, request);
        return toSkuResponse(skuRepository.save(entity));
    }

    @DeleteMapping("/skus/{id}")
    @PreAuthorize("hasRole('JEFE_PRODUCCION')")
    @Transactional
    public ResponseEntity<Void> eliminarSku(@PathVariable Long id) {
        CatalogoSkuEntity entity = buscarSku(id);
        skuRepository.delete(entity);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/insumos")
    public List<InsumoResponse> listarInsumos(@RequestParam(defaultValue = "true") boolean activos) {
        return (activos ? insumoRepository.findByActivoTrue() : insumoRepository.findAll())
                .stream().map(this::toInsumoResponse).toList();
    }

    @GetMapping("/insumos/{id}")
    public InsumoResponse obtenerInsumoPorId(@PathVariable Long id) {
        return toInsumoResponse(buscarInsumo(id));
    }

    @PostMapping("/insumos")
    @PreAuthorize("hasAnyRole('ADMIN','JEFE_PRODUCCION')")
    @Transactional
    public ResponseEntity<InsumoResponse> crearInsumo(@Valid @RequestBody InsumoRequest request) {
        validarNombreNuevoInsumo(request.nombre());
        validarCodigoNuevoInsumo(request.codigo());
        InsumoEntity entity = new InsumoEntity();
        aplicarInsumo(entity, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toInsumoResponse(insumoRepository.save(entity)));
    }

    @PutMapping("/insumos/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','JEFE_PRODUCCION')")
    @Transactional
    public InsumoResponse actualizarInsumo(@PathVariable Long id, @Valid @RequestBody InsumoRequest request) {
        InsumoEntity entity = buscarInsumo(id);
        if (!entity.getNombre().equalsIgnoreCase(limpiar(request.nombre()))) {
            validarNombreNuevoInsumo(request.nombre());
        }
        if (request.codigo() != null && !request.codigo().isBlank()
                && (entity.getCodigo() == null || !entity.getCodigo().equalsIgnoreCase(limpiar(request.codigo())))) {
            validarCodigoNuevoInsumo(request.codigo());
        }
        aplicarInsumo(entity, request);
        return toInsumoResponse(insumoRepository.save(entity));
    }

    @DeleteMapping("/insumos/{id}")
    @PreAuthorize("hasRole('JEFE_PRODUCCION')")
    @Transactional
    public ResponseEntity<Void> eliminarInsumo(@PathVariable Long id) {
        InsumoEntity entity = buscarInsumo(id);
        insumoRepository.delete(entity);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/marmitas")
    public List<MarmitaResponse> listarMarmitas(@RequestParam(defaultValue = "true") boolean activos) {
        return (activos ? marmitaRepository.findByActivaTrue() : marmitaRepository.findAll())
                .stream().map(m -> new MarmitaResponse(m.getId(), m.getNombre(), m.getActiva()))
                .toList();
    }

    private void aplicarTurno(TurnoEntity entity, TurnoRequest request) {
        entity.setNombre(limpiar(request.nombre()));
        entity.setHoraInicio(request.horaInicio());
        entity.setHoraFin(request.horaFin());
        entity.setActivo(valorActivo(request.activo()));
    }

    private void aplicarProveedor(ProveedorEntity entity, ProveedorRequest request) {
        entity.setNombre(limpiar(request.nombre()));
        entity.setActivo(valorActivo(request.activo()));
    }

    private void aplicarMarca(MarcaEntity entity, MarcaRequest request) {
        entity.setNombre(limpiar(request.nombre()));
        entity.setEsPropia(request.esPropia());
        entity.setActivo(valorActivo(request.activo()));
    }

    private void aplicarLinea(CatalogoLineaEntity entity, LineaRequest request) {
        entity.setNombre(limpiar(request.nombre()));
        entity.setActivo(valorActivo(request.activo()));
    }

    private void aplicarProducto(CatalogoProductoEntity entity, ProductoRequest request) {
        entity.setNombre(limpiar(request.nombre()));
        entity.setLinea(buscarLinea(request.idLinea()));
        entity.setActivo(valorActivo(request.activo()));
    }

    private void aplicarSku(CatalogoSkuEntity entity, SkuRequest request) {
        entity.setCodigoSku(limpiar(request.codigoSku()));
        entity.setDescripcion(limpiar(request.descripcion()));
        entity.setProducto(buscarProducto(request.idProducto()));
        entity.setMarca(buscarMarca(request.idMarca()));
        entity.setPesoNetoGr(request.pesoNetoGr());
        entity.setUnidadMedida(limpiar(request.unidadMedida()));
        entity.setTipoEnvase(parseTipoEnvase(request.tipoEnvase()));
        entity.setUnidadesPorCaja(request.unidadesPorCaja());
        entity.setEsExport(request.esExport() != null ? request.esExport() : false);
        entity.setActivo(valorActivo(request.activo()));
    }

    private void aplicarInsumo(InsumoEntity entity, InsumoRequest request) {
        entity.setCodigo(limpiar(request.codigo()));
        entity.setNombre(limpiar(request.nombre()));
        entity.setDescripcion(limpiar(request.descripcion()));
        entity.setTipo(parseTipoInsumo(request.tipo()));
        entity.setUnidadMedida(limpiar(request.unidadMedida()));
        entity.setStockMinimo(request.stockMinimo());
        entity.setProveedor(request.idProveedor() != null ? buscarProveedor(request.idProveedor()) : null);
        entity.setActivo(valorActivo(request.activo()));
    }

    private TurnoEntity buscarTurno(Long id) {
        return turnoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Turno no encontrado con id: " + id));
    }

    private ProveedorEntity buscarProveedor(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no encontrado con id: " + id));
    }

    private MarcaEntity buscarMarca(Long id) {
        return marcaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Marca no encontrada con id: " + id));
    }

    private CatalogoLineaEntity buscarLinea(Long id) {
        return lineaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Linea no encontrada con id: " + id));
    }

    private CatalogoProductoEntity buscarProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con id: " + id));
    }

    private CatalogoSkuEntity buscarSku(Long id) {
        return skuRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("SKU no encontrado con id: " + id));
    }

    private InsumoEntity buscarInsumo(Long id) {
        return insumoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Insumo no encontrado con id: " + id));
    }

    private void validarNombreNuevoTurno(String nombre) {
        if (turnoRepository.existsByNombre(limpiar(nombre))) {
            throw new RecursoDuplicadoException("Ya existe un turno con nombre: " + nombre);
        }
    }

    private void validarNombreNuevoProveedor(String nombre) {
        if (proveedorRepository.existsByNombre(limpiar(nombre))) {
            throw new RecursoDuplicadoException("Ya existe un proveedor con nombre: " + nombre);
        }
    }

    private void validarNombreNuevoMarca(String nombre) {
        if (marcaRepository.existsByNombre(limpiar(nombre))) {
            throw new RecursoDuplicadoException("Ya existe una marca con nombre: " + nombre);
        }
    }

    private void validarNombreNuevoLinea(String nombre) {
        if (lineaRepository.existsByNombre(limpiar(nombre))) {
            throw new RecursoDuplicadoException("Ya existe una linea con nombre: " + nombre);
        }
    }

    private void validarNombreNuevoProducto(Long idLinea, String nombre) {
        if (productoRepository.existsByLineaIdAndNombre(idLinea, limpiar(nombre))) {
            throw new RecursoDuplicadoException("Ya existe un producto con ese nombre en la linea indicada");
        }
    }

    private void validarCodigoSkuNuevo(String codigoSku) {
        if (skuRepository.existsByCodigoSku(limpiar(codigoSku))) {
            throw new RecursoDuplicadoException("Ya existe un SKU con codigo: " + codigoSku);
        }
    }

    private void validarNombreNuevoInsumo(String nombre) {
        if (insumoRepository.existsByNombre(limpiar(nombre))) {
            throw new RecursoDuplicadoException("Ya existe un insumo con nombre: " + nombre);
        }
    }

    private void validarCodigoNuevoInsumo(String codigo) {
        if (codigo != null && !codigo.isBlank() && insumoRepository.existsByCodigo(limpiar(codigo))) {
            throw new RecursoDuplicadoException("Ya existe un insumo con codigo: " + codigo);
        }
    }

    private CatalogoSkuEntity.TipoEnvase parseTipoEnvase(String value) {
        if (value == null || value.trim().isEmpty()) {
            return CatalogoSkuEntity.TipoEnvase.OTRO;
        }
        try {
            return CatalogoSkuEntity.TipoEnvase.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ReglaNegocioException("Tipo de envase invalido: " + value);
        }
    }

    private InsumoEntity.TipoInsumo parseTipoInsumo(String value) {
        if (value == null || value.trim().isEmpty()) {
            return InsumoEntity.TipoInsumo.MATERIA_PRIMA;
        }
        try {
            return InsumoEntity.TipoInsumo.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ReglaNegocioException("Tipo de insumo invalido: " + value);
        }
    }

    private Boolean valorActivo(Boolean activo) {
        return activo != null ? activo : true;
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }

    private TurnoResponse toTurnoResponse(TurnoEntity entity) {
        return new TurnoResponse(entity.getId(), entity.getNombre(), entity.getHoraInicio(), entity.getHoraFin(),
                entity.getActivo(), entity.getCreatedAt(), entity.getUpdatedAt());
    }

    private ProveedorResponse toProveedorResponse(ProveedorEntity entity) {
        return new ProveedorResponse(entity.getId(), entity.getNombre(), entity.getActivo(), entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    private MarcaResponse toMarcaResponse(MarcaEntity entity) {
        return new MarcaResponse(entity.getId(), entity.getNombre(), entity.getEsPropia(), entity.getActivo(),
                entity.getCreatedAt(), entity.getUpdatedAt());
    }

    private LineaResponse toLineaResponse(CatalogoLineaEntity entity) {
        return new LineaResponse(entity.getId(), entity.getNombre(), entity.getActivo(), entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    private ProductoResponse toProductoResponse(CatalogoProductoEntity entity) {
        return new ProductoResponse(entity.getId(), entity.getNombre(), entity.getLinea().getId(),
                entity.getLinea().getNombre(), entity.getActivo(), entity.getCreatedAt(), entity.getUpdatedAt());
    }

    private SkuResponse toSkuResponse(CatalogoSkuEntity entity) {
        return new SkuResponse(entity.getId(), entity.getCodigoSku(), entity.getDescripcion(),
                entity.getProducto().getId(), entity.getProducto().getNombre(), entity.getMarca().getId(),
                entity.getMarca().getNombre(), entity.getPesoNetoGr(), entity.getUnidadMedida(), entity.getTipoEnvase().name(),
                entity.getUnidadesPorCaja(), entity.getEsExport(), entity.getActivo(), entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    private InsumoResponse toInsumoResponse(InsumoEntity entity) {
        return new InsumoResponse(
                entity.getId(),
                entity.getCodigo(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getTipo().name(),
                entity.getUnidadMedida(),
                entity.getStockMinimo(),
                entity.getProveedor() != null ? entity.getProveedor().getId() : null,
                entity.getProveedor() != null ? entity.getProveedor().getNombre() : null,
                entity.getActivo(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}

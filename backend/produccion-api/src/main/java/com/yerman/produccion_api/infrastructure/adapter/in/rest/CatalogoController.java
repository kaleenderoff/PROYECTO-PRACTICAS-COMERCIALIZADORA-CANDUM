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

    public CatalogoController(
            TurnoJpaRepository turnoRepository,
            ProveedorJpaRepository proveedorRepository,
            MarcaJpaRepository marcaRepository,
            CatalogoLineaJpaRepository lineaRepository,
            CatalogoProductoJpaRepository productoRepository,
            CatalogoSkuJpaRepository skuRepository) {
        this.turnoRepository = turnoRepository;
        this.proveedorRepository = proveedorRepository;
        this.marcaRepository = marcaRepository;
        this.lineaRepository = lineaRepository;
        this.productoRepository = productoRepository;
        this.skuRepository = skuRepository;
    }

    @GetMapping("/turnos")
    public List<TurnoResponse> listarTurnos(@RequestParam(defaultValue = "true") boolean activos) {
        return (activos ? turnoRepository.findByActivoTrue() : turnoRepository.findAll())
                .stream().map(this::toTurnoResponse).toList();
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

    @GetMapping("/marcas")
    public List<MarcaResponse> listarMarcas(@RequestParam(defaultValue = "true") boolean activos) {
        return (activos ? marcaRepository.findByActivoTrue() : marcaRepository.findAll())
                .stream().map(this::toMarcaResponse).toList();
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
        entity.setTipoEnvase(parseTipoEnvase(request.tipoEnvase()));
        entity.setUnidadesPorCaja(request.unidadesPorCaja());
        entity.setEsExport(request.esExport() != null ? request.esExport() : false);
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
                entity.getMarca().getNombre(), entity.getPesoNetoGr(), entity.getTipoEnvase().name(),
                entity.getUnidadesPorCaja(), entity.getEsExport(), entity.getActivo(), entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}

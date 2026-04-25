package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.OrdenProduccionDetalleRequest;
import com.yerman.produccion_api.application.dto.response.OrdenProduccionDetalleResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.mapper.OrdenProduccionDetalleRestMapper;
import com.yerman.produccion_api.domain.model.OrdenProduccionDetalle;
import com.yerman.produccion_api.domain.port.in.GestionOrdenProduccionDetalleUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes-produccion")
public class OrdenProduccionDetalleController {

    private final GestionOrdenProduccionDetalleUseCase useCase;

    public OrdenProduccionDetalleController(GestionOrdenProduccionDetalleUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/{idOrden}/detalles")
    @ResponseStatus(HttpStatus.CREATED)
    public OrdenProduccionDetalleResponse agregarDetalle(
            @PathVariable Long idOrden,
            @Valid @RequestBody OrdenProduccionDetalleRequest request) {

        OrdenProduccionDetalle detalle = new OrdenProduccionDetalle();
        detalle.setIdOrden(idOrden);
        detalle.setIdProgramacionSku(request.getIdProgramacionSku());
        detalle.setIdSku(request.getIdSku());
        detalle.setCantidadProgramada(request.getCantidadProgramada());
        detalle.setUnidadProgramada(request.getUnidadProgramada());
        detalle.setPrioridad(request.getPrioridad());
        detalle.setObservaciones(request.getObservaciones());

        return OrdenProduccionDetalleRestMapper.toResponse(useCase.agregarDetalle(detalle));
    }

    @GetMapping("/{idOrden}/detalles")
    public List<OrdenProduccionDetalleResponse> listarPorOrden(@PathVariable Long idOrden) {
        return useCase.listarPorOrden(idOrden)
                .stream()
                .map(OrdenProduccionDetalleRestMapper::toResponse)
                .toList();
    }

    @GetMapping("/detalles/{idDetalle}")
    public OrdenProduccionDetalleResponse obtenerPorId(@PathVariable Long idDetalle) {
        return useCase.obtenerPorId(idDetalle)
                .map(OrdenProduccionDetalleRestMapper::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un detalle de orden de producción con ID: " + idDetalle));
    }

    @DeleteMapping("/detalles/{idDetalle}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long idDetalle) {
        useCase.eliminar(idDetalle);
    }
}
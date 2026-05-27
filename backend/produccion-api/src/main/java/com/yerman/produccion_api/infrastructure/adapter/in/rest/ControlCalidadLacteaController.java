package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.CalidadRecepcionLecheRequest;
import com.yerman.produccion_api.application.dto.request.ControlCalidadProcesoRequest;
import com.yerman.produccion_api.application.dto.request.ControlPesoProductoRequest;
import com.yerman.produccion_api.application.dto.response.CalidadRecepcionLecheResponse;
import com.yerman.produccion_api.application.dto.response.ControlCalidadProcesoResponse;
import com.yerman.produccion_api.application.dto.response.ControlPesoProductoResponse;
import com.yerman.produccion_api.application.dto.response.EstadoCalidadRecepcionResponse;
import com.yerman.produccion_api.application.service.GestionControlCalidadLacteaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/controles-calidad-lactea")
public class ControlCalidadLacteaController {

    private final GestionControlCalidadLacteaService service;

    public ControlCalidadLacteaController(GestionControlCalidadLacteaService service) {
        this.service = service;
    }

    @PostMapping("/recepcion")
    @ResponseStatus(HttpStatus.CREATED)
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('AUXILIAR_CALIDAD')")
    public CalidadRecepcionLecheResponse registrarRecepcion(
            @Valid @RequestBody CalidadRecepcionLecheRequest request) {
        return service.registrarRecepcion(request);
    }

    @GetMapping("/recepcion/{idRecepcionLeche}")
    public List<CalidadRecepcionLecheResponse> listarRecepcion(@PathVariable Long idRecepcionLeche) {
        return service.listarRecepcion(idRecepcionLeche);
    }

    @PostMapping("/proceso")
    @ResponseStatus(HttpStatus.CREATED)
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('AUXILIAR_CALIDAD')")
    public ControlCalidadProcesoResponse registrarProceso(@Valid @RequestBody ControlCalidadProcesoRequest request) {
        return service.registrarProceso(request);
    }

    @GetMapping("/proceso/orden/{idOrdenProduccion}")
    public List<ControlCalidadProcesoResponse> listarProcesosPorOrden(@PathVariable Long idOrdenProduccion) {
        return service.listarProcesosPorOrden(idOrdenProduccion);
    }

    @PostMapping("/peso")
    @ResponseStatus(HttpStatus.CREATED)
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('AUXILIAR_CALIDAD')")
    public ControlPesoProductoResponse registrarPeso(@Valid @RequestBody ControlPesoProductoRequest request) {
        return service.registrarPeso(request);
    }

    @GetMapping("/peso/orden/{idOrdenProduccion}")
    public List<ControlPesoProductoResponse> listarPesosPorOrden(@PathVariable Long idOrdenProduccion) {
        return service.listarPesosPorOrden(idOrdenProduccion);
    }

    @GetMapping("/recepciones/estados")
    public List<EstadoCalidadRecepcionResponse> listarEstadosRecepcion() {
        return service.listarEstadosRecepcion();
    }
}

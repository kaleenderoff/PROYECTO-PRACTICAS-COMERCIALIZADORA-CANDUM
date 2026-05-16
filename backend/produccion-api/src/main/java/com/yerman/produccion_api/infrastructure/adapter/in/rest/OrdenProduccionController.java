package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.CancelarOrdenProduccionRequest;
import com.yerman.produccion_api.application.dto.request.IniciarOrdenProduccionRequest;
import com.yerman.produccion_api.application.dto.request.OrdenProduccionRequest;
import com.yerman.produccion_api.application.dto.request.RegistrarProduccionSkuRequest;
import com.yerman.produccion_api.application.dto.response.OrdenProduccionResponse;
import com.yerman.produccion_api.application.mapper.OrdenProduccionRestMapper;
import com.yerman.produccion_api.domain.port.in.GestionOrdenProduccionUseCase;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ordenes-produccion")
public class OrdenProduccionController {

    private final GestionOrdenProduccionUseCase gestionOrdenProduccionUseCase;

    public OrdenProduccionController(GestionOrdenProduccionUseCase gestionOrdenProduccionUseCase) {
        this.gestionOrdenProduccionUseCase = gestionOrdenProduccionUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdenProduccionResponse crearDesdeProgramacion(@Valid @RequestBody OrdenProduccionRequest request) {
        return OrdenProduccionRestMapper.toResponse(
                gestionOrdenProduccionUseCase.crearDesdeProgramacion(
                        request.getIdProgramacion(),
                        request.getIdCreadaPor(),
                        request.getObservaciones()));
    }

    @GetMapping("/{id}")
    public OrdenProduccionResponse obtenerPorId(@PathVariable Long id) {
        return gestionOrdenProduccionUseCase.obtenerPorId(id)
                .map(OrdenProduccionRestMapper::toResponse)
                .orElseThrow();
    }

    @GetMapping
    public List<OrdenProduccionResponse> listar(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        if (fecha != null) {
            return gestionOrdenProduccionUseCase.listarPorFecha(fecha)
                    .stream()
                    .map(OrdenProduccionRestMapper::toResponse)
                    .toList();
        }

        return gestionOrdenProduccionUseCase.listarTodas()
                .stream()
                .map(OrdenProduccionRestMapper::toResponse)
                .toList();
    }

    @PatchMapping("/{id}/iniciar")
    public OrdenProduccionResponse iniciar(
            @PathVariable Long id,
            @Valid @RequestBody IniciarOrdenProduccionRequest request) {

        return OrdenProduccionRestMapper.toResponse(
                gestionOrdenProduccionUseCase.iniciar(id, request.getIdJefeLineaEjecutor()));
    }

    @PatchMapping("/{id}/finalizar")
    public OrdenProduccionResponse finalizar(@PathVariable Long id) {
        return OrdenProduccionRestMapper.toResponse(
                gestionOrdenProduccionUseCase.finalizar(id));
    }

    @PatchMapping("/{id}/cancelar")
    public OrdenProduccionResponse cancelar(
            @PathVariable Long id,
            @Valid @RequestBody CancelarOrdenProduccionRequest request) {

        return OrdenProduccionRestMapper.toResponse(
                gestionOrdenProduccionUseCase.cancelar(id, request.getObservaciones()));
    }

    @PatchMapping("/{id}/skus")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registrarSkus(@PathVariable Long id, @RequestBody List<RegistrarProduccionSkuRequest> request) {
        gestionOrdenProduccionUseCase.registrarProduccionSku(id, request);
    }

    @PatchMapping("/{id}/tanque")
    public OrdenProduccionResponse actualizarTanque(@PathVariable Long id, @RequestParam Long idTanque) {
        return OrdenProduccionRestMapper.toResponse(
                gestionOrdenProduccionUseCase.actualizarTanqueLeche(id, idTanque));
    }
}
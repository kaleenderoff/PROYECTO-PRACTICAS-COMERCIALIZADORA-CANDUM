package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.MovimientoLecheRequest;
import com.yerman.produccion_api.application.dto.response.MovimientoLecheResponse;
import com.yerman.produccion_api.application.mapper.MovimientoLecheRestMapper;
import com.yerman.produccion_api.domain.model.TipoMovimientoLeche;
import com.yerman.produccion_api.domain.port.in.GestionMovimientoLecheUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/movimientos-leche")
public class MovimientoLecheController {

    private final GestionMovimientoLecheUseCase useCase;

    public MovimientoLecheController(GestionMovimientoLecheUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoLecheResponse registrar(
            @Valid @RequestBody MovimientoLecheRequest request) {

        return MovimientoLecheRestMapper.toResponse(
                useCase.registrarMovimiento(
                        request.getIdTanque(),
                        TipoMovimientoLeche.valueOf(request.getTipoMovimiento()),
                        request.getCantidadLitros(),
                        request.getIdUsuario(),
                        request.getReferencia(),
                        request.getObservaciones()));
    }

    @GetMapping("/saldo/{idTanque}")
    public BigDecimal saldo(@PathVariable Long idTanque) {
        return useCase.obtenerSaldoActualPorTanque(idTanque);
    }

    @GetMapping("/tanque/{idTanque}")
    public List<MovimientoLecheResponse> historial(@PathVariable Long idTanque) {

        return useCase.listarPorTanque(idTanque)
                .stream()
                .map(MovimientoLecheRestMapper::toResponse)
                .toList();
    }

    @GetMapping("/fecha/{fecha}")
    public List<MovimientoLecheResponse> movimientosPorFecha(
            @PathVariable LocalDate fecha) {

        return useCase.listarPorFecha(fecha)
                .stream()
                .map(MovimientoLecheRestMapper::toResponse)
                .toList();
    }
}
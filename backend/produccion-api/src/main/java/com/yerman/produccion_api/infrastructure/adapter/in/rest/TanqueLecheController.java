package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.response.SaldoTanqueLecheResponse;
import com.yerman.produccion_api.application.mapper.SaldoTanqueLecheRestMapper;
import com.yerman.produccion_api.domain.port.in.ConsultaSaldoTanqueLecheUseCase;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tanques-leche")
public class TanqueLecheController {

    private final ConsultaSaldoTanqueLecheUseCase useCase;

    public TanqueLecheController(ConsultaSaldoTanqueLecheUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/saldos")
    public List<SaldoTanqueLecheResponse> listarSaldos() {
        return useCase.listarSaldos()
                .stream()
                .map(SaldoTanqueLecheRestMapper::toResponse)
                .toList();
    }

    @GetMapping("/{idTanque}/saldo")
    public SaldoTanqueLecheResponse obtenerSaldo(@PathVariable Long idTanque) {
        return SaldoTanqueLecheRestMapper.toResponse(
                useCase.obtenerSaldo(idTanque));
    }
}

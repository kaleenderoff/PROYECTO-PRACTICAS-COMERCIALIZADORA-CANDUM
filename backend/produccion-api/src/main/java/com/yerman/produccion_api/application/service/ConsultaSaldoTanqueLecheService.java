package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.domain.model.SaldoTanqueLeche;
import com.yerman.produccion_api.domain.port.in.ConsultaSaldoTanqueLecheUseCase;
import com.yerman.produccion_api.domain.port.out.TanqueLecheRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaSaldoTanqueLecheService implements ConsultaSaldoTanqueLecheUseCase {

    private final TanqueLecheRepositoryPort tanqueLecheRepositoryPort;

    public ConsultaSaldoTanqueLecheService(TanqueLecheRepositoryPort tanqueLecheRepositoryPort) {
        this.tanqueLecheRepositoryPort = tanqueLecheRepositoryPort;
    }

    @Override
    public List<SaldoTanqueLeche> listarSaldos() {
        return tanqueLecheRepositoryPort.listarSaldos();
    }

    @Override
    public SaldoTanqueLeche obtenerSaldo(Long idTanque) {
        return tanqueLecheRepositoryPort.obtenerSaldo(idTanque)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tanque de leche no encontrado"));
    }
}

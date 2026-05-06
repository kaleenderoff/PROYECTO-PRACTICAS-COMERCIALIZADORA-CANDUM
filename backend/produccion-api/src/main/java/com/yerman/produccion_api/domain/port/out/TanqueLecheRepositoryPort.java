package com.yerman.produccion_api.domain.port.out;

import com.yerman.produccion_api.domain.model.SaldoTanqueLeche;

import java.util.List;
import java.util.Optional;

public interface TanqueLecheRepositoryPort {

    List<SaldoTanqueLeche> listarSaldos();

    Optional<SaldoTanqueLeche> obtenerSaldo(Long idTanque);
}

package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.domain.model.SaldoTanqueLeche;
import com.yerman.produccion_api.domain.port.out.MovimientoLecheRepositoryPort;
import com.yerman.produccion_api.domain.port.out.TanqueLecheRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.TanqueLecheEntity;
import com.yerman.produccion_api.infrastructure.repository.TanqueLecheJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TanqueLecheJpaAdapter implements TanqueLecheRepositoryPort {

    private final TanqueLecheJpaRepository repository;
    private final MovimientoLecheRepositoryPort movimientoLecheRepositoryPort;

    public TanqueLecheJpaAdapter(
            TanqueLecheJpaRepository repository,
            MovimientoLecheRepositoryPort movimientoLecheRepositoryPort) {
        this.repository = repository;
        this.movimientoLecheRepositoryPort = movimientoLecheRepositoryPort;
    }

    @Override
    public List<SaldoTanqueLeche> listarSaldos() {
        return repository.findAllByOrderByIdAsc()
                .stream()
                .map(this::toSaldo)
                .toList();
    }

    @Override
    public Optional<SaldoTanqueLeche> obtenerSaldo(Long idTanque) {
        return repository.findById(idTanque)
                .map(this::toSaldo);
    }

    private SaldoTanqueLeche toSaldo(TanqueLecheEntity entity) {
        return new SaldoTanqueLeche(
                entity.getId(),
                entity.getNombre(),
                entity.getTipo(),
                movimientoLecheRepositoryPort.obtenerSaldoActualPorTanque(entity.getId()),
                entity.getActivo());
    }
}

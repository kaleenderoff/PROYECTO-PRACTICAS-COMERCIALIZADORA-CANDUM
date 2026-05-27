package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.mapper.EjecucionBatchMapper;
import com.yerman.produccion_api.domain.model.EjecucionBatch;
import com.yerman.produccion_api.domain.model.EjecucionBatch.EstadoBatch;
import com.yerman.produccion_api.domain.port.out.EjecucionBatchRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.EjecucionBatchEntity;
import com.yerman.produccion_api.infrastructure.entity.MarmitaEntity;
import com.yerman.produccion_api.infrastructure.entity.MovimientoLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionEntity;
import com.yerman.produccion_api.infrastructure.repository.EjecucionBatchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EjecucionBatchJpaAdapter implements EjecucionBatchRepositoryPort {

    private final EjecucionBatchRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public EjecucionBatchJpaAdapter(EjecucionBatchRepository repository) {
        this.repository = repository;
    }

    @Override
    public EjecucionBatch guardar(EjecucionBatch domain) {
        OrdenProduccionEntity orden = entityManager.getReference(OrdenProduccionEntity.class, domain.getIdOrdenProduccion());
        MarmitaEntity marmita = entityManager.getReference(MarmitaEntity.class, domain.getIdMarmita());
        
        EjecucionBatchEntity entity = EjecucionBatchMapper.toEntity(domain, orden, marmita);
        if (domain.getIdMovimientoLeche() != null) {
            entity.setMovimientoLeche(entityManager.getReference(
                    MovimientoLecheEntity.class,
                    domain.getIdMovimientoLeche()));
        }
        return EjecucionBatchMapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<EjecucionBatch> obtenerPorId(Long id) {
        return repository.findById(id).map(EjecucionBatchMapper::toDomain);
    }

    @Override
    public List<EjecucionBatch> listarPorOrden(Long idOrden) {
        return repository.findByOrdenProduccionId(idOrden).stream()
                .map(EjecucionBatchMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<EjecucionBatch> obtenerPorOrdenYNumero(Long idOrden, Integer numeroBatch) {
        return repository.findByOrdenProduccionIdAndNumeroBatch(idOrden, numeroBatch)
                .map(EjecucionBatchMapper::toDomain);
    }

    @Override
    public boolean existeMarmitaOcupadaEnOrden(Long idMarmita, Long idOrden) {
        return repository.existsByMarmitaAndEstadoAndOrden(idMarmita, EstadoBatch.EN_PROCESO, idOrden);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}

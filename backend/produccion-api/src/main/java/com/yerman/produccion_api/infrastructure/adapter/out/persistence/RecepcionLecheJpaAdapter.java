package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.mapper.RecepcionLecheMapper;
import com.yerman.produccion_api.domain.model.RecepcionLeche;
import com.yerman.produccion_api.domain.port.out.RecepcionLecheRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.MovimientoLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.RecepcionLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.RecepcionLechePesajeEntity;
import com.yerman.produccion_api.infrastructure.entity.TanqueLecheEntity;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.repository.RecepcionLecheJpaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class RecepcionLecheJpaAdapter implements RecepcionLecheRepositoryPort {

    private final RecepcionLecheJpaRepository repository;
    private final EntityManager entityManager;

    public RecepcionLecheJpaAdapter(
            RecepcionLecheJpaRepository repository,
            EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    @Override
    public RecepcionLeche guardar(RecepcionLeche recepcionLeche) {
        RecepcionLecheEntity entity = toEntity(recepcionLeche);
        RecepcionLecheEntity guardada = repository.save(entity);
        return RecepcionLecheMapper.toDomain(guardada);
    }

    @Override
    public Optional<RecepcionLeche> obtenerPorId(Long id) {
        return repository.findById(id)
                .map(RecepcionLecheMapper::toDomain);
    }

    @Override
    public List<RecepcionLeche> listarTodas() {
        return repository.findAll()
                .stream()
                .map(RecepcionLecheMapper::toDomain)
                .toList();
    }

    @Override
    public List<RecepcionLeche> listarPorFecha(LocalDate fechaRecepcion) {
        return repository.findByFechaRecepcionOrderByIdDesc(fechaRecepcion)
                .stream()
                .map(RecepcionLecheMapper::toDomain)
                .toList();
    }

    @Override
    public List<RecepcionLeche> listarPorProveedor(String proveedor) {
        return repository.findByProveedorContainingIgnoreCaseOrderByIdDesc(proveedor)
                .stream()
                .map(RecepcionLecheMapper::toDomain)
                .toList();
    }

    private RecepcionLecheEntity toEntity(RecepcionLeche recepcionLeche) {
        RecepcionLecheEntity entity = new RecepcionLecheEntity();

        entity.setId(recepcionLeche.getId());
        entity.setFechaRecepcion(recepcionLeche.getFechaRecepcion());
        entity.setTipoMateriaPrima(recepcionLeche.getTipoMateriaPrima());
        entity.setProveedor(recepcionLeche.getProveedor());
        entity.setCantidadRecibidaLitros(recepcionLeche.getCantidadRecibidaLitros());
        entity.setRecibidoPor(recepcionLeche.getRecibidoPor());
        entity.setNumeroRemision(recepcionLeche.getNumeroRemision());
        entity.setCantidadRemisionLitros(recepcionLeche.getCantidadRemisionLitros());
        entity.setObservaciones(recepcionLeche.getObservaciones());

        if (recepcionLeche.getIdTanque() != null) {
            entity.setTanque(entityManager.getReference(
                    TanqueLecheEntity.class,
                    recepcionLeche.getIdTanque()));
        } else {
            entity.setTanque(null);
        }

        entity.setUsuario(entityManager.getReference(
                UsuarioEntity.class,
                recepcionLeche.getIdUsuario()));

        if (recepcionLeche.getIdMovimientoLeche() != null) {
            entity.setMovimientoLeche(entityManager.getReference(
                    MovimientoLecheEntity.class,
                    recepcionLeche.getIdMovimientoLeche()));
        } else {
            entity.setMovimientoLeche(null);
        }

        if (recepcionLeche.getPesajes() != null) {
            List<RecepcionLechePesajeEntity> pesajes = recepcionLeche.getPesajes()
                    .stream()
                    .map(pesaje -> {
                        RecepcionLechePesajeEntity pesajeEntity = new RecepcionLechePesajeEntity();
                        pesajeEntity.setId(pesaje.getId());
                        pesajeEntity.setRecepcionLeche(entity);
                        pesajeEntity.setNumeroPesaje(pesaje.getNumeroPesaje());
                        pesajeEntity.setPesoBrutoKg(pesaje.getPesoBrutoKg());
                        pesajeEntity.setTaraKg(pesaje.getTaraKg());
                        pesajeEntity.setPesoNetoKg(pesaje.getPesoNetoKg());
                        pesajeEntity.setObservaciones(pesaje.getObservaciones());
                        return pesajeEntity;
                    })
                    .toList();

            entity.setPesajes(pesajes);
        }

        return entity;
    }
}
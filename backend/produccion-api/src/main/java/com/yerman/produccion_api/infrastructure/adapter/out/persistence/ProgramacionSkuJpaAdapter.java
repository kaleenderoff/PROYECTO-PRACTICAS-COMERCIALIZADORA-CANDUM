package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.application.mapper.ProgramacionSkuMapper;
import com.yerman.produccion_api.domain.model.ProgramacionSku;
import com.yerman.produccion_api.domain.port.out.ProgramacionSkuRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.CatalogoSkuEntity;
import com.yerman.produccion_api.infrastructure.entity.FormulaVersionEntity;
import com.yerman.produccion_api.infrastructure.entity.ProgramacionProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.ProgramacionSkuEntity;
import com.yerman.produccion_api.infrastructure.repository.CatalogoSkuJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ProgramacionProduccionJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ProgramacionSkuJpaRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Component
public class ProgramacionSkuJpaAdapter implements ProgramacionSkuRepositoryPort {

    private final ProgramacionSkuJpaRepository repository;
    private final ProgramacionProduccionJpaRepository programacionRepository;
    private final CatalogoSkuJpaRepository skuRepository;

    public ProgramacionSkuJpaAdapter(
            ProgramacionSkuJpaRepository repository,
            ProgramacionProduccionJpaRepository programacionRepository,
            CatalogoSkuJpaRepository skuRepository) {
        this.repository = repository;
        this.programacionRepository = programacionRepository;
        this.skuRepository = skuRepository;
    }

    @Override
    public ProgramacionSku guardar(ProgramacionSku programacionSku) {
        ProgramacionProduccionEntity programacion = programacionRepository.findById(programacionSku.getIdProgramacion())
                .orElseThrow(() -> new RecursoNoEncontradoException("Programación no encontrada"));

        CatalogoSkuEntity sku = skuRepository.findById(programacionSku.getIdSku())
                .orElseThrow(() -> new RecursoNoEncontradoException("SKU no encontrado"));

        validarSkuCompatibleConProgramacion(programacion, sku);

        ProgramacionSkuEntity entity = ProgramacionSkuMapper.toEntity(programacionSku, programacion, sku);
        ProgramacionSkuEntity saved = repository.save(entity);

        recalcularCabeceraProgramacion(programacion.getId());

        return ProgramacionSkuMapper.toDomain(saved);
    }

    @Override
    public ProgramacionSku actualizar(Long id, ProgramacionSku programacionSku) {
        ProgramacionSkuEntity actual = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Programación SKU no encontrada"));

        CatalogoSkuEntity sku = skuRepository.findById(programacionSku.getIdSku())
                .orElseThrow(() -> new RecursoNoEncontradoException("SKU no encontrado"));

        validarSkuCompatibleConProgramacion(actual.getProgramacion(), sku);

        actual.setSku(sku);
        actual.setUnidadesObjetivo(programacionSku.getUnidadesObjetivo());
        actual.setObservaciones(programacionSku.getObservaciones());

        ProgramacionSkuEntity updated = repository.save(actual);

        recalcularCabeceraProgramacion(actual.getProgramacion().getId());

        return ProgramacionSkuMapper.toDomain(updated);
    }

    @Override
    public void eliminar(Long id) {
        ProgramacionSkuEntity actual = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Programación SKU no encontrada"));

        Long idProgramacion = actual.getProgramacion().getId();

        repository.delete(actual);

        recalcularCabeceraProgramacion(idProgramacion);
    }

    @Override
    public Optional<ProgramacionSku> obtenerPorId(Long id) {
        return repository.findById(id).map(ProgramacionSkuMapper::toDomain);
    }

    @Override
    public List<ProgramacionSku> listarPorProgramacion(Long idProgramacion) {
        return repository.findByProgramacionIdOrderByIdAsc(idProgramacion)
                .stream()
                .map(ProgramacionSkuMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existePorProgramacionYSku(Long idProgramacion, Long idSku) {
        return repository.existsByProgramacionIdAndSkuId(idProgramacion, idSku);
    }

    private void validarSkuCompatibleConProgramacion(
            ProgramacionProduccionEntity programacion,
            CatalogoSkuEntity sku) {

        if (programacion == null || programacion.getProducto() == null) {
            return;
        }

        if (sku == null || sku.getProducto() == null) {
            return;
        }

        Long idProductoProgramacion = programacion.getProducto().getId();
        Long idProductoSku = sku.getProducto().getId();

        if (!idProductoProgramacion.equals(idProductoSku)) {
            throw new ReglaNegocioException(
                    "El SKU seleccionado no pertenece al producto de la programación");
        }
    }

    private void recalcularCabeceraProgramacion(Long idProgramacion) {
        ProgramacionProduccionEntity programacion = programacionRepository.findById(idProgramacion)
                .orElseThrow(() -> new RecursoNoEncontradoException("Programación no encontrada"));

        FormulaVersionEntity formulaVersion = programacion.getFormulaVersion();

        if (formulaVersion == null
                || formulaVersion.getRendimientoTeoricoPct() == null
                || formulaVersion.getKgBatchTotal() == null
                || formulaVersion.getRendimientoTeoricoPct().compareTo(BigDecimal.ZERO) <= 0
                || formulaVersion.getKgBatchTotal().compareTo(BigDecimal.ZERO) <= 0) {
            programacion.setKgBachePlan(BigDecimal.ZERO);
            programacion.setNumBachesPlan(0);
            programacionRepository.save(programacion);
            return;
        }

        List<ProgramacionSkuEntity> skus = repository.findByProgramacionIdOrderByIdAsc(idProgramacion);

        BigDecimal totalKgProductoTerminado = BigDecimal.ZERO;

        for (ProgramacionSkuEntity detalle : skus) {
            Integer unidades = detalle.getUnidadesObjetivo();

            Integer pesoUnidadGr = detalle.getSku() != null
                    ? detalle.getSku().getPesoNetoGr()
                    : null;

            if (unidades == null || pesoUnidadGr == null) {
                continue;
            }

            BigDecimal kgProductoTerminado = BigDecimal.valueOf(unidades)
                    .multiply(BigDecimal.valueOf(pesoUnidadGr))
                    .divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);

            totalKgProductoTerminado = totalKgProductoTerminado.add(kgProductoTerminado);
        }

        if (totalKgProductoTerminado.compareTo(BigDecimal.ZERO) <= 0) {
            programacion.setKgBachePlan(BigDecimal.ZERO);
            programacion.setNumBachesPlan(0);
            programacionRepository.save(programacion);
            return;
        }

        BigDecimal rendimientoDecimal = formulaVersion.getRendimientoTeoricoPct().compareTo(BigDecimal.ONE) > 0
                ? formulaVersion.getRendimientoTeoricoPct().divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP)
                : formulaVersion.getRendimientoTeoricoPct();

        BigDecimal totalKgBatch = totalKgProductoTerminado.divide(
                rendimientoDecimal,
                2,
                RoundingMode.HALF_UP);

        BigDecimal totalBatchesDecimal = totalKgBatch.divide(
                formulaVersion.getKgBatchTotal(),
                3,
                RoundingMode.HALF_UP);

        int totalBatchesEntero = totalBatchesDecimal
                .setScale(0, RoundingMode.CEILING)
                .intValue();

        programacion.setKgBachePlan(totalKgBatch);
        programacion.setNumBachesPlan(totalBatchesEntero);

        programacionRepository.save(programacion);
    }
}
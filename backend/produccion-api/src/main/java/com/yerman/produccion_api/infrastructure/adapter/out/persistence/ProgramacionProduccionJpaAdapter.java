package com.yerman.produccion_api.infrastructure.adapter.out.persistence;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.mapper.ProgramacionProduccionMapper;
import com.yerman.produccion_api.domain.model.ProgramacionProduccion;
import com.yerman.produccion_api.domain.port.out.ProgramacionProduccionRepositoryPort;
import com.yerman.produccion_api.infrastructure.entity.*;
import com.yerman.produccion_api.infrastructure.repository.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
public class ProgramacionProduccionJpaAdapter implements ProgramacionProduccionRepositoryPort {

    private final ProgramacionProduccionJpaRepository repository;
    private final CatalogoLineaJpaRepository lineaRepository;
    private final CatalogoProductoJpaRepository productoRepository;
    private final TurnoJpaRepository turnoRepository;
    private final FormulaVersionJpaRepository formulaVersionRepository;
    private final UsuarioJpaRepository usuarioRepository;

    public ProgramacionProduccionJpaAdapter(
            ProgramacionProduccionJpaRepository repository,
            CatalogoLineaJpaRepository lineaRepository,
            CatalogoProductoJpaRepository productoRepository,
            TurnoJpaRepository turnoRepository,
            FormulaVersionJpaRepository formulaVersionRepository,
            UsuarioJpaRepository usuarioRepository) {
        this.repository = repository;
        this.lineaRepository = lineaRepository;
        this.productoRepository = productoRepository;
        this.turnoRepository = turnoRepository;
        this.formulaVersionRepository = formulaVersionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public ProgramacionProduccion guardar(ProgramacionProduccion programacion) {
        CatalogoLineaEntity linea = lineaRepository.findById(programacion.getIdLinea())
                .orElseThrow(() -> new RecursoNoEncontradoException("Línea no encontrada"));
        CatalogoProductoEntity producto = productoRepository.findById(programacion.getIdProducto())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
        TurnoEntity turno = turnoRepository.findById(programacion.getIdTurno())
                .orElseThrow(() -> new RecursoNoEncontradoException("Turno no encontrado"));
        FormulaVersionEntity formulaVersion = formulaVersionRepository.findById(programacion.getIdFormulaVersion())
                .orElseThrow(() -> new RecursoNoEncontradoException("Versión de fórmula no encontrada"));
        UsuarioEntity jefeProduccion = usuarioRepository.findById(programacion.getIdJefeProduccion())
                .orElseThrow(() -> new RecursoNoEncontradoException("Jefe de producción no encontrado"));

        ProgramacionProduccionEntity entity = ProgramacionProduccionMapper.toEntity(
                programacion,
                linea,
                producto,
                turno,
                formulaVersion,
                jefeProduccion);

        ProgramacionProduccionEntity saved = repository.save(entity);
        return ProgramacionProduccionMapper.toDomain(saved);
    }

    @Override
    public Optional<ProgramacionProduccion> obtenerPorId(Long id) {
        return repository.findById(id).map(ProgramacionProduccionMapper::toDomain);
    }

    @Override
    public List<ProgramacionProduccion> listarPorFecha(LocalDate fecha) {
        return repository.findByFechaProduccionOrderByIdDesc(fecha)
                .stream()
                .map(ProgramacionProduccionMapper::toDomain)
                .toList();
    }

    @Override
    public List<ProgramacionProduccion> listarTodas() {
        return repository.findAll()
                .stream()
                .map(ProgramacionProduccionMapper::toDomain)
                .toList();
    }

    public boolean existeDuplicada(LocalDate fecha, Long idLinea, Long idTurno, Long idProducto) {
        return repository.existsByFechaProduccionAndLineaIdAndTurnoIdAndProductoId(
                fecha, idLinea, idTurno, idProducto);
    }
}
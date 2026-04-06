package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.Insumo;
import com.yerman.produccion_api.domain.port.in.GestionInsumoUseCase;
import com.yerman.produccion_api.domain.port.out.InsumoRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GestionInsumoService implements GestionInsumoUseCase {

    private final InsumoRepositoryPort insumoRepositoryPort;

    public GestionInsumoService(InsumoRepositoryPort insumoRepositoryPort) {
        this.insumoRepositoryPort = insumoRepositoryPort;
    }

    @Override
    public Insumo crearInsumo(Insumo insumo) {
        validarInsumoObligatorio(insumo);
        validarCamposObligatorios(insumo);

        String nombreNormalizado = normalizarTextoObligatorio(insumo.getNombre());
        String descripcionNormalizada = normalizarTextoOpcional(insumo.getDescripcion());
        String unidadMedidaNormalizada = normalizarUnidadMedida(insumo.getUnidadMedida());

        validarDuplicadoInsumo(nombreNormalizado);

        insumo.setNombre(nombreNormalizado);
        insumo.setDescripcion(descripcionNormalizada);
        insumo.setUnidadMedida(unidadMedidaNormalizada);

        if (insumo.getActivo() == null) {
            insumo.setActivo(true);
        }

        insumo.setCreatedAt(LocalDateTime.now());
        insumo.setUpdatedAt(LocalDateTime.now());

        return insumoRepositoryPort.guardar(insumo);
    }

    @Override
    public Optional<Insumo> obtenerPorId(Long id) {
        if (id == null) {
            throw new ReglaNegocioException("El id del insumo es obligatorio");
        }
        return insumoRepositoryPort.buscarPorId(id);
    }

    @Override
    public Optional<Insumo> obtenerPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ReglaNegocioException("El nombre del insumo es obligatorio");
        }
        return insumoRepositoryPort.buscarPorNombre(normalizarTextoObligatorio(nombre));
    }

    @Override
    public List<Insumo> listarTodos() {
        return insumoRepositoryPort.listarTodos();
    }

    @Override
    public List<Insumo> listarActivos() {
        return insumoRepositoryPort.listarActivos();
    }

    public Insumo obtenerPorIdObligatorio(Long id) {
        if (id == null) {
            throw new ReglaNegocioException("El id del insumo es obligatorio");
        }

        return insumoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Insumo no encontrado con id: " + id));
    }

    private void validarInsumoObligatorio(Insumo insumo) {
        if (insumo == null) {
            throw new ReglaNegocioException("El insumo es obligatorio");
        }
    }

    private void validarCamposObligatorios(Insumo insumo) {
        if (insumo.getNombre() == null || insumo.getNombre().trim().isEmpty()) {
            throw new ReglaNegocioException("El nombre del insumo es obligatorio");
        }

        if (insumo.getUnidadMedida() == null || insumo.getUnidadMedida().trim().isEmpty()) {
            throw new ReglaNegocioException("La unidad de medida del insumo es obligatoria");
        }
    }

    private void validarDuplicadoInsumo(String nombre) {
        if (insumoRepositoryPort.existePorNombre(nombre)) {
            throw new ReglaNegocioException("Ya existe un insumo con el nombre: " + nombre);
        }
    }

    private String normalizarTextoObligatorio(String valor) {
        return valor.trim();
    }

    private String normalizarTextoOpcional(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }

    private String normalizarUnidadMedida(String unidadMedida) {
        return unidadMedida.trim().toUpperCase();
    }
}

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
        validarDatosObligatorios(insumo);

        String nombreLimpio = limpiar(insumo.getNombre());
        String descripcionLimpia = limpiarOpcional(insumo.getDescripcion());
        String unidadMedidaLimpia = limpiar(insumo.getUnidadMedida());

        if (insumoRepositoryPort.existePorNombre(nombreLimpio)) {
            throw new ReglaNegocioException(
                    "Ya existe un insumo con el nombre: " + nombreLimpio);
        }

        insumo.setNombre(nombreLimpio);
        insumo.setDescripcion(descripcionLimpia);
        insumo.setUnidadMedida(unidadMedidaLimpia);

        if (insumo.getActivo() == null) {
            insumo.setActivo(true);
        }

        insumo.setCreatedAt(LocalDateTime.now());
        insumo.setUpdatedAt(LocalDateTime.now());

        return insumoRepositoryPort.guardar(insumo);
    }

    @Override
    public Optional<Insumo> obtenerPorId(Long id) {
        return insumoRepositoryPort.buscarPorId(id);
    }

    @Override
    public Optional<Insumo> obtenerPorNombre(String nombre) {
        return insumoRepositoryPort.buscarPorNombre(limpiar(nombre));
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
        return insumoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Insumo no encontrado con id: " + id));
    }

    private void validarDatosObligatorios(Insumo insumo) {
        if (insumo == null) {
            throw new ReglaNegocioException("El insumo es obligatorio");
        }

        if (insumo.getNombre() == null || insumo.getNombre().trim().isEmpty()) {
            throw new ReglaNegocioException("El nombre del insumo es obligatorio");
        }

        if (insumo.getUnidadMedida() == null || insumo.getUnidadMedida().trim().isEmpty()) {
            throw new ReglaNegocioException("La unidad de medida del insumo es obligatoria");
        }
    }

    private String limpiar(String valor) {
        return valor == null ? null : valor.trim();
    }

    private String limpiarOpcional(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }
}

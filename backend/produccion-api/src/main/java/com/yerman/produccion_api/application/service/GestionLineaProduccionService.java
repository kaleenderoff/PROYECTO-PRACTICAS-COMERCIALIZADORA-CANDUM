package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.LineaProduccion;
import com.yerman.produccion_api.domain.port.in.GestionLineaProduccionUseCase;
import com.yerman.produccion_api.domain.port.out.LineaProduccionRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GestionLineaProduccionService implements GestionLineaProduccionUseCase {

    private final LineaProduccionRepositoryPort lineaProduccionRepositoryPort;

    public GestionLineaProduccionService(LineaProduccionRepositoryPort lineaProduccionRepositoryPort) {
        this.lineaProduccionRepositoryPort = lineaProduccionRepositoryPort;
    }

    @Override
    public LineaProduccion crearLineaProduccion(LineaProduccion lineaProduccion) {
        validarNombreObligatorio(lineaProduccion.getNombre());

        String nombreLimpio = limpiar(lineaProduccion.getNombre());

        if (lineaProduccionRepositoryPort.existePorNombre(nombreLimpio)) {
            throw new ReglaNegocioException(
                    "Ya existe una línea de producción con el nombre: " + nombreLimpio);
        }

        lineaProduccion.setNombre(nombreLimpio);
        lineaProduccion.setDescripcion(limpiarOpcional(lineaProduccion.getDescripcion()));

        if (lineaProduccion.getActivo() == null) {
            lineaProduccion.setActivo(true);
        }

        lineaProduccion.setCreatedAt(LocalDateTime.now());
        lineaProduccion.setUpdatedAt(LocalDateTime.now());

        return lineaProduccionRepositoryPort.guardar(lineaProduccion);
    }

    @Override
    public Optional<LineaProduccion> obtenerPorId(Long id) {
        return lineaProduccionRepositoryPort.buscarPorId(id);
    }

    @Override
    public Optional<LineaProduccion> obtenerPorNombre(String nombre) {
        return lineaProduccionRepositoryPort.buscarPorNombre(limpiar(nombre));
    }

    @Override
    public List<LineaProduccion> listarTodas() {
        return lineaProduccionRepositoryPort.listarTodas();
    }

    @Override
    public List<LineaProduccion> listarActivas() {
        return lineaProduccionRepositoryPort.listarActivas();
    }

    public LineaProduccion obtenerPorIdObligatorio(Long id) {
        return lineaProduccionRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Línea de producción no encontrada con id: " + id));
    }

    private void validarNombreObligatorio(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ReglaNegocioException("El nombre de la línea de producción es obligatorio");
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

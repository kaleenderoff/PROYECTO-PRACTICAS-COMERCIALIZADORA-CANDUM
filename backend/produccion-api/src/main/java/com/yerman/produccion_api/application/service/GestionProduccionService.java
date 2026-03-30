package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.domain.model.LineaProduccion;
import com.yerman.produccion_api.domain.model.Produccion;
import com.yerman.produccion_api.domain.model.Usuario;
import com.yerman.produccion_api.domain.port.in.GestionProduccionUseCase;
import com.yerman.produccion_api.domain.port.out.LineaProduccionRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ProduccionRepositoryPort;
import com.yerman.produccion_api.domain.port.out.UsuarioRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GestionProduccionService implements GestionProduccionUseCase {

    private final ProduccionRepositoryPort produccionRepositoryPort;
    private final LineaProduccionRepositoryPort lineaProduccionRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public GestionProduccionService(ProduccionRepositoryPort produccionRepositoryPort,
            LineaProduccionRepositoryPort lineaProduccionRepositoryPort,
            UsuarioRepositoryPort usuarioRepositoryPort) {
        this.produccionRepositoryPort = produccionRepositoryPort;
        this.lineaProduccionRepositoryPort = lineaProduccionRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public Produccion crearProduccion(Produccion produccion) {
        validarDatosObligatorios(produccion);

        String tipoTurnoLimpio = limpiar(produccion.getTipoTurno());
        String numeroLoteLimpio = limpiar(produccion.getNumeroLote());
        String estadoLimpio = limpiar(produccion.getEstado());
        String observacionesLimpias = limpiarOpcional(produccion.getObservacionesGenerales());

        if (produccionRepositoryPort.existePorNumeroLote(numeroLoteLimpio)) {
            throw new IllegalArgumentException(
                    "Ya existe una producción con el número de lote: " + numeroLoteLimpio);
        }

        Long idLinea = produccion.getLineaProduccion().getIdLineaProduccion();

        LineaProduccion linea = lineaProduccionRepositoryPort.buscarPorId(idLinea)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Línea de producción no encontrada con id: " + idLinea));

        Usuario operario = usuarioRepositoryPort.buscarPorId(produccion.getIdOperario())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Operario no encontrado con id: " + produccion.getIdOperario()));

        Usuario jefeLinea = usuarioRepositoryPort.buscarPorId(produccion.getIdJefeLinea())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Jefe de línea no encontrado con id: " + produccion.getIdJefeLinea()));

        validarRoles(operario, jefeLinea);

        produccion.setTipoTurno(tipoTurnoLimpio);
        produccion.setNumeroLote(numeroLoteLimpio);
        produccion.setEstado(estadoLimpio);
        produccion.setObservacionesGenerales(observacionesLimpias);
        produccion.setLineaProduccion(linea);
        produccion.setCreatedAt(LocalDateTime.now());
        produccion.setUpdatedAt(LocalDateTime.now());

        return produccionRepositoryPort.guardar(produccion);
    }

    @Override
    public Optional<Produccion> obtenerPorId(Long id) {
        return produccionRepositoryPort.buscarPorId(id);
    }

    @Override
    public Optional<Produccion> obtenerPorNumeroLote(String numeroLote) {
        return produccionRepositoryPort.buscarPorNumeroLote(limpiar(numeroLote));
    }

    @Override
    public List<Produccion> listarTodas() {
        return produccionRepositoryPort.listarTodas();
    }

    @Override
    public List<Produccion> listarPorFecha(LocalDate fechaProduccion) {
        if (fechaProduccion == null) {
            throw new IllegalArgumentException("La fecha de producción es obligatoria");
        }
        return produccionRepositoryPort.listarPorFecha(fechaProduccion);
    }

    @Override
    public List<Produccion> listarPorEstado(String estado) {
        String estadoLimpio = limpiar(estado);
        if (estadoLimpio == null || estadoLimpio.isEmpty()) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        return produccionRepositoryPort.listarPorEstado(estadoLimpio);
    }

    @Override
    public List<Produccion> listarPorLineaProduccion(Long idLineaProduccion) {
        if (idLineaProduccion == null) {
            throw new IllegalArgumentException("El id de la línea de producción es obligatorio");
        }
        return produccionRepositoryPort.listarPorLineaProduccion(idLineaProduccion);
    }

    @Override
    public List<Produccion> listarPorOperario(Long idOperario) {
        if (idOperario == null) {
            throw new IllegalArgumentException("El id del operario es obligatorio");
        }
        return produccionRepositoryPort.listarPorOperario(idOperario);
    }

    @Override
    public List<Produccion> listarPorJefeLinea(Long idJefeLinea) {
        if (idJefeLinea == null) {
            throw new IllegalArgumentException("El id del jefe de línea es obligatorio");
        }
        return produccionRepositoryPort.listarPorJefeLinea(idJefeLinea);
    }

    public Produccion obtenerPorIdObligatorio(Long id) {
        return produccionRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producción no encontrada con id: " + id));
    }

    private void validarDatosObligatorios(Produccion produccion) {
        if (produccion == null) {
            throw new IllegalArgumentException("La producción es obligatoria");
        }

        if (produccion.getFechaProduccion() == null) {
            throw new IllegalArgumentException("La fecha de producción es obligatoria");
        }

        if (produccion.getTipoTurno() == null || produccion.getTipoTurno().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de turno es obligatorio");
        }

        if (produccion.getNumeroLote() == null || produccion.getNumeroLote().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de lote es obligatorio");
        }

        if (produccion.getEstado() == null || produccion.getEstado().trim().isEmpty()) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }

        if (produccion.getLineaProduccion() == null || produccion.getLineaProduccion().getIdLineaProduccion() == null) {
            throw new IllegalArgumentException("La línea de producción es obligatoria");
        }

        if (produccion.getIdOperario() == null) {
            throw new IllegalArgumentException("El operario es obligatorio");
        }

        if (produccion.getIdJefeLinea() == null) {
            throw new IllegalArgumentException("El jefe de línea es obligatorio");
        }
    }

    private void validarRoles(Usuario operario, Usuario jefeLinea) {
        if (operario.getRol() != Usuario.Rol.OPERARIO) {
            throw new IllegalArgumentException("El usuario indicado como operario no tiene rol OPERARIO");
        }

        if (jefeLinea.getRol() != Usuario.Rol.JEFE_LINEA) {
            throw new IllegalArgumentException("El usuario indicado como jefe de línea no tiene rol JEFE_LINEA");
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
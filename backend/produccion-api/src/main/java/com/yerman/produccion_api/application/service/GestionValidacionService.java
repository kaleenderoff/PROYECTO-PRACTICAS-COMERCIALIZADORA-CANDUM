package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.domain.model.DetalleProduccion;
import com.yerman.produccion_api.domain.model.Usuario;
import com.yerman.produccion_api.domain.model.Validacion;
import com.yerman.produccion_api.domain.port.in.GestionValidacionUseCase;
import com.yerman.produccion_api.domain.port.out.DetalleProduccionRepositoryPort;
import com.yerman.produccion_api.domain.port.out.UsuarioRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ValidacionRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GestionValidacionService implements GestionValidacionUseCase {

    private final ValidacionRepositoryPort validacionRepositoryPort;
    private final DetalleProduccionRepositoryPort detalleProduccionRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public GestionValidacionService(ValidacionRepositoryPort validacionRepositoryPort,
            DetalleProduccionRepositoryPort detalleProduccionRepositoryPort,
            UsuarioRepositoryPort usuarioRepositoryPort) {
        this.validacionRepositoryPort = validacionRepositoryPort;
        this.detalleProduccionRepositoryPort = detalleProduccionRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public Validacion crearValidacion(Validacion validacion) {
        validarDatosObligatorios(validacion);

        DetalleProduccion detalle = detalleProduccionRepositoryPort.buscarPorId(validacion.getIdDetalleProduccion())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Detalle de producción no encontrado con id: " + validacion.getIdDetalleProduccion()));

        Usuario validador = usuarioRepositoryPort.buscarPorId(validacion.getIdValidador())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario validador no encontrado con id: " + validacion.getIdValidador()));

        validarRolValidador(validador);

        if (validacionRepositoryPort.existePorDetalleProduccion(validacion.getIdDetalleProduccion())) {
            throw new IllegalArgumentException(
                    "Ya existe una validación para el detalle de producción indicado");
        }

        validacion.setEstado(limpiar(validacion.getEstado()));
        validacion.setObservacion(limpiarOpcional(validacion.getObservacion()));
        validacion.setFechaValidacion(LocalDateTime.now());
        validacion.setCreatedAt(LocalDateTime.now());
        validacion.setUpdatedAt(LocalDateTime.now());

        return validacionRepositoryPort.guardar(validacion);
    }

    @Override
    public Optional<Validacion> obtenerPorId(Long id) {
        return validacionRepositoryPort.buscarPorId(id);
    }

    @Override
    public Optional<Validacion> obtenerPorDetalleProduccion(Long idDetalleProduccion) {
        if (idDetalleProduccion == null) {
            throw new IllegalArgumentException("El id del detalle de producción es obligatorio");
        }
        return validacionRepositoryPort.buscarPorDetalleProduccion(idDetalleProduccion);
    }

    @Override
    public List<Validacion> listarTodas() {
        return validacionRepositoryPort.listarTodas();
    }

    @Override
    public List<Validacion> listarPorEstado(String estado) {
        String estadoLimpio = limpiar(estado);
        if (estadoLimpio == null || estadoLimpio.isEmpty()) {
            throw new IllegalArgumentException("El estado de la validación es obligatorio");
        }
        return validacionRepositoryPort.listarPorEstado(estadoLimpio);
    }

    @Override
    public List<Validacion> listarPorValidador(Long idValidador) {
        if (idValidador == null) {
            throw new IllegalArgumentException("El id del validador es obligatorio");
        }
        return validacionRepositoryPort.listarPorValidador(idValidador);
    }

    public Validacion obtenerPorIdObligatorio(Long id) {
        return validacionRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Validación no encontrada con id: " + id));
    }

    private void validarDatosObligatorios(Validacion validacion) {
        if (validacion == null) {
            throw new IllegalArgumentException("La validación es obligatoria");
        }

        if (validacion.getIdDetalleProduccion() == null) {
            throw new IllegalArgumentException("El detalle de producción es obligatorio");
        }

        if (validacion.getIdValidador() == null) {
            throw new IllegalArgumentException("El validador es obligatorio");
        }

        if (validacion.getEstado() == null || validacion.getEstado().trim().isEmpty()) {
            throw new IllegalArgumentException("El estado de la validación es obligatorio");
        }
    }

    private void validarRolValidador(Usuario validador) {
        Usuario.Rol rol = validador.getRol();

        if (rol != Usuario.Rol.JEFE_LINEA
                && rol != Usuario.Rol.INGENIERO
                && rol != Usuario.Rol.JEFE_PLANTA
                && rol != Usuario.Rol.ADMIN) {
            throw new IllegalArgumentException(
                    "El usuario indicado no tiene permisos para validar registros de producción");
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

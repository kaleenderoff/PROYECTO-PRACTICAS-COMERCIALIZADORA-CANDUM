package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoDuplicadoException;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.EstadoValidacion;
import com.yerman.produccion_api.domain.model.Usuario;
import com.yerman.produccion_api.domain.model.Validacion;
import com.yerman.produccion_api.domain.port.in.GestionAuditoriaUseCase;
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
    private final GestionAuditoriaUseCase gestionAuditoriaUseCase;

    public GestionValidacionService(
            ValidacionRepositoryPort validacionRepositoryPort,
            DetalleProduccionRepositoryPort detalleProduccionRepositoryPort,
            UsuarioRepositoryPort usuarioRepositoryPort,
            GestionAuditoriaUseCase gestionAuditoriaUseCase) {
        this.validacionRepositoryPort = validacionRepositoryPort;
        this.detalleProduccionRepositoryPort = detalleProduccionRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
        this.gestionAuditoriaUseCase = gestionAuditoriaUseCase;
    }

    @Override
    public Validacion crearValidacion(Validacion validacion) {
        validarDatosObligatorios(validacion);

        detalleProduccionRepositoryPort.buscarPorId(validacion.getIdDetalleProduccion())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Detalle de producción no encontrado con id: " + validacion.getIdDetalleProduccion()));

        Usuario validador = usuarioRepositoryPort.buscarPorId(validacion.getIdValidador())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario validador no encontrado con id: " + validacion.getIdValidador()));

        validarUsuarioActivo(validador);
        validarRolValidador(validador);

        if (validacionRepositoryPort.existePorDetalleProduccion(validacion.getIdDetalleProduccion())) {
            throw new RecursoDuplicadoException(
                    "Ya existe una validación para el detalle de producción indicado");
        }

        validacion.setObservacion(limpiarOpcional(validacion.getObservacion()));
        validacion.setFechaValidacion(LocalDateTime.now());

        Validacion validacionGuardada = validacionRepositoryPort.guardar(validacion);

        gestionAuditoriaUseCase.registrar(
                validacionGuardada.getIdValidador(),
                "VALIDATE",
                "VALIDACION",
                validacionGuardada.getIdValidacion(),
                "Se registró una validación para el detalle de producción con id "
                        + validacionGuardada.getIdDetalleProduccion()
                        + " con estado "
                        + validacionGuardada.getEstado().name());

        return validacionGuardada;
    }

    @Override
    public Optional<Validacion> obtenerPorId(Long id) {
        if (id == null) {
            throw new ReglaNegocioException("El id de la validación es obligatorio");
        }
        return validacionRepositoryPort.buscarPorId(id);
    }

    @Override
    public Optional<Validacion> obtenerPorDetalleProduccion(Long idDetalleProduccion) {
        if (idDetalleProduccion == null) {
            throw new ReglaNegocioException("El id del detalle de producción es obligatorio");
        }
        return validacionRepositoryPort.buscarPorDetalleProduccion(idDetalleProduccion);
    }

    @Override
    public List<Validacion> listarTodas() {
        return validacionRepositoryPort.listarTodas();
    }

    @Override
    public List<Validacion> listarPorEstado(EstadoValidacion estado) {
        if (estado == null) {
            throw new ReglaNegocioException("El estado de la validación es obligatorio");
        }
        return validacionRepositoryPort.listarPorEstado(estado);
    }

    @Override
    public List<Validacion> listarPorValidador(Long idValidador) {
        if (idValidador == null) {
            throw new ReglaNegocioException("El id del validador es obligatorio");
        }
        return validacionRepositoryPort.listarPorValidador(idValidador);
    }

    public Validacion obtenerPorIdObligatorio(Long id) {
        if (id == null) {
            throw new ReglaNegocioException("El id de la validación es obligatorio");
        }

        return validacionRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Validación no encontrada con id: " + id));
    }

    private void validarDatosObligatorios(Validacion validacion) {
        if (validacion == null) {
            throw new ReglaNegocioException("La validación es obligatoria");
        }

        if (validacion.getIdDetalleProduccion() == null) {
            throw new ReglaNegocioException("El detalle de producción es obligatorio");
        }

        if (validacion.getIdValidador() == null) {
            throw new ReglaNegocioException("El validador es obligatorio");
        }

        if (validacion.getEstado() == null) {
            throw new ReglaNegocioException("El estado de la validación es obligatorio");
        }
    }

    private void validarUsuarioActivo(Usuario validador) {
        if (!validador.isActivo()) {
            throw new ReglaNegocioException("El usuario validador está inactivo");
        }
    }

    private void validarRolValidador(Usuario validador) {
        Usuario.Rol rol = validador.getRol();

        if (rol != Usuario.Rol.JEFE_LINEA
                && rol != Usuario.Rol.INGENIERO
                && rol != Usuario.Rol.JEFE_PLANTA
                && rol != Usuario.Rol.ADMIN) {
            throw new ReglaNegocioException(
                    "El usuario indicado no tiene permisos para validar registros de producción");
        }
    }

    private String limpiarOpcional(String valor) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }
}
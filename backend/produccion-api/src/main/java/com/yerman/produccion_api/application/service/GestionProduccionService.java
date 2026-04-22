package com.yerman.produccion_api.application.service;

import com.yerman.produccion_api.application.exception.RecursoDuplicadoException;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.LineaProduccion;
import com.yerman.produccion_api.domain.model.Produccion;
import com.yerman.produccion_api.domain.model.ProduccionFiltro;
import com.yerman.produccion_api.domain.model.Usuario;
import com.yerman.produccion_api.domain.port.in.GestionProduccionUseCase;
import com.yerman.produccion_api.domain.port.out.LineaProduccionRepositoryPort;
import com.yerman.produccion_api.domain.port.out.ProduccionRepositoryPort;
import com.yerman.produccion_api.domain.port.out.UsuarioRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public GestionProduccionService(
            ProduccionRepositoryPort produccionRepositoryPort,
            LineaProduccionRepositoryPort lineaProduccionRepositoryPort,
            UsuarioRepositoryPort usuarioRepositoryPort) {
        this.produccionRepositoryPort = produccionRepositoryPort;
        this.lineaProduccionRepositoryPort = lineaProduccionRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    @Override
    public Produccion crearProduccion(Produccion produccion) {
        validarProduccionObligatoria(produccion);
        validarCamposObligatorios(produccion);

        String tipoTurnoNormalizado = normalizarTextoObligatorio(produccion.getTipoTurno()).toUpperCase();
        String numeroLoteNormalizado = normalizarTextoObligatorio(produccion.getNumeroLote());
        String estadoNormalizado = normalizarTextoObligatorio(produccion.getEstado()).toUpperCase();

        validarDuplicadoLote(numeroLoteNormalizado);

        Long idLineaProduccion = produccion.getLineaProduccion().getIdLineaProduccion();
        Long idOperario = produccion.getIdOperario();
        Long idJefeLinea = produccion.getIdJefeLinea();

        LineaProduccion lineaProduccion = lineaProduccionRepositoryPort.buscarPorId(idLineaProduccion)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Línea de producción no encontrada con id: " + idLineaProduccion));

        Usuario operario = usuarioRepositoryPort.buscarPorId(idOperario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Operario no encontrado con id: " + idOperario));

        Usuario jefeLinea = usuarioRepositoryPort.buscarPorId(idJefeLinea)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Jefe de línea no encontrado con id: " + idJefeLinea));

        validarLineaProduccionActiva(lineaProduccion);
        validarUsuarioActivo(operario, "jefe de linea ejecutor");
        validarUsuarioActivo(jefeLinea, "jefe de línea");
        validarRolJefeLineaEjecutor(operario);
        validarRolJefeLinea(jefeLinea);

        produccion.setTipoTurno(tipoTurnoNormalizado);
        produccion.setNumeroLote(numeroLoteNormalizado);
        produccion.setEstado(estadoNormalizado);
        produccion.setCreatedAt(LocalDateTime.now());
        produccion.setUpdatedAt(LocalDateTime.now());

        return produccionRepositoryPort.guardar(produccion);
    }

    @Override
    public Optional<Produccion> obtenerPorId(Long id) {
        if (id == null) {
            throw new ReglaNegocioException("El id de la producción es obligatorio");
        }
        return produccionRepositoryPort.buscarPorId(id);
    }

    @Override
    public Optional<Produccion> obtenerPorNumeroLote(String numeroLote) {
        if (numeroLote == null || numeroLote.trim().isEmpty()) {
            throw new ReglaNegocioException("El número de lote es obligatorio");
        }
        return produccionRepositoryPort.buscarPorNumeroLote(normalizarTextoObligatorio(numeroLote));
    }

    @Override
    public List<Produccion> listarTodas() {
        return produccionRepositoryPort.listarTodas();
    }

    @Override
    public List<Produccion> listarPorFecha(LocalDate fechaProduccion) {
        if (fechaProduccion == null) {
            throw new ReglaNegocioException("La fecha de producción es obligatoria");
        }
        return produccionRepositoryPort.listarPorFecha(fechaProduccion);
    }

    @Override
    public List<Produccion> listarPorEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new ReglaNegocioException("El estado es obligatorio");
        }
        return produccionRepositoryPort.listarPorEstado(normalizarTextoObligatorio(estado).toUpperCase());
    }

    @Override
    public List<Produccion> listarPorLineaProduccion(Long idLineaProduccion) {
        if (idLineaProduccion == null) {
            throw new ReglaNegocioException("El id de la línea de producción es obligatorio");
        }
        return produccionRepositoryPort.listarPorLineaProduccion(idLineaProduccion);
    }

    @Override
    public List<Produccion> listarPorOperario(Long idOperario) {
        if (idOperario == null) {
            throw new ReglaNegocioException("El id del operario es obligatorio");
        }
        return produccionRepositoryPort.listarPorOperario(idOperario);
    }

    @Override
    public List<Produccion> listarPorJefeLinea(Long idJefeLinea) {
        if (idJefeLinea == null) {
            throw new ReglaNegocioException("El id del jefe de línea es obligatorio");
        }
        return produccionRepositoryPort.listarPorJefeLinea(idJefeLinea);
    }

    @Override
    public Page<Produccion> listarPaginado(Pageable pageable) {
        return produccionRepositoryPort.listarPaginado(pageable);
    }

    @Override
    public Page<Produccion> filtrar(ProduccionFiltro filtro, Pageable pageable) {
        if (filtro != null
                && filtro.getFechaDesde() != null
                && filtro.getFechaHasta() != null
                && filtro.getFechaDesde().isAfter(filtro.getFechaHasta())) {
            throw new ReglaNegocioException("La fecha desde no puede ser mayor que la fecha hasta");
        }

        if (filtro == null) {
            filtro = new ProduccionFiltro();
        }

        filtro.setNumeroLote(normalizarTextoOpcional(filtro.getNumeroLote()));

        String estado = normalizarTextoOpcional(filtro.getEstado());
        filtro.setEstado(estado != null ? estado.toUpperCase() : null);

        return produccionRepositoryPort.filtrar(filtro, pageable);
    }

    public Produccion obtenerPorIdObligatorio(Long id) {
        if (id == null) {
            throw new ReglaNegocioException("El id de la producción es obligatorio");
        }

        return produccionRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producción no encontrada con id: " + id));
    }

    private void validarProduccionObligatoria(Produccion produccion) {
        if (produccion == null) {
            throw new ReglaNegocioException("La producción es obligatoria");
        }
    }

    private void validarCamposObligatorios(Produccion produccion) {
        if (produccion.getFechaProduccion() == null) {
            throw new ReglaNegocioException("La fecha de producción es obligatoria");
        }

        if (produccion.getTipoTurno() == null || produccion.getTipoTurno().trim().isEmpty()) {
            throw new ReglaNegocioException("El tipo de turno es obligatorio");
        }

        if (produccion.getNumeroLote() == null || produccion.getNumeroLote().trim().isEmpty()) {
            throw new ReglaNegocioException("El número de lote es obligatorio");
        }

        if (produccion.getEstado() == null || produccion.getEstado().trim().isEmpty()) {
            throw new ReglaNegocioException("El estado es obligatorio");
        }

        if (produccion.getLineaProduccion() == null
                || produccion.getLineaProduccion().getIdLineaProduccion() == null) {
            throw new ReglaNegocioException("La línea de producción es obligatoria");
        }

        if (produccion.getIdOperario() == null) {
            throw new ReglaNegocioException("El operario es obligatorio");
        }

        if (produccion.getIdJefeLinea() == null) {
            throw new ReglaNegocioException("El jefe de línea es obligatorio");
        }
    }

    private void validarDuplicadoLote(String numeroLote) {
        if (produccionRepositoryPort.buscarPorNumeroLote(numeroLote).isPresent()) {
            throw new RecursoDuplicadoException(
                    "Ya existe una producción con el número de lote: " + numeroLote);
        }
    }

    private void validarLineaProduccionActiva(LineaProduccion lineaProduccion) {
        if (Boolean.FALSE.equals(lineaProduccion.getActivo())) {
            throw new ReglaNegocioException("No se puede crear producción con una línea de producción inactiva");
        }
    }

    private void validarUsuarioActivo(Usuario usuario, String etiqueta) {
        if (!usuario.isActivo()) {
            throw new ReglaNegocioException("No se puede usar un usuario inactivo como " + etiqueta);
        }
    }

    private void validarRolJefeLineaEjecutor(Usuario usuario) {
        if (usuario.getRol() != Usuario.Rol.JEFE_LINEA) {
            throw new ReglaNegocioException("El usuario ejecutor debe tener rol JEFE_LINEA");
        }
    }

    private void validarRolJefeLinea(Usuario jefeLinea) {
        if (jefeLinea.getRol() != Usuario.Rol.JEFE_LINEA) {
            throw new ReglaNegocioException("El usuario indicado como jefe de línea no tiene rol JEFE_LINEA");
        }
    }

    private String normalizarTextoObligatorio(String valor) {
        return valor.trim();
    }

    private String normalizarTextoOpcional(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        return valor.trim();
    }
}

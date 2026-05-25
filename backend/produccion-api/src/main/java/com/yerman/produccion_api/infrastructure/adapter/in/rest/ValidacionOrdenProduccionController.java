package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.ValidacionOrdenProduccionRequest;
import com.yerman.produccion_api.application.dto.response.ValidacionOrdenProduccionResponse;
import com.yerman.produccion_api.application.exception.RecursoDuplicadoException;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.domain.model.EstadoOrdenProduccion;
import com.yerman.produccion_api.infrastructure.entity.OrdenProduccionEntity;
import com.yerman.produccion_api.infrastructure.entity.ValidacionOrdenProduccionEntity;
import com.yerman.produccion_api.infrastructure.repository.OrdenProduccionJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.UsuarioJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.ValidacionOrdenProduccionJpaRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/validaciones-orden-produccion")
public class ValidacionOrdenProduccionController {

    private final ValidacionOrdenProduccionJpaRepository validacionRepository;
    private final OrdenProduccionJpaRepository ordenRepository;
    private final UsuarioJpaRepository usuarioRepository;

    public ValidacionOrdenProduccionController(
            ValidacionOrdenProduccionJpaRepository validacionRepository,
            OrdenProduccionJpaRepository ordenRepository,
            UsuarioJpaRepository usuarioRepository) {
        this.validacionRepository = validacionRepository;
        this.ordenRepository = ordenRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<ValidacionOrdenProduccionResponse> listar() {
        return validacionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/orden/{idOrden}")
    public ValidacionOrdenProduccionResponse obtenerPorOrden(@PathVariable Long idOrden) {
        return validacionRepository.findByOrdenId(idOrden)
                .map(this::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe validacion para la orden con ID: " + idOrden));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ValidacionOrdenProduccionResponse validar(
            @Valid @RequestBody ValidacionOrdenProduccionRequest request) {

        if (validacionRepository.existsByOrdenId(request.idOrden())) {
            throw new RecursoDuplicadoException(
                    "La orden ya tiene una validacion registrada.");
        }

        OrdenProduccionEntity orden = ordenRepository.findById(request.idOrden())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la orden con ID: " + request.idOrden()));

        if (orden.getEstado() != EstadoOrdenProduccion.FINALIZADA) {
            throw new ReglaNegocioException(
                    "Solo se pueden validar ordenes finalizadas.");
        }

        var jefeProduccion = usuarioRepository.findByIdUsuario(request.idJefeProduccion())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el jefe de produccion con ID: " + request.idJefeProduccion()));

        ValidacionOrdenProduccionEntity entity = new ValidacionOrdenProduccionEntity();
        entity.setOrden(orden);
        entity.setAprobado(request.aprobado());
        entity.setJefeProduccion(jefeProduccion);
        entity.setObservacion(request.observacion());
        entity.setFechaValidacion(LocalDateTime.now());
        entity.setRequiereRevision(Boolean.TRUE.equals(request.requiereRevision())
                || Boolean.FALSE.equals(request.aprobado()));

        return toResponse(validacionRepository.save(entity));
    }

    private ValidacionOrdenProduccionResponse toResponse(ValidacionOrdenProduccionEntity entity) {
        return new ValidacionOrdenProduccionResponse(
                entity.getId(),
                entity.getOrden().getId(),
                entity.getOrden().getNumeroOrden(),
                entity.getAprobado(),
                entity.getJefeProduccion().getIdUsuario(),
                entity.getObservacion(),
                entity.getFechaValidacion(),
                entity.getRequiereRevision());
    }
}

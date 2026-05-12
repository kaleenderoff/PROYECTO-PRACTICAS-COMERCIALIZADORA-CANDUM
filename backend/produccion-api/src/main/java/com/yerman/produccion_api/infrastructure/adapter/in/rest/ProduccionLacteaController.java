package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.ProduccionLacteaRequest;
import com.yerman.produccion_api.application.dto.response.ProduccionLacteaResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.mapper.ProduccionLacteaRestMapper;
import com.yerman.produccion_api.domain.port.in.GestionProduccionLacteaUseCase;
import com.yerman.produccion_api.infrastructure.entity.UsuarioEntity;
import com.yerman.produccion_api.infrastructure.repository.UsuarioJpaRepository;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/producciones-lactea")
public class ProduccionLacteaController {

    private final GestionProduccionLacteaUseCase useCase;

    private final UsuarioJpaRepository usuarioRepository;

    public ProduccionLacteaController(
            GestionProduccionLacteaUseCase useCase,
            UsuarioJpaRepository usuarioRepository) {

        this.useCase = useCase;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProduccionLacteaResponse registrar(
            @Valid @RequestBody ProduccionLacteaRequest request,
            Authentication authentication) {

        String ccUsuario = authentication.getName();

        UsuarioEntity usuario = usuarioRepository
                .findByCc(ccUsuario)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario autenticado no encontrado"));

        request.setIdUsuario(usuario.getIdUsuario());

        return ProduccionLacteaRestMapper.toResponse(
                useCase.registrarProduccion(
                        ProduccionLacteaRestMapper.toDomain(request)));
    }

    @GetMapping("/{id}")
    public ProduccionLacteaResponse obtener(@PathVariable Long id) {

        return ProduccionLacteaRestMapper.toResponse(
                useCase.obtenerPorId(id));
    }

    @GetMapping
    public List<ProduccionLacteaResponse> listar() {

        return useCase.listarTodas()
                .stream()
                .map(ProduccionLacteaRestMapper::toResponse)
                .toList();
    }

    @GetMapping("/fecha/{fecha}")
    public List<ProduccionLacteaResponse> listarPorFecha(
            @PathVariable LocalDate fecha) {

        return useCase.listarPorFecha(fecha)
                .stream()
                .map(ProduccionLacteaRestMapper::toResponse)
                .toList();
    }
}
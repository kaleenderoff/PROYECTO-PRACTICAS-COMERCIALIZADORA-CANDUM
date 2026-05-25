package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.infrastructure.entity.LogAuditoriaEntity;
import com.yerman.produccion_api.infrastructure.repository.LogAuditoriaJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.UsuarioJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/auditoria")
public class AuditoriaController {

    private final LogAuditoriaJpaRepository repository;
    private final UsuarioJpaRepository usuarioRepository;

    public AuditoriaController(
            LogAuditoriaJpaRepository repository,
            UsuarioJpaRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<AuditoriaResponse> listarUltimos(
            @RequestParam(defaultValue = "100") int limite) {

        int limiteSeguro = Math.max(1, Math.min(limite, 500));

        return repository.findAll(
                        PageRequest.of(
                                0,
                                limiteSeguro,
                                Sort.by(Sort.Direction.DESC, "fechaHora")))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private AuditoriaResponse toResponse(LogAuditoriaEntity entity) {
        return new AuditoriaResponse(
                entity.getId(),
                entity.getIdUsuario(),
                nombreUsuario(entity.getIdUsuario()),
                entity.getAccion(),
                entity.getEntidadAfectada(),
                entity.getIdRegistroAfectado(),
                entity.getDetalle(),
                entity.getFechaHora());
    }

    private String nombreUsuario(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .map(usuario -> usuario.getPrimerNombre() + " " + usuario.getPrimerApellido())
                .orElse("Usuario #" + idUsuario);
    }

    public record AuditoriaResponse(
            Long id,
            Long idUsuario,
            String nombreUsuario,
            String accion,
            String entidadAfectada,
            Long idRegistroAfectado,
            String detalle,
            LocalDateTime fechaHora) {
    }
}

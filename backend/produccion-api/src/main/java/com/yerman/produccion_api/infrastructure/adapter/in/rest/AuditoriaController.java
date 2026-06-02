package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.infrastructure.entity.LogAuditoriaEntity;
import com.yerman.produccion_api.infrastructure.repository.LogAuditoriaJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.UsuarioJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/auditoria")
public class AuditoriaController {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

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
            @RequestParam(defaultValue = "100") int limite,
            Authentication authentication) {

        int limiteSeguro = Math.max(1, Math.min(limite, 500));
        boolean puedeVerDetalleTecnico = esAdmin(authentication);

        return repository.findAll(
                        PageRequest.of(
                                0,
                                limiteSeguro,
                                Sort.by(Sort.Direction.DESC, "fechaHora")))
                .stream()
                .map(entity -> toResponse(entity, puedeVerDetalleTecnico))
                .toList();
    }

    private AuditoriaResponse toResponse(
            LogAuditoriaEntity entity,
            boolean puedeVerDetalleTecnico) {

        return new AuditoriaResponse(
                entity.getId(),
                entity.getIdUsuario(),
                nombreUsuario(entity.getIdUsuario()),
                entity.getAccion(),
                entity.getEntidadAfectada(),
                entity.getIdRegistroAfectado(),
                puedeVerDetalleTecnico ? entity.getDetalle() : null,
                entity.getFechaHora());
    }

    private boolean esAdmin(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }

        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> ROLE_ADMIN.equals(authority.getAuthority()));
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
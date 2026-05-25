package com.yerman.produccion_api.infrastructure.security;

import com.yerman.produccion_api.infrastructure.entity.LogAuditoriaEntity;
import com.yerman.produccion_api.infrastructure.repository.LogAuditoriaJpaRepository;
import com.yerman.produccion_api.infrastructure.repository.UsuarioJpaRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

@Component
public class AuditoriaHttpFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AuditoriaHttpFilter.class);

    private final LogAuditoriaJpaRepository auditoriaRepository;
    private final UsuarioJpaRepository usuarioRepository;

    public AuditoriaHttpFilter(
            LogAuditoriaJpaRepository auditoriaRepository,
            UsuarioJpaRepository usuarioRepository) {
        this.auditoriaRepository = auditoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        filterChain.doFilter(request, response);

        if (!debeAuditar(request, response)) {
            return;
        }

        try {
            registrarAuditoria(request, response);
        } catch (Exception ex) {
            log.warn("No se pudo registrar auditoria para {} {}: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    ex.getMessage());
        }
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return true;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return true;
    }

    private boolean debeAuditar(HttpServletRequest request, HttpServletResponse response) {
        String method = request.getMethod();
        String path = normalizarPath(request);

        boolean metodoAuditable = "POST".equals(method)
                || "PUT".equals(method)
                || "PATCH".equals(method)
                || "DELETE".equals(method);

        return metodoAuditable
                && response.getStatus() < 400
                && !path.startsWith("/auth")
                && !path.startsWith("/actuator");
    }

    private void registrarAuditoria(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return;
        }

        String cc = authentication.getName();
        if (cc == null || "anonymousUser".equals(cc)) {
            return;
        }

        Optional<Long> idUsuario = usuarioRepository.findByCc(cc)
                .map(usuario -> usuario.getIdUsuario());

        if (idUsuario.isEmpty()) {
            return;
        }

        String path = normalizarPath(request);

        LogAuditoriaEntity auditoria = new LogAuditoriaEntity();
        auditoria.setIdUsuario(idUsuario.get());
        auditoria.setAccion(accionDesdeMetodo(request.getMethod()));
        auditoria.setEntidadAfectada(entidadDesdePath(path));
        auditoria.setIdRegistroAfectado(idDesdePath(path).orElse(null));
        auditoria.setDetalle(detalle(request, response, path));

        auditoriaRepository.save(auditoria);
    }

    private String normalizarPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        if (contextPath != null && !contextPath.isBlank() && uri.startsWith(contextPath)) {
            return uri.substring(contextPath.length());
        }

        return uri;
    }

    private String accionDesdeMetodo(String method) {
        return switch (method) {
            case "POST" -> "CREAR";
            case "PUT" -> "ACTUALIZAR";
            case "PATCH" -> "CAMBIAR_ESTADO";
            case "DELETE" -> "ELIMINAR";
            default -> method;
        };
    }

    private String entidadDesdePath(String path) {
        String[] segmentos = path.split("/");
        StringBuilder entidad = new StringBuilder();

        for (String segmento : segmentos) {
            if (!segmento.isBlank() && !esNumero(segmento)) {
                if (!entidad.isEmpty()) {
                    entidad.append("_");
                }
                entidad.append(segmento.replace("-", "_").toUpperCase(Locale.ROOT));

                if (entidad.toString().length() >= 60 || entidad.toString().split("_").length >= 4) {
                    break;
                }
            }
        }

        return entidad.isEmpty() ? "DESCONOCIDA" : entidad.toString();
    }

    private Optional<Long> idDesdePath(String path) {
        String[] segmentos = path.split("/");
        for (String segmento : segmentos) {
            if (esNumero(segmento)) {
                return Optional.of(Long.parseLong(segmento));
            }
        }

        return Optional.empty();
    }

    private boolean esNumero(String valor) {
        if (valor == null || valor.isBlank()) {
            return false;
        }

        for (int i = 0; i < valor.length(); i++) {
            if (!Character.isDigit(valor.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    private String detalle(HttpServletRequest request, HttpServletResponse response, String path) {
        String query = request.getQueryString();
        StringBuilder detalle = new StringBuilder();
        detalle.append("method=").append(request.getMethod());
        detalle.append("; path=").append(path);
        detalle.append("; status=").append(response.getStatus());

        if (query != null && !query.isBlank()) {
            detalle.append("; query=").append(query);
        }

        return detalle.toString();
    }
}

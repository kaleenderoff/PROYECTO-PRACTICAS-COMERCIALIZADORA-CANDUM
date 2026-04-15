package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.response.LogAuditoriaResponse;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.application.mapper.LogAuditoriaMapper;
import com.yerman.produccion_api.domain.model.EntidadAuditoria;
import com.yerman.produccion_api.domain.port.in.GestionAuditoriaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditoria")
public class LogAuditoriaController {

    private final GestionAuditoriaUseCase gestionAuditoriaUseCase;

    public LogAuditoriaController(GestionAuditoriaUseCase gestionAuditoriaUseCase) {
        this.gestionAuditoriaUseCase = gestionAuditoriaUseCase;
    }

    @GetMapping
    public ResponseEntity<List<LogAuditoriaResponse>> listarTodos() {
        List<LogAuditoriaResponse> response = gestionAuditoriaUseCase.listarTodos()
                .stream()
                .map(LogAuditoriaMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<LogAuditoriaResponse>> listarPorUsuario(
            @PathVariable Long idUsuario) {

        List<LogAuditoriaResponse> response = gestionAuditoriaUseCase
                .listarPorUsuario(idUsuario)
                .stream()
                .map(LogAuditoriaMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/entidad/{entidad}")
    public ResponseEntity<List<LogAuditoriaResponse>> listarPorEntidad(
            @PathVariable String entidad) {

        EntidadAuditoria entidadEnum;

        try {
            entidadEnum = EntidadAuditoria.valueOf(entidad.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ReglaNegocioException("Entidad de auditoría inválida: " + entidad);
        }

        List<LogAuditoriaResponse> response = gestionAuditoriaUseCase
                .listarPorEntidad(entidadEnum)
                .stream()
                .map(LogAuditoriaMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }
}
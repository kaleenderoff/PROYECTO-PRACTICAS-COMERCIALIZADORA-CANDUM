package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.ChangePasswordRequest;
import com.yerman.produccion_api.application.dto.request.CreateUsuarioRequest;
import com.yerman.produccion_api.application.dto.request.ResetPasswordRequest;
import com.yerman.produccion_api.application.dto.request.UpdateUsuarioRequest;
import com.yerman.produccion_api.application.dto.response.MeResponse;
import com.yerman.produccion_api.application.dto.response.PageResponse;
import com.yerman.produccion_api.application.dto.response.UsuarioResponse;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.mapper.UsuarioMapper;
import com.yerman.produccion_api.domain.model.Usuario;
import com.yerman.produccion_api.domain.port.in.GestionUsuarioUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsuarioController {

    private final GestionUsuarioUseCase gestionUsuarioUseCase;

    public UsuarioController(GestionUsuarioUseCase gestionUsuarioUseCase) {
        this.gestionUsuarioUseCase = gestionUsuarioUseCase;
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponse>> listarUsuariosActivos() {
        List<UsuarioResponse> response = gestionUsuarioUseCase.listarUsuariosActivos()
                .stream()
                .map(UsuarioMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuarios/todos")
    public ResponseEntity<List<UsuarioResponse>> listarTodosLosUsuarios() {
        List<UsuarioResponse> response = gestionUsuarioUseCase.listarUsuarios()
                .stream()
                .map(UsuarioMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponse> obtenerUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = gestionUsuarioUseCase.obtenerUsuarioPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con id: " + id));

        return ResponseEntity.ok(UsuarioMapper.toResponse(usuario));
    }

    @GetMapping("/usuarios/cc/{cc}")
    public ResponseEntity<UsuarioResponse> obtenerUsuarioPorCc(@PathVariable String cc) {
        Usuario usuario = gestionUsuarioUseCase.obtenerUsuarioPorCc(cc)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado con cc: " + cc));

        return ResponseEntity.ok(UsuarioMapper.toResponse(usuario));
    }

    @GetMapping("/usuarios/rol/{rol}")
    public ResponseEntity<List<UsuarioResponse>> listarPorRol(@PathVariable String rol) {
        List<UsuarioResponse> response = gestionUsuarioUseCase.listarPorRol(rol)
                .stream()
                .map(UsuarioMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody CreateUsuarioRequest request) {
        Usuario usuario = UsuarioMapper.toDomain(request);
        Usuario creado = gestionUsuarioUseCase.crearUsuario(usuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UsuarioMapper.toResponse(creado));
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUsuarioRequest request) {

        Usuario usuario = UsuarioMapper.toDomain(request);
        Usuario actualizado = gestionUsuarioUseCase.actualizarUsuario(id, usuario);

        return ResponseEntity.ok(UsuarioMapper.toResponse(actualizado));
    }

    @PatchMapping("/usuarios/{id}/desactivar")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable Long id) {
        gestionUsuarioUseCase.desactivarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/usuarios/{id}/activar")
    public ResponseEntity<Void> activarUsuario(@PathVariable Long id) {
        gestionUsuarioUseCase.activarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuarios/paginado")
    public ResponseEntity<PageResponse<UsuarioResponse>> listarUsuariosActivosPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(
                gestionUsuarioUseCase.listarUsuariosActivosPaginado(page, size));
    }

    @GetMapping("/usuarios/todos/paginado")
    public ResponseEntity<PageResponse<UsuarioResponse>> listarTodosPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return ResponseEntity.ok(
                gestionUsuarioUseCase.listarTodosPaginado(page, size));
    }

    @PatchMapping("/usuarios/{id}/reset-password")
    public ResponseEntity<Void> resetearPassword(
            @PathVariable Long id,
            @Valid @RequestBody ResetPasswordRequest request) {

        gestionUsuarioUseCase.resetearPassword(id, request.getNuevaPassword());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/usuarios/mi-password")
    public ResponseEntity<Void> cambiarMiPassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {

        String cc = authentication.getName();

        gestionUsuarioUseCase.cambiarMiPassword(
                cc,
                request.getPasswordActual(),
                request.getNuevaPassword());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(Authentication authentication) {
        String cc = authentication.getName();

        Usuario usuario = gestionUsuarioUseCase.obtenerUsuarioPorCc(cc)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Usuario no encontrado: " + cc));

        MeResponse response = new MeResponse(
                usuario.getCc(),
                usuario.getPrimerNombre(),
                usuario.getSegundoNombre(),
                usuario.getPrimerApellido(),
                usuario.getSegundoApellido(),
                usuario.getRol().name());

        return ResponseEntity.ok(response);
    }
}

package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.request.LoginRequest;
import com.yerman.produccion_api.application.dto.response.AuthResponse;
import com.yerman.produccion_api.domain.port.in.LoginUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

        private final LoginUseCase loginUseCase;

        public AuthController(LoginUseCase loginUseCase) {
                this.loginUseCase = loginUseCase;
        }

        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
                return ResponseEntity.ok(loginUseCase.login(request));
        }
}
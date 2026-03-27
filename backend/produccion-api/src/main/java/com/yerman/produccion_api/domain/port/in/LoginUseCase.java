package com.yerman.produccion_api.domain.port.in;

import com.yerman.produccion_api.application.dto.request.LoginRequest;
import com.yerman.produccion_api.application.dto.response.AuthResponse;

public interface LoginUseCase {
    AuthResponse login(LoginRequest request);
}

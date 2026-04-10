package com.yerman.produccion_api.infrastructure.adapter.in.rest;

import com.yerman.produccion_api.application.dto.response.ErrorResponse;
import com.yerman.produccion_api.application.exception.CcDuplicadaException;
import com.yerman.produccion_api.application.exception.PasswordIncorrectaException;
import com.yerman.produccion_api.application.exception.RecursoDuplicadoException;
import com.yerman.produccion_api.application.exception.RecursoNoEncontradoException;
import com.yerman.produccion_api.application.exception.ReglaNegocioException;
import com.yerman.produccion_api.application.exception.UsuarioInactivoException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        @ExceptionHandler(UsuarioInactivoException.class)
        public ResponseEntity<ErrorResponse> handleUsuarioInactivo(
                        UsuarioInactivoException ex,
                        HttpServletRequest request) {

                log.warn("Usuario inactivo en {} {}: {}",
                                request.getMethod(), request.getRequestURI(), ex.getMessage());

                ErrorResponse error = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.FORBIDDEN.value(),
                                "Forbidden",
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        }

        @ExceptionHandler(RecursoNoEncontradoException.class)
        public ResponseEntity<ErrorResponse> handleRecursoNoEncontrado(
                        RecursoNoEncontradoException ex,
                        HttpServletRequest request) {

                log.warn("Recurso no encontrado en {} {}: {}",
                                request.getMethod(), request.getRequestURI(), ex.getMessage());

                ErrorResponse error = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.NOT_FOUND.value(),
                                "Not Found",
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        @ExceptionHandler({
                        CcDuplicadaException.class,
                        RecursoDuplicadoException.class
        })
        public ResponseEntity<ErrorResponse> handleRecursoDuplicado(
                        RuntimeException ex,
                        HttpServletRequest request) {

                log.warn("Recurso duplicado en {} {}: {}",
                                request.getMethod(), request.getRequestURI(), ex.getMessage());

                ErrorResponse error = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.CONFLICT.value(),
                                "Conflict",
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler({
                        ReglaNegocioException.class,
                        PasswordIncorrectaException.class
        })
        public ResponseEntity<ErrorResponse> handleBadRequest(
                        RuntimeException ex,
                        HttpServletRequest request) {

                log.warn("Regla de negocio / solicitud inválida en {} {}: {}",
                                request.getMethod(), request.getRequestURI(), ex.getMessage());

                ErrorResponse error = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
                        DataIntegrityViolationException ex,
                        HttpServletRequest request) {

                String message = "Error de integridad en la base de datos";

                if (ex.getMostSpecificCause() != null &&
                                ex.getMostSpecificCause().getMessage() != null) {

                        String dbMessage = ex.getMostSpecificCause().getMessage().toLowerCase();

                        if (dbMessage.contains("cc") || dbMessage.contains("uk_usuario_cc")) {
                                message = "Ya existe un usuario con esa cédula";
                        } else if (dbMessage.contains("email")) {
                                message = "Ya existe un usuario con ese email";
                        } else if (dbMessage.contains("uq_producto")) {
                                message = "Ya existe un producto con esos datos";
                        } else if (dbMessage.contains("uq_insumo_nombre")) {
                                message = "Ya existe un insumo con ese nombre";
                        } else if (dbMessage.contains("uq_validacion_detalle")) {
                                message = "Ya existe una validación para el detalle de producción indicado";
                        } else if (dbMessage.contains("linea_produccion")) {
                                message = "La línea de producción asociada no es válida";
                        }
                }

                log.warn("Error de integridad en {} {}: {}",
                                request.getMethod(), request.getRequestURI(), message, ex);

                ErrorResponse error = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.CONFLICT.value(),
                                "Conflict",
                                message,
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgument(
                        IllegalArgumentException ex,
                        HttpServletRequest request) {

                log.warn("Argumento inválido en {} {}: {}",
                                request.getMethod(), request.getRequestURI(), ex.getMessage());

                ErrorResponse error = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGeneralException(
                        Exception ex,
                        HttpServletRequest request) {

                log.error("Error inesperado en {} {}",
                                request.getMethod(), request.getRequestURI(), ex);

                ErrorResponse error = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                "Ocurrió un error inesperado en el servidor",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
}

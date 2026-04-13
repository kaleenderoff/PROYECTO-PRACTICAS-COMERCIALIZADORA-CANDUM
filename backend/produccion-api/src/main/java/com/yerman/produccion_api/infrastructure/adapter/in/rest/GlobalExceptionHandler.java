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
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        private static final String BAD_REQUEST = HttpStatus.BAD_REQUEST.getReasonPhrase();
        private static final String CONFLICT = HttpStatus.CONFLICT.getReasonPhrase();
        private static final String INTERNAL_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
        private static final String NOT_FOUND = HttpStatus.NOT_FOUND.getReasonPhrase();
        private static final String FORBIDDEN = HttpStatus.FORBIDDEN.getReasonPhrase();

        @ExceptionHandler(UsuarioInactivoException.class)
        public ResponseEntity<ErrorResponse> handleUsuarioInactivo(
                        UsuarioInactivoException ex,
                        HttpServletRequest request) {

                log.warn("Usuario inactivo en {} {}: {}",
                                request.getMethod(), request.getRequestURI(), ex.getMessage());

                return buildResponse(HttpStatus.FORBIDDEN, FORBIDDEN, ex.getMessage(), request);
        }

        @ExceptionHandler(RecursoNoEncontradoException.class)
        public ResponseEntity<ErrorResponse> handleRecursoNoEncontrado(
                        RecursoNoEncontradoException ex,
                        HttpServletRequest request) {

                log.warn("Recurso no encontrado en {} {}: {}",
                                request.getMethod(), request.getRequestURI(), ex.getMessage());

                return buildResponse(HttpStatus.NOT_FOUND, NOT_FOUND, ex.getMessage(), request);
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

                return buildResponse(HttpStatus.CONFLICT, CONFLICT, ex.getMessage(), request);
        }

        @ExceptionHandler({
                        ReglaNegocioException.class,
                        PasswordIncorrectaException.class,
                        IllegalArgumentException.class
        })
        public ResponseEntity<ErrorResponse> handleBadRequest(
                        RuntimeException ex,
                        HttpServletRequest request) {

                log.warn("Regla de negocio / solicitud inválida en {} {}: {}",
                                request.getMethod(), request.getRequestURI(), ex.getMessage());

                return buildResponse(HttpStatus.BAD_REQUEST, BAD_REQUEST, ex.getMessage(), request);
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
                        DataIntegrityViolationException ex,
                        HttpServletRequest request) {

                String message = "Error de integridad en la base de datos";

                Throwable cause = ex.getMostSpecificCause();
                String dbMessage = cause.getMessage();

                if (dbMessage != null) {
                        dbMessage = dbMessage.toLowerCase();

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

                return buildResponse(HttpStatus.CONFLICT, CONFLICT, message, request);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
                        HttpMessageNotReadableException ex,
                        HttpServletRequest request) {

                return buildResponse(
                                HttpStatus.BAD_REQUEST,
                                BAD_REQUEST,
                                "El cuerpo de la solicitud contiene tipos de datos inválidos",
                                request);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGeneralException(
                        Exception ex,
                        HttpServletRequest request) {

                log.error("Error inesperado en {} {}",
                                request.getMethod(), request.getRequestURI(), ex);

                return buildResponse(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                INTERNAL_ERROR,
                                "Ocurrió un error inesperado en el servidor",
                                request);
        }

        private ResponseEntity<ErrorResponse> buildResponse(
                        HttpStatus status,
                        String error,
                        String message,
                        HttpServletRequest request) {

                ErrorResponse response = new ErrorResponse(
                                LocalDateTime.now(),
                                status.value(),
                                error,
                                message,
                                request.getRequestURI());

                return ResponseEntity.status(status).body(response);
        }
}
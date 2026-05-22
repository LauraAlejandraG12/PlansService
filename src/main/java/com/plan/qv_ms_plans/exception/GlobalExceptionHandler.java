package com.plan.qv_ms_plans.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones del microservicio de planes.
 *
 * <p>Intercepta las excepciones lanzadas en cualquier controlador
 * y retorna respuestas HTTP estructuradas y consistentes al cliente.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Maneja excepciones de entidad no encontrada.
     * Retorna HTTP 404.
     *
     * @param ex excepción lanzada
     * @return respuesta con mensaje de error
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error("Entidad no encontrada: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /**
     * Maneja excepciones de argumento inválido como nombre duplicado.
     * Retorna HTTP 400.
     *
     * @param ex excepción lanzada
     * @return respuesta con mensaje de error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Argumento inválido: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /**
     * Maneja excepciones de estado inválido como eliminar un plan asignado.
     * Retorna HTTP 409.
     *
     * @param ex excepción lanzada
     * @return respuesta con mensaje de error
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalStateException(IllegalStateException ex) {
        log.error("Estado inválido: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Maneja excepciones de validación de campos del request (HU36, HU40).
     * Retorna HTTP 400 con el detalle de cada campo inválido.
     *
     * @param ex excepción lanzada
     * @return respuesta con mapa de errores por campo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Error de validación: {}", ex.getMessage());

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Error de validación");
        body.put("messages", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Maneja excepciones de lógica de negocio personalizadas.
     * Retorna el código HTTP definido en la excepción.
     *
     * @param ex excepción lanzada
     * @return respuesta con mensaje de error
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {
        log.error("Error de negocio: {}", ex.getMessage());
        return buildResponse(ex.getStatus(), ex.getMessage());
    }

    /**
     * Maneja cualquier excepción no controlada.
     * Retorna HTTP 500.
     *
     * @param ex excepción lanzada
     * @return respuesta con mensaje de error genérico
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Error inesperado: {}", ex.getMessage());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error inesperado. Por favor intente nuevamente.");
    }

    /**
     * Construye una respuesta HTTP estructurada con timestamp, status y mensaje.
     *
     * @param status código HTTP de la respuesta
     * @param message mensaje descriptivo del error
     * @return respuesta estructurada lista para enviar al cliente
     */
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}

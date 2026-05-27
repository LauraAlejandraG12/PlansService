package com.plan.qv_ms_plans.model.dto.plans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO genérico para estandarizar las respuestas del microservicio.
 *
 * <p>Envuelve cualquier respuesta con un mensaje descriptivo,
 * el estado de la operación y la fecha y hora de la respuesta.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDTO<T> {
    /**
     * Mensaje descriptivo de la operación realizada.
     */
    private String message;

    /**
     * Indica si la operación fue exitosa.
     */
    private boolean success;

    /**
     * Datos retornados por la operación.
     */
    private T data;

    /**
     * Fecha y hora de la respuesta.
     */
    private LocalDateTime timestamp;

    /**
     * Crea una respuesta exitosa con mensaje y datos.
     *
     * @param message mensaje descriptivo
     * @param data datos a retornar
     * @return respuesta exitosa
     */
    public  static <T> MessageResponseDTO<T> success(String message, T data) {
        return new MessageResponseDTO<>(message, true, data, LocalDateTime.now());
    }

    /**
     * Crea una respuesta exitosa sin datos.
     *
     * @param message mensaje descriptivo
     * @return respuesta exitosa sin datos
     */
    public static <T> MessageResponseDTO<T> success(String message) {
        return new MessageResponseDTO<>(message, true, null, LocalDateTime.now());
    }
}

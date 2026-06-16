package com.plan.qv_ms_plans.model.dto.plans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para una notificación de la bandeja del usuario.
 *
 * <p>Mantiene la misma estructura que el DTO de notificaciones del auth-service
 * para que el frontend pueda unificar las notificaciones de todos los microservicios.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {

    /** Identificador de la notificación */
    private Long id;

    /** Tipo de notificación */
    private String type;

    /** Título visible en la bandeja */
    private String title;

    /** Mensaje descriptivo */
    private String message;

    /** Indica si la notificación fue leída */
    private Boolean read;

    /** Indica si la notificación no debe mostrarse como alerta emergente */
    private Boolean silent;

    /** Estado de almacenamiento o envío */
    private String status;

    /** Fecha de creación */
    private LocalDateTime createdAt;
}
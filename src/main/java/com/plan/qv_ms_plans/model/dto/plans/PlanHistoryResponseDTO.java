package com.plan.qv_ms_plans.model.dto.plans;

import com.plan.qv_ms_plans.model.enums.UserPlanStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para el envío de datos del historial de planes de un organizador.
 *
 * <p>Representa la información que se retorna al cliente al consultar
 * el historial de planes asignados a un organizador.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Data
public class PlanHistoryResponseDTO {

    /** Identificador único del registro de historial. */
    private Long idHistory;

    /** Información del plan asociado al historial. */
    private PlanResponseDTO plan;

    /** Fecha de inicio del plan en este registro. */
    private LocalDate startDate;

    /** Fecha de vencimiento del plan en este registro. */
    private LocalDate endDate;

    /** Estado del plan en el momento del registro. */
    private UserPlanStatus status;

    /** Motivo del cambio o renovación. */
    private String reason;

    /** Fecha y hora en que se creó el registro. */
    private LocalDateTime createdAt;
}

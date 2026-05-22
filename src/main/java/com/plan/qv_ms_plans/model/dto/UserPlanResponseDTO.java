package com.plan.qv_ms_plans.model.dto;

import com.plan.qv_ms_plans.model.enums.UserPlanStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para el envío de datos de un plan asignado a un organizador.
 *
 * <p>Representa la información que se retorna al cliente tras
 * consultar, asignar o renovar un plan a un organizador.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Data
public class UserPlanResponseDTO {

    /** Identificador único de la asignación. */
    private Long idUserPlan;

    /** ID del organizador al que se le asignó el plan. */
    private Long userId;

    /** Información del plan asignado. */
    private PlanResponseDTO plan;

    /** Fecha de inicio de la asignación. */
    private LocalDate startDate;

    /** Fecha de vencimiento de la asignación. */
    private LocalDate endDate;

    /** Estado actual de la asignación. */
    private UserPlanStatus status;

    /** Indica si el plan ha sido renovado al menos una vez. */
    private Boolean renewal;

    /** Fecha y hora en que se realizó la asignación. */
    private LocalDateTime createdAt;
}

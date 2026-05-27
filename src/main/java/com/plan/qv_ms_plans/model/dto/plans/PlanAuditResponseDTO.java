package com.plan.qv_ms_plans.model.dto.plans;

import com.plan.qv_ms_plans.model.enums.AuditAction;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO para el envío de datos de la bitácora de auditoría de planes.
 *
 * <p>Representa la información que se retorna al cliente al consultar
 * los registros de auditoría de acciones realizadas sobre los planes.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Data
public class PlanAuditResponseDTO {

    /** Identificador único del registro de auditoría. */
    private Long idAudit;

    /** Información del plan auditado. */
    private PlanResponseDTO plan;

    /** ID del administrador que realizó la acción. */
    private Long userId;

    /** Tipo de acción realizada sobre el plan. */
    private AuditAction action;

    /** Descripción detallada de los cambios realizados. */
    private String changeDescription;

    /** Fecha y hora en que se registró la auditoría. */
    private LocalDateTime auditDate;
}

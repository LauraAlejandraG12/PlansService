package com.plan.qv_ms_plans.model.dto.plans;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO para la recepción de datos al asignar o renovar un plan a un organizador.
 *
 * <p>Contiene las validaciones necesarias para garantizar la integridad
 * de los datos enviados desde el cliente.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Data
public class UserPlanRequestDTO {

    /**
     * ID del organizador al que se le asigna el plan.
     */
    @NotNull(message = "El ID del organizador es obligatorio.")
    private Long userId;

    /**
     * ID del plan a asignar.
     */
    @NotNull(message = "El ID del plan es obligatorio.")
    private Long planId;

    /**
     * Fecha de inicio de la asignación.
     */
    @NotNull(message = "La fecha de inicio es obligatoria.")
    private LocalDate startDate;

}
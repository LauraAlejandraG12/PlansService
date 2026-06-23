package com.plan.qv_ms_plans.model.dto.plans;

import com.plan.qv_ms_plans.model.enums.PlanStatus;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para el envío de datos de un plan de servicio al cliente.
 *
 * <p>Representa la información que se retorna al cliente tras
 * consultar, crear o actualizar un plan.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Data
public class PlanResponseDTO {

    /** Identificador único del plan. */
    private Long idPlan;

    /** Nombre del plan. */
    private String name;

    /** Descripción del plan. */
    private String description;

    /** Precio del plan. */
    private BigDecimal price;

    /** Duración del plan en días. */
    private Integer durationDays;

    /** Cantidad máxima de eventos permitidos */
    private Integer maxEvents;

    /** Cantidad máxima de organizadores. */
    private Integer maxOrganizers;

    /** Cantidad máxima de invitados (jurado, participante, asistente). */
    private Integer maxGuests;

    /** Cantidad máxima de personal de apoyo. */
    private Integer maxStaff;

    /** Estado actual del plan. */
    private PlanStatus status;

    /** Fecha de creación del plan. */
    private LocalDateTime createdAt;

    /** Fecha de última actualización del plan. */
    private LocalDateTime updatedAt;
}

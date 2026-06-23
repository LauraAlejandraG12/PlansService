package com.plan.qv_ms_plans.model.dto.plans;

import com.plan.qv_ms_plans.model.enums.PlanStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * DTO para la recepción de datos al crear o editar un plan de servicio.
 *
 * <p>Contiene las validaciones necesarias para garantizar la integridad
 * de los datos enviados desde el cliente.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Data
public class PlanRequestDTO {

    /**
     * Nombre del plan. No puede estar vacío.
     */
    @NotBlank(message = "El nombre del plan es obligatorio.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String name;

    /**
     * Descripción detallada del plan.
     */
    private String description;

    /**
     * Precio del plan. Debe ser mayor a cero.
     */
    @NotNull(message = "El precio del plan es obligatorio.")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a cero.")
    private BigDecimal price;

    /**
     * Duración del plan en días. Debe ser al menos 1 día.
     */
    @NotNull(message = "La duración del plan es obligatoria.")
    @Min(value = 1, message = "La duración debe ser de al menos 1 día.")
    private Integer durationDays;

    /**
     * Cantidad máxima de eventos permitidos.
     */
    @Min(value = 0, message = "El valor no puede ser negativo.")
    private Integer maxEvents;

    /**
     * Cantidad máxima de organizadores permitidos.
     */
    @Min(value = 0, message = "El valor no puede ser negativo.")
    private Integer maxOrganizers = 0;

    /**
     * Cantidad máxima de invitados permitidos (jurado, participante, asistente).
     */
    @Min(value = 0, message = "El valor no puede ser negativo.")
    private Integer maxGuests = 0;

    /**
     * Cantidad máxima de personal de apoyo permitido.
     */
    @Min(value = 0, message = "El valor no puede ser negativo.")
    private Integer maxStaff = 0;

    /**
     * Estado del plan. Por defecto es activo.
     */
    private PlanStatus status = PlanStatus.active;
}

package com.plan.qv_ms_plans.model.enums;

/**
 * Enumeración de los posibles estados de un plan de servicio.
 *
 * <p>Se persiste como cadena de texto ({@code @Enumerated(EnumType.STRING)})
 * en la columna {@code status} de la tabla {@code plan}.</p>
 *
 * <ul>
 *   <li>{@link #active}   — El plan está disponible para ser asignado</li>
 *   <li>{@link #inactive} — El plan no está disponible para asignación</li>
 * </ul>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
public enum PlanStatus {

    /** El plan está activo y disponible en el sistema. */
    active,

    /** El plan está inactivo y no puede ser asignado. */
    inactive
}

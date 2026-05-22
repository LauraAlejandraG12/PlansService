package com.plan.qv_ms_plans.model.enums;

/**
 * Enumeración de los posibles estados de un plan asignado a un organizador.
 *
 * <p>Se persiste como cadena de texto ({@code @Enumerated(EnumType.STRING)})
 * en la columna {@code status} de las tablas {@code user_plan} y {@code plan_history}.</p>
 *
 * <ul>
 *   <li>{@link #active}    — El plan asignado está vigente</li>
 *   <li>{@link #expired}   — El plan asignado ha superado su fecha de vencimiento</li>
 *   <li>{@link #cancelled} — El plan asignado fue cancelado manualmente</li>
 * </ul>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
public enum UserPlanStatus {

    /**
     * El plan asignado está activo y vigente.
     */
    active,

    /**
     * El plan asignado ha vencido por superar su fecha de finalización.
     */
    expired,

    /**
     * El plan asignado fue cancelado.
     */
    cancelled

}
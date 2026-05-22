package com.plan.qv_ms_plans.model.enums;

/**
 * Enumeración de los tipos de acciones registradas en la bitácora de auditoría.
 *
 * <p>Se persiste como cadena de texto ({@code @Enumerated(EnumType.STRING)})
 * en la columna {@code action} de la tabla {@code plan_audit}.</p>
 *
 * <ul>
 *   <li>{@link #create} — Creación de un nuevo plan</li>
 *   <li>{@link #update} — Modificación de un plan existente</li>
 *   <li>{@link #delete} — Eliminación de un plan</li>
 *   <li>{@link #assign} — Asignación de un plan a un organizador</li>
 *   <li>{@link #renew}  — Renovación de un plan asignado</li>
 * </ul>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
public enum AuditAction {

    /** Se creó un nuevo plan en el sistema. */
    create,

    /** Se modificaron los datos de un plan existente. */
    update,

    /** Se eliminó un plan del sistema. */
    delete,

    /** Se asignó un plan a un organizador. */
    assign,

    /** Se renovó un plan asignado a un organizador. */
    renew
}
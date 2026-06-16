package com.plan.qv_ms_plans.model.enums;

/**
 * Tipos de notificaciones que genera el microservicio de planes.
 */
public enum NotificationType {
    /** Compra o adquisición exitosa de un plan */
    PLAN_PURCHASED,
    /** Renovación exitosa de un plan */
    PLAN_RENEWED,
    /** Cambio exitoso de plan */
    PLAN_CHANGED,
    /** Aviso de vencimiento próximo del plan */
    PLAN_EXPIRING,
    /** Plan vencido */
    PLAN_EXPIRED
}

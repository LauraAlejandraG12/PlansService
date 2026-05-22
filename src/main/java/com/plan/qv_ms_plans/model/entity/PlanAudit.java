package com.plan.qv_ms_plans.model.entity;

import com.plan.qv_ms_plans.model.enums.AuditAction;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entidad que representa un registro en la bitácora de auditoría de planes.
 *
 * <p>Mapea la tabla {@code plan_audit} de la base de datos. Registra cada
 * acción realizada sobre los planes del sistema, incluyendo quién la realizó,
 * qué tipo de acción fue y qué datos fueron modificados.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Data
@Entity
@Table(name = "plan_audit")
public class PlanAudit {

    /**
     * Identificador único del registro de auditoría. Se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_audit")
    private Long idAudit;

    /**
     * Plan sobre el cual se realizó la acción auditada.
     */
    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    /**
     * ID del administrador que realizó la acción.
     * Solo se almacena el ID porque los usuarios pertenecen a un microservicio independiente.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * Tipo de acción realizada sobre el plan.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private AuditAction action;

    /**
     * Descripción detallada de los cambios realizados.
     */
    @Column(name = "change_description", columnDefinition = "TEXT", nullable = false)
    private String changeDescription;

    /**
     * Fecha y hora en que se registró la auditoría. No se puede modificar.
     */
    @Column(name = "audit_date", updatable = false)
    private LocalDateTime auditDate;

    /**
     * Asigna {@code auditDate} antes de persistir el registro.
     */
    @PrePersist
    protected void onCreate() {
        auditDate = LocalDateTime.now();
    }
}
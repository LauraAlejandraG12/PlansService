package com.plan.qv_ms_plans.model.entity;

import com.plan.qv_ms_plans.model.enums.UserPlanStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad que representa el historial de planes asignados a un organizador.
 *
 * <p>Mapea la tabla {@code plan_history} de la base de datos. Cada registro
 * almacena el estado de una asignación en un momento específico, permitiendo
 * consultar el historial completo de cambios y renovaciones.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Data
@Entity
@Table(name = "plan_history")
public class PlanHistory {

    /**
     * Identificador único del registro de historial. Se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_history")
    private Long idHistory;

    /**
     * Asignación de plan a la que pertenece este registro de historial.
     */
    @ManyToOne
    @JoinColumn(name = "user_plan_id", nullable = false)
    private UserPlan userPlan;

    /**
     * Plan asociado a este registro de historial.
     */
    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    /**
     * Fecha de inicio del plan en este registro de historial.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * Fecha de vencimiento del plan en este registro de historial.
     */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /**
     * Estado del plan en el momento en que se generó este registro.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserPlanStatus status;

    /**
     * Motivo del cambio o renovación que generó este registro de historial.
     */
    @Column(name = "reason", length = 255)
    private String reason;

    /**
     * Fecha y hora en que se creó el registro. No se puede modificar.
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Asigna {@code createdAt} antes de persistir el registro.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

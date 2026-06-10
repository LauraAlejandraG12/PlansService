package com.plan.qv_ms_plans.model.entity;

import com.plan.qv_ms_plans.model.enums.PlanStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa un plan de servicio del sistema.
 *
 * <p>Mapea la tabla {@code plan} de la base de datos y contiene
 * la información principal del plan como nombre, precio, duración
 * y límites de usuarios por rol.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Data
@Entity
@Table(name = "plan")
public class Plan {

    /**
     * Identificador único del plan. Se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan")
    private Long idPlan;

    /**
     * Nombre del plan. No puede ser nulo.
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Descripción detallada del plan.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Precio del plan. No puede ser nulo.
     */
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * Duración del plan expresada en días.
     */
    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;

    /** Cantidad máxima de eventos permitidos en el plan */
    @Column(name = "max_events", nullable = false)
    private Integer maxEvents = 0;

    /**
     * Cantidad máxima de organizadores permitidos en el plan.
     */
    @Column(name = "max_organizers")
    private Integer maxOrganizers = 0;

    /**
     * Cantidad máxima de participantes permitidos en el plan.
     */
    @Column(name = "max_participants")
    private Integer maxParticipants = 0;

    /**
     * Cantidad máxima de jueces permitidos en el plan.
     */
    @Column(name = "max_judges")
    private Integer maxJudges = 0;

    /**
     * Cantidad máxima de asistentes permitidos en el plan.
     */
    @Column(name = "max_attendees")
    private Integer maxAttendees = 0;

    /**
     * Cantidad máxima de personal de apoyo permitido en el plan.
     */
    @Column(name = "max_staff")
    private Integer maxStaff = 0;

    /**
     * Indica si el plan fue eliminado lógicamente del sistema.
     * Los planes eliminados no aparecen en las consultas normales.
     */
    @Column(name = "deleted")
    private Boolean deleted = false;

    /**
     * Estado actual del plan. Por defecto es {@code active}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PlanStatus status = PlanStatus.active;

    /**
     * Fecha y hora de creación del plan. No se puede modificar.
     */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de la última actualización del plan.
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Asigna {@code createdAt} y {@code updatedAt} antes de persistir el registro.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Actualiza {@code updatedAt} antes de cada operación de actualización.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
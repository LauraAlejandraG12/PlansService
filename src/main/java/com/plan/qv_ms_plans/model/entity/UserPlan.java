package com.plan.qv_ms_plans.model.entity;

import com.plan.qv_ms_plans.model.enums.UserPlanStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad que representa la asignación de un plan a un organizador.
 *
 * <p>Mapea la tabla {@code user_plan} de la base de datos. Registra
 * la fecha de inicio, la fecha de vencimiento y el estado actual
 * del plan asignado al organizador.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Data
@Entity
@Table(name = "user_plan")
public class UserPlan {

    /**
     * Identificador único de la asignación. Se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user_plan")
    private Long idUserPlan;

    /**
     * ID del organizador al que se le asigna el plan.
     * Solo se almacena el ID porque los usuarios pertenecen a un microservicio independiente.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_name")
    private String userName;

    /**
     * Plan asignado al organizador.
     */
    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    /**
     * Fecha de inicio de la asignación del plan.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * Fecha de vencimiento del plan. Se calcula automáticamente
     * según la duración del plan seleccionado.
     */
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /**
     * Estado actual de la asignación. Por defecto es {@code active}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserPlanStatus status = UserPlanStatus.active;

    /**
     * Indica si el plan ha sido renovado al menos una vez.
     */
    @Column(name = "renewal")
    private Boolean renewal = false;

    /**
     * Fecha y hora en que se realizó la asignación. No se puede modificar.
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
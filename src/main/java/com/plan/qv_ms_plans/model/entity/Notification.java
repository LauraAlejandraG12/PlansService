package com.plan.qv_ms_plans.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entidad que representa una notificación generada por el microservicio de planes.
 *
 * <p>Sigue la misma estructura que las notificaciones del auth-service para que
 * el frontend pueda unificar las notificaciones de todos los microservicios.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Entity
@Table(name = "notification")
@Data
public class Notification {
    /** Identificador único de la notificación */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID del usuario destinatario de la notificación */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /** Correo del destinatario para el envío por email */
    @Column(name = "recipient_email")
    private String recipientEmail;

    /** Nombre del destinatario */
    @Column(name = "recipient_name")
    private String recipientName;

    /** Tipo de notificación */
    @Column(name = "type", nullable = false)
    private String type;

    /** Título visible de la notificación */
    @Column(name = "title", nullable = false)
    private String title;

    /** Mensaje descriptivo de la notificación */
    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    /** Indica si la notificación fue leída */
    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    /** Indica si la notificación no debe mostrarse como alerta emergente */
    @Column(name = "silent", nullable = false)
    private Boolean silent = true;

    /** Estado de almacenamiento o envío externo */
    @Column(name = "status", nullable = false)
    private String status = "STORED";

    /** Número de reintentos de envío por correo */
    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0;

    /** Último error de envío registrado */
    @Column(name = "last_error", length = 1000)
    private String lastError;

    /** Fecha de creación de la notificación */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Fecha de última actualización */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Inicializa las fechas antes de persistir.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Actualiza la fecha de modificación antes de cada update.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

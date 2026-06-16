package com.plan.qv_ms_plans.repository;

import com.plan.qv_ms_plans.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    /**
     * Retorna las notificaciones de un usuario ordenadas de más reciente a más antigua.
     *
     * @param userId ID del usuario
     * @return lista de notificaciones del usuario
     */
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * Elimina todas las notificaciones creadas antes de la fecha indicada.
     * Usado por el scheduler para borrar notificaciones de más de 30 días.
     *
     * @param date fecha límite; se eliminan las anteriores a esta
     * @return cantidad de registros eliminados
     */
    long deleteByCreatedAtBefore(LocalDateTime date);
}

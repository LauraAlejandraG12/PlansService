package com.plan.qv_ms_plans.scheduler;

import com.plan.qv_ms_plans.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Tarea programada que elimina automáticamente las notificaciones antiguas.
 *
 * <p>Se ejecuta todos los días a la medianoche y borra las notificaciones
 * con más de 30 días de antigüedad para mantener la base de datos limpia.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationCleanupScheduler {

    private final NotificationRepository notificationRepository;

    /**
     * Elimina las notificaciones de más de 30 días.
     * Se ejecuta diariamente a las 00:00 (medianoche).
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteOldNotifications() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        long deleted = notificationRepository.deleteByCreatedAtBefore(cutoffDate);
        log.info("Limpieza de notificaciones: {} registros eliminados (anteriores a {})",
                deleted, cutoffDate);
    }
}
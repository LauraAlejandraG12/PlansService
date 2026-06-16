package com.plan.qv_ms_plans.service.plans;

import com.plan.qv_ms_plans.model.dto.plans.NotificationResponseDTO;
import com.plan.qv_ms_plans.model.entity.Notification;
import com.plan.qv_ms_plans.model.enums.NotificationType;
import com.plan.qv_ms_plans.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para la gestión de notificaciones de planes.
 *
 * <p>Crea y almacena notificaciones internas, dispara el envío de correos
 * y permite al usuario consultarlas y marcarlas como leídas.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PlanEmailService planEmailService;

    /**
     * Crea y almacena una notificación, y envía el correo correspondiente.
     *
     * @param userId         ID del usuario destinatario
     * @param recipientEmail correo del destinatario
     * @param recipientName  nombre del destinatario
     * @param type           tipo de notificación
     * @param title          título de la notificación
     * @param message        mensaje descriptivo
     */
    public void createNotification(Long userId, String recipientEmail, String recipientName,
                                   NotificationType type, String title, String message) {
        // Guarda la notificación en la base de datos
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setRecipientEmail(recipientEmail);
        notification.setRecipientName(recipientName);
        notification.setType(type.name());
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setIsRead(false);
        notification.setSilent(false);
        notification.setStatus("STORED");

        notificationRepository.save(notification);
        log.info("Notificación creada para usuario ID: {} - tipo: {}", userId, type);

        // Envía el correo de forma asíncrona
        planEmailService.sendPlanNotificationEmail(recipientEmail, recipientName, title, message);
    }

    /**
     * Retorna las notificaciones de un usuario ordenadas de más reciente a más antigua.
     *
     * @param userId ID del usuario
     * @return lista de notificaciones del usuario
     */
    public List<NotificationResponseDTO> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Marca una notificación como leída.
     *
     * @param notificationId ID de la notificación
     */
    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Notificación no encontrada con ID: " + notificationId));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    /**
     * Convierte una entidad {@link Notification} a su DTO de respuesta.
     *
     * @param notification entidad a convertir
     * @return DTO listo para enviar al cliente
     */
    private NotificationResponseDTO toResponseDTO(Notification notification) {
        return NotificationResponseDTO.builder()
                .id(notification.getId())
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .read(notification.getIsRead())
                .silent(notification.getSilent())
                .status(notification.getStatus())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
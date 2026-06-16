package com.plan.qv_ms_plans.controller.plans;

import com.plan.qv_ms_plans.model.dto.plans.MessageResponseDTO;
import com.plan.qv_ms_plans.model.dto.plans.NotificationResponseDTO;
import com.plan.qv_ms_plans.service.plans.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la consulta de notificaciones de planes.
 *
 * <p>Expone los endpoints para que el usuario consulte sus notificaciones
 * y las marque como leídas. El frontend unifica estas notificaciones con
 * las de otros microservicios.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Retorna las notificaciones del usuario autenticado.
     *
     * @param userId ID del usuario obtenido del header inyectado por el Gateway
     * @return lista de notificaciones con HTTP 200
     */
    @GetMapping
    public ResponseEntity<MessageResponseDTO<List<NotificationResponseDTO>>> getMyNotifications(
            @RequestHeader("X-User-Id") Long userId) {
        log.info("Consultando notificaciones del usuario ID: {}", userId);
        List<NotificationResponseDTO> notifications =
                notificationService.getNotificationsByUser(userId);
        return ResponseEntity.ok(MessageResponseDTO.success(
                "Notificaciones obtenidas exitosamente.", notifications));
    }

    /**
     * Marca una notificación como leída.
     *
     * @param id ID de la notificación
     * @return confirmación con HTTP 200
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<MessageResponseDTO<Void>> markAsRead(@PathVariable Long id) {
        log.info("Marcando notificación ID: {} como leída", id);
        notificationService.markAsRead(id);
        return ResponseEntity.ok(MessageResponseDTO.success(
                "Notificación marcada como leída.", null));
    }
}
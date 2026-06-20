package com.plan.qv_ms_plans.scheduler;

import com.plan.qv_ms_plans.model.dto.plans.UserInfoDTO;
import com.plan.qv_ms_plans.model.entity.PlanHistory;
import com.plan.qv_ms_plans.model.entity.UserPlan;
import com.plan.qv_ms_plans.model.enums.AuditAction;
import com.plan.qv_ms_plans.model.enums.NotificationType;
import com.plan.qv_ms_plans.model.enums.UserPlanStatus;
import com.plan.qv_ms_plans.repository.PlanHistoryRepository;
import com.plan.qv_ms_plans.repository.UserPlanRepository;
import com.plan.qv_ms_plans.service.plans.NotificationService;
import com.plan.qv_ms_plans.service.plans.PlanAuditService;
import com.plan.qv_ms_plans.service.plans.UserClientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * Componente encargado del control automático de vigencia de planes (HU44).
 *
 * <p>Ejecuta tareas programadas para identificar planes vencidos o próximos
 * a vencer, actualizar su estado, notificar al organizador y registrar los
 * cambios en el historial y en la bitácora de auditoría.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PlanValidityScheduler {

    private final UserPlanRepository userPlanRepository;
    private final PlanHistoryRepository planHistoryRepository;
    private final PlanAuditService planAuditService;
    private final NotificationService notificationService;
    private final UserClientService userClientService;

    /** Días de anticipación para avisar que un plan está por vencer. */
    private static final int DIAS_AVISO_PREVIO = 3;

    /**
     * Verifica y actualiza el estado de los planes vencidos (HU44).
     *
     * <p>Se ejecuta automáticamente todos los días a medianoche.
     * Busca todos los planes activos cuya fecha de vencimiento ya pasó,
     * los marca como {@code expired} y notifica al organizador.</p>
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void checkExpiredPlans() {
        log.info("Iniciando verificación automática de planes vencidos...");

        List<UserPlan> activePlans = userPlanRepository.findByStatus(UserPlanStatus.active);
        int expiredCount = 0;

        for (UserPlan userPlan : activePlans) {
            if (userPlan.getEndDate().isBefore(LocalDate.now())) {
                userPlan.setStatus(UserPlanStatus.expired);
                userPlanRepository.save(userPlan);

                saveHistory(userPlan);

                planAuditService.registerAudit(
                        userPlan.getPlan(),
                        0L,
                        AuditAction.update,
                        "El plan: " + userPlan.getPlan().getName() +
                                " del organizador ID: " + userPlan.getUserId() +
                                " fue marcado como vencido automáticamente."
                );

                // Notifica al organizador que su plan venció
                notifyExpired(userPlan);

                expiredCount++;
            }
        }

        log.info("Verificación completada. Planes vencidos actualizados: {}", expiredCount);
    }

    /**
     * Avisa a los organizadores cuyos planes vencen en {@value #DIAS_AVISO_PREVIO} días.
     *
     * <p>Se ejecuta automáticamente todos los días a medianoche.</p>
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void checkExpiringPlans() {
        log.info("Iniciando verificación de planes próximos a vencer...");

        LocalDate targetDate = LocalDate.now().plusDays(DIAS_AVISO_PREVIO);
        List<UserPlan> activePlans = userPlanRepository.findByStatus(UserPlanStatus.active);
        int expiringCount = 0;

        for (UserPlan userPlan : activePlans) {
            if (userPlan.getEndDate().isEqual(targetDate)) {
                log.info("¡Plan {} vence en 3 días! Notificando...", userPlan.getUserId());
                notifyExpiring(userPlan);
                expiringCount++;
            }
        }

        log.info("Verificación completada. Avisos de vencimiento próximo enviados: {}", expiringCount);
    }

    /**
     * Envía la notificación de plan próximo a vencer (PLAN_EXPIRING).
     */
    private void notifyExpiring(UserPlan userPlan) {
        UserInfoDTO userInfo = userClientService.getUserById(userPlan.getUserId());
        if (userInfo == null || userInfo.getEmail() == null) {
            log.warn("No se pudo notificar vencimiento próximo: no se obtuvo info del usuario ID {}.", userPlan.getUserId());
            return;
        }

        String title = "Tu plan está por vencer";
        String message = "Hola " + userInfo.getFullName() + ", tu plan \"" + userPlan.getPlan().getName() +
                "\" vencerá el " + userPlan.getEndDate() + ". Renuévalo para no perder el acceso a tus eventos.";

        notificationService.createNotification(
                userPlan.getUserId(),
                userInfo.getEmail(),
                userInfo.getFullName(),
                NotificationType.PLAN_EXPIRING,
                title, message
        );
    }

    /**
     * Envía la notificación de plan vencido (PLAN_EXPIRED).
     */
    private void notifyExpired(UserPlan userPlan) {
        UserInfoDTO userInfo = userClientService.getUserById(userPlan.getUserId());
        if (userInfo == null || userInfo.getEmail() == null) {
            log.warn("No se pudo notificar vencimiento: no se obtuvo info del usuario ID {}.", userPlan.getUserId());
            return;
        }

        String title = "Tu plan ha vencido";
        String message = "Hola " + userInfo.getFullName() + ", tu plan \"" + userPlan.getPlan().getName() +
                "\" venció el " + userPlan.getEndDate() + ". Renuévalo o adquiere uno nuevo para seguir gestionando tus eventos.";

        notificationService.createNotification(
                userPlan.getUserId(),
                userInfo.getEmail(),
                userInfo.getFullName(),
                NotificationType.PLAN_EXPIRED,
                title, message
        );
    }

    /**
     * Guarda un registro en el historial cuando un plan vence automáticamente.
     *
     * @param userPlan asignación de plan que venció
     */
    private void saveHistory(UserPlan userPlan) {
        PlanHistory history = new PlanHistory();
        history.setUserPlan(userPlan);
        history.setPlan(userPlan.getPlan());
        history.setStartDate(userPlan.getStartDate());
        history.setEndDate(userPlan.getEndDate());
        history.setStatus(UserPlanStatus.expired);
        history.setReason("Plan vencido automáticamente por el sistema.");
        planHistoryRepository.save(history);
    }
}
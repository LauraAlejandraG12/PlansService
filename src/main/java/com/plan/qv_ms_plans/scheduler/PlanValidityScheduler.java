package com.plan.qv_ms_plans.scheduler;


import com.plan.qv_ms_plans.model.entity.PlanHistory;
import com.plan.qv_ms_plans.model.entity.UserPlan;
import com.plan.qv_ms_plans.model.enums.AuditAction;
import com.plan.qv_ms_plans.model.enums.UserPlanStatus;
import com.plan.qv_ms_plans.repository.PlanHistoryRepository;
import com.plan.qv_ms_plans.repository.UserPlanRepository;
import com.plan.qv_ms_plans.service.PlanAuditService;
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
 * <p>Ejecuta tareas programadas para identificar planes vencidos,
 * actualizar su estado automáticamente y registrar los cambios
 * en el historial y en la bitácora de auditoría.</p>
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

    /**
     * Verifica y actualiza el estado de los planes vencidos (HU44).
     *
     * <p>Se ejecuta automáticamente todos los días a medianoche.
     * Busca todos los planes activos cuya fecha de vencimiento
     * ya pasó y los marca como {@code expired}.</p>
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

                expiredCount++;
            }
        }

        log.info("Verificación completada. Planes vencidos actualizados: {}", expiredCount);
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

package com.plan.qv_ms_plans.controller.plans;

import com.plan.qv_ms_plans.model.dto.plans.MessageResponseDTO;
import com.plan.qv_ms_plans.model.dto.plans.PlanHistoryResponseDTO;
import com.plan.qv_ms_plans.service.plans.PlanHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador REST para la consulta del historial de planes (HU46).
 *
 * <p>Expone los endpoints para consultar el historial completo
 * de planes asignados a un organizador.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Slf4j
@RestController
@RequestMapping("/api/plan-history")
@RequiredArgsConstructor
public class PlanHistoryController {
    private final PlanHistoryService planHistoryService;

    /**
     * Retorna el historial completo de planes de un organizador (HU46).
     *
     * @param userId ID del organizador
     * @return historial de planes con HTTP 200
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<MessageResponseDTO<List<PlanHistoryResponseDTO>>>  getHistoryByUser(@PathVariable Long userId) {
        log.info("Consultando historial de planes del organizador ID: {}", userId);
        List<PlanHistoryResponseDTO> planHistoryResponseDTOList = planHistoryService.getHistoryByUser(userId);
        return ResponseEntity.ok(MessageResponseDTO.success("Historial de planes obtenido exitosamente.", planHistoryResponseDTOList));
    }

    /**
     * Retorna el historial de una asignación específica (HU46).
     *
     * @param userPlanId ID de la asignación
     * @return historial de la asignación con HTTP 200
     */
    @GetMapping("/user-plan/{userPlanId}")
    public ResponseEntity<MessageResponseDTO<List<PlanHistoryResponseDTO>>> getHistoryByUserPlan(@PathVariable Long userPlanId) {
        log.info("Consultando historial de la asignación ID: {}", userPlanId);
        List<PlanHistoryResponseDTO> planHistoryResponseDTOList = planHistoryService.getHistoryByUserPlan(userPlanId);
        return ResponseEntity.ok(MessageResponseDTO.success("Historial de la asignación obtenido exitosamente.", planHistoryResponseDTOList));
    }
}

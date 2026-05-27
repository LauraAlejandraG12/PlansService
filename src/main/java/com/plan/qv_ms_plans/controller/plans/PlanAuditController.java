package com.plan.qv_ms_plans.controller.plans;

import com.plan.qv_ms_plans.model.dto.plans.MessageResponseDTO;
import com.plan.qv_ms_plans.model.dto.plans.PlanAuditResponseDTO;
import com.plan.qv_ms_plans.model.enums.AuditAction;
import com.plan.qv_ms_plans.service.plans.PlanAuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para la consulta de la bitácora de auditoría (HU48).
 *
 * <p>Expone los endpoints para consultar los registros de auditoría
 * de acciones realizadas sobre los planes del sistema.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */

@Slf4j
@RestController
@RequestMapping("api/plan-audit")
@RequiredArgsConstructor
public class PlanAuditController {
    private final PlanAuditService planAuditService;

    /**
     * Retorna todos los registros de auditoría del sistema (HU48).
     * Requiere rol ADMIN.
     *
     * @return lista de todos los registros de auditoría con HTTP 200
     */
    @GetMapping
    public ResponseEntity<MessageResponseDTO<List<PlanAuditResponseDTO>>> getAllAudits() {
        log.info("Consultando todos los registros de auditoría");
        List<PlanAuditResponseDTO> audits = planAuditService.getAllAudits();
        return ResponseEntity.ok(MessageResponseDTO.success(
                "Registros de auditoría obtenidos exitosamente.", audits));
    }


    /**
     * Retorna todos los registros de auditoría de un plan específico (HU48).
     *
     * @param planId ID del plan
     * @return lista de registros de auditoría con HTTP 200
     */
    @GetMapping("/plan/{planId}")
    public ResponseEntity<MessageResponseDTO<List<PlanAuditResponseDTO>>> getAuditByPlan(
            @PathVariable Long planId) {
        log.info("Consultando auditoría del plan ID: {}", planId);
        List<PlanAuditResponseDTO> audit = planAuditService.getAuditByPlan(planId);
        return ResponseEntity.ok(MessageResponseDTO.success(
                "Auditoría del plan obtenida exitosamente.", audit));
    }

    /**
     * Retorna todos los registros de auditoría por tipo de acción (HU48).
     *
     * @param action tipo de acción a filtrar
     * @return lista de registros de auditoría con HTTP 200
     */
    @GetMapping("/action/{action}")
    public ResponseEntity<MessageResponseDTO<List<PlanAuditResponseDTO>>> getAuditByAction(
            @PathVariable AuditAction action) {
        log.info("Consultando auditoría por acción: {}", action);
        List<PlanAuditResponseDTO> audit = planAuditService.getAuditByAction(action);
        return ResponseEntity.ok(MessageResponseDTO.success(
                "Auditoría por acción obtenida exitosamente.", audit));
    }

    /**
     * Retorna los registros de auditoría dentro de un rango de fechas (HU48).
     *
     * @param startDate fecha inicial del rango
     * @param endDate fecha final del rango
     * @return lista de registros de auditoría con HTTP 200
     */
    @GetMapping("/date-range")
    public ResponseEntity<MessageResponseDTO<List<PlanAuditResponseDTO>>> getAuditByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("Consultando auditoría entre {} y {}", startDate, endDate);
        List<PlanAuditResponseDTO> audit = planAuditService.getAuditByDateRange(startDate, endDate);
        return ResponseEntity.ok(MessageResponseDTO.success(
                "Auditoría por rango de fechas obtenida exitosamente.", audit));
    }
}

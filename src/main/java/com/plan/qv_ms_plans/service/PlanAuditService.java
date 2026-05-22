package com.plan.qv_ms_plans.service;

import com.plan.qv_ms_plans.model.dto.PlanAuditResponseDTO;
import com.plan.qv_ms_plans.model.dto.PlanResponseDTO;
import com.plan.qv_ms_plans.model.entity.Plan;
import com.plan.qv_ms_plans.model.entity.PlanAudit;
import com.plan.qv_ms_plans.model.enums.AuditAction;
import com.plan.qv_ms_plans.repository.PlanAuditRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para la gestión de la bitácora de auditoría de planes.
 *
 * <p>Registra y consulta todas las acciones realizadas sobre los planes
 * del sistema, garantizando trazabilidad y control (HU47, HU48).</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Service
@RequiredArgsConstructor
public class PlanAuditService {
    private final PlanAuditRepository planAuditRepository;

    /**
     * Registra una acción en la bitácora de auditoría (HU47).
     *
     * @param plan plan sobre el que se realizó la acción
     * @param adminId ID del administrador que realizó la acción
     * @param action tipo de acción realizada
     * @param description descripción detallada de los cambios
     */

    @Transactional
    public void registerAudit(Plan plan, Integer adminId, AuditAction action, String description) {
        PlanAudit audit = new PlanAudit();
        audit.setPlan(plan);
        audit.setUserId(adminId);
        audit.setAction(action);
        audit.setChangeDescription(description);
        planAuditRepository.save(audit);
    }

    /**
     * Convierte una entidad {@link PlanAudit} a un {@link PlanAuditResponseDTO}.
     *
     * @param audit entidad a convertir
     * @return DTO listo para enviar al cliente
     */
    private PlanAuditResponseDTO toResponseDTO(PlanAudit audit) {
        PlanAuditResponseDTO dto = new PlanAuditResponseDTO();
        dto.setIdAudit(audit.getIdAudit());
        dto.setUserId(audit.getUserId());
        dto.setAction(audit.getAction());
        dto.setChangeDescription(audit.getChangeDescription());
        dto.setAuditDate(audit.getAuditDate());

        if(audit.getPlan() != null) {
            PlanResponseDTO planResponseDTO = new PlanResponseDTO();
            planResponseDTO.setIdPlan(audit.getPlan().getIdPlan());
            planResponseDTO.setName(audit.getPlan().getName());
            dto.setPlan(planResponseDTO);
        }

        return dto;
    }

    /**
     * Retorna todos los registros de auditoría de un plan específico (HU48).
     *
     * @param planId ID del plan
     * @return lista de registros de auditoría convertidos a DTO
     */

    public List<PlanAuditResponseDTO> getAuditByPlan(Integer planId) {
        return planAuditRepository.findByPlanIdPlan(planId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Retorna todos los registros de auditoría por tipo de acción (HU48).
     *
     * @param action tipo de acción a filtrar
     * @return lista de registros de auditoría convertidos a DTO
     */
    public  List<PlanAuditResponseDTO> getAuditByAction(AuditAction action) {
        return planAuditRepository.findByAction(action)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Retorna todos los registros de auditoría dentro de un rango de fechas (HU48).
     *
     * @param startDate fecha inicial del rango
     * @param endDate fecha final del rango
     * @return lista de registros de auditoría convertidos a DTO
     */

    public List<PlanAuditResponseDTO> getAuditByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return planAuditRepository.findByAuditDateBetween(startDate, endDate)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }
}

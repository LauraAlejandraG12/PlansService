package com.plan.qv_ms_plans.service.plans;

import com.plan.qv_ms_plans.model.dto.plans.PlanAuditResponseDTO;
import com.plan.qv_ms_plans.model.dto.plans.PlanResponseDTO;
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
    public void registerAudit(Plan plan, Long adminId, AuditAction action, String description) {
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
        PlanAuditResponseDTO planAuditResponseDTO = new PlanAuditResponseDTO();
        planAuditResponseDTO.setIdAudit(audit.getIdAudit());
        planAuditResponseDTO.setUserId(audit.getUserId());
        planAuditResponseDTO.setAction(audit.getAction());
        planAuditResponseDTO.setChangeDescription(audit.getChangeDescription());
        planAuditResponseDTO.setAuditDate(audit.getAuditDate());

        if(audit.getPlan() != null) {
            PlanResponseDTO planResponseDTO = new PlanResponseDTO();
            planResponseDTO.setIdPlan(audit.getPlan().getIdPlan());
            planResponseDTO.setName(audit.getPlan().getName());
            planResponseDTO.setDescription(audit.getPlan().getDescription());
            planResponseDTO.setPrice(audit.getPlan().getPrice());
            planResponseDTO.setDurationDays(audit.getPlan().getDurationDays());
            planResponseDTO.setMaxEvents(audit.getPlan().getMaxEvents());
            planResponseDTO.setMaxOrganizers(audit.getPlan().getMaxOrganizers());
            planResponseDTO.setMaxParticipants(audit.getPlan().getMaxParticipants());
            planResponseDTO.setMaxJudges(audit.getPlan().getMaxJudges());
            planResponseDTO.setMaxAttendees(audit.getPlan().getMaxAttendees());
            planResponseDTO.setMaxStaff(audit.getPlan().getMaxStaff());
            planResponseDTO.setStatus(audit.getPlan().getStatus());
            planResponseDTO.setCreatedAt(audit.getPlan().getCreatedAt());
            planResponseDTO.setUpdatedAt(audit.getPlan().getUpdatedAt());
            planAuditResponseDTO.setPlan(planResponseDTO);
        }

        return planAuditResponseDTO;
    }

    /**
     * Retorna todos los registros de auditoría del sistema (HU48).
     *
     * @return lista de todos los registros de auditoría convertidos a DTO
     */
    public List<PlanAuditResponseDTO> getAllAudits() {
        return planAuditRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Retorna todos los registros de auditoría de un plan específico (HU48).
     *
     * @param planId ID del plan
     * @return lista de registros de auditoría convertidos a DTO
     */

    public List<PlanAuditResponseDTO> getAuditByPlan(Long planId) {
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

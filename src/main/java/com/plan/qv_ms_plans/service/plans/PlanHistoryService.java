package com.plan.qv_ms_plans.service.plans;

import com.plan.qv_ms_plans.model.dto.plans.PlanHistoryResponseDTO;
import com.plan.qv_ms_plans.model.dto.plans.PlanResponseDTO;
import com.plan.qv_ms_plans.model.entity.PlanHistory;
import com.plan.qv_ms_plans.repository.PlanHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para la consulta del historial de planes asignados a organizadores.
 *
 * <p>Permite consultar el historial completo de planes por organizador
 * o por asignación específica (HU46).</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */

@Service
@RequiredArgsConstructor
public class PlanHistoryService {

    private final PlanHistoryRepository planHistoryRepository;

    /**
     * Convierte una entidad {@link PlanHistory} a un {@link PlanHistoryResponseDTO}.
     *
     * @param history entidad a convertir
     * @return DTO listo para enviar al cliente
     */
    private PlanHistoryResponseDTO toResponseDTO(PlanHistory history) {
        PlanHistoryResponseDTO planHistoryResponseDTO = new PlanHistoryResponseDTO();
        planHistoryResponseDTO.setIdHistory(history.getIdHistory());
        planHistoryResponseDTO.setStartDate(history.getStartDate());
        planHistoryResponseDTO.setEndDate(history.getEndDate());
        planHistoryResponseDTO.setStatus(history.getStatus());
        planHistoryResponseDTO.setReason(history.getReason());
        planHistoryResponseDTO.setCreatedAt(history.getCreatedAt());

        if (history.getPlan() != null) {
            PlanResponseDTO planResponseDTO = new PlanResponseDTO();
            planResponseDTO.setIdPlan(history.getPlan().getIdPlan());
            planResponseDTO.setName(history.getPlan().getName());
            planResponseDTO.setDescription(history.getPlan().getDescription());
            planResponseDTO.setPrice(history.getPlan().getPrice());
            planResponseDTO.setDurationDays(history.getPlan().getDurationDays());
            planResponseDTO.setMaxOrganizers(history.getPlan().getMaxOrganizers());
            planResponseDTO.setMaxParticipants(history.getPlan().getMaxParticipants());
            planResponseDTO.setMaxJudges(history.getPlan().getMaxJudges());
            planResponseDTO.setMaxAttendees(history.getPlan().getMaxAttendees());
            planResponseDTO.setMaxStaff(history.getPlan().getMaxStaff());
            planResponseDTO.setStatus(history.getPlan().getStatus());
            planResponseDTO.setCreatedAt(history.getPlan().getCreatedAt());
            planResponseDTO.setUpdatedAt(history.getPlan().getUpdatedAt());
            planHistoryResponseDTO.setPlan(planResponseDTO);
        }

        return planHistoryResponseDTO;
    }

    /**
     * Retorna el historial completo de planes de un organizador (HU46).
     *
     * <p>Muestra todos los planes que ha tenido el organizador
     * en orden cronológico.</p>
     *
     * @param userId ID del organizador
     * @return lista de registros de historial convertidos a DTO
     * @throws EntityNotFoundException si el organizador no tiene historial
     */
    public List<PlanHistoryResponseDTO> getHistoryByUser(Long userId) {
        List<PlanHistory> history = planHistoryRepository.findByUserPlanUserId(userId);
        if (history.isEmpty()) {
            throw new EntityNotFoundException(
                    "No se encontró historial de planes para el organizador con ID: " + userId);
        }
        return history.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Retorna el historial de una asignación específica (HU46).
     *
     * @param userPlanId ID de la asignación
     * @return lista de registros de historial de la asignación
     * @throws EntityNotFoundException si no hay historial para la asignación
     */
    public List<PlanHistoryResponseDTO> getHistoryByUserPlan(Long userPlanId) {
        List<PlanHistory> history = planHistoryRepository.findByUserPlanIdUserPlan(userPlanId);
        if (history.isEmpty()) {
            throw new EntityNotFoundException(
                    "No se encontró historial para la asignación con ID: " + userPlanId);
        }
        return history.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}

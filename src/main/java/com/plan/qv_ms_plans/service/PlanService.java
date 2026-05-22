package com.plan.qv_ms_plans.service;

import com.plan.qv_ms_plans.model.dto.PlanRequestDTO;
import com.plan.qv_ms_plans.model.dto.PlanResponseDTO;
import com.plan.qv_ms_plans.model.entity.Plan;
import com.plan.qv_ms_plans.model.enums.AuditAction;
import com.plan.qv_ms_plans.model.enums.PlanStatus;
import com.plan.qv_ms_plans.model.specification.PlanSpecification;
import com.plan.qv_ms_plans.repository.PlanAuditRepository;
import com.plan.qv_ms_plans.repository.PlanRepository;
import com.plan.qv_ms_plans.repository.UserPlanRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio para la gestión de planes de servicio del sistema.
 *
 * <p>Contiene la lógica de negocio para crear, consultar, editar
 * y eliminar planes, así como el registro de auditoría de cada acción.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */

@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final UserPlanRepository userPlanRepository;
    private final PlanAuditService planAuditService;

    /**
     * Convierte una entidad {@link Plan} a un {@link PlanResponseDTO}.
     *
     * @param plan entidad a convertir
     * @return DTO listo para enviar al cliente
     */
    private PlanResponseDTO toResponseDTO(Plan plan) {
        PlanResponseDTO planResponseDTO = new PlanResponseDTO();
        planResponseDTO.setIdPlan(plan.getIdPlan());
        planResponseDTO.setName(plan.getName());
        planResponseDTO.setDescription(plan.getDescription());
        planResponseDTO.setPrice(plan.getPrice());
        planResponseDTO.setDurationDays(plan.getDurationDays());
        planResponseDTO.setMaxOrganizers(plan.getMaxOrganizers());
        planResponseDTO.setMaxParticipants(plan.getMaxParticipants());
        planResponseDTO.setMaxJudges(plan.getMaxJudges());
        planResponseDTO.setMaxAttendees(plan.getMaxAttendees());
        planResponseDTO.setMaxStaff(plan.getMaxStaff());
        planResponseDTO.setStatus(plan.getStatus());
        planResponseDTO.setCreatedAt(plan.getCreatedAt());
        planResponseDTO.setUpdatedAt(plan.getUpdatedAt());

        return planResponseDTO;
    }

    /**
     * Crea un nuevo plan de servicio en el sistema (HU36).
     *
     * <p>Valida que no exista otro plan con el mismo nombre antes de registrarlo.
     * Registra la acción en la bitácora de auditoría.</p>
     *
     * @param planRequestDTO datos del plan a crear
     * @param adminId ID del administrador que realiza la acción
     * @return DTO con los datos del plan creado
     * @throws IllegalArgumentException si ya existe un plan con el mismo nombre
     */

    @Transactional
    public PlanResponseDTO createPlan(PlanRequestDTO planRequestDTO, Integer adminId) {
        if(planRepository.existsByName(planRequestDTO.getName())) {
            throw new IllegalArgumentException("Ya existe un plan con el nombre: " + planRequestDTO.getName());
        }

        Plan plan = new Plan();
        plan.setName(planRequestDTO.getName());
        plan.setDescription(planRequestDTO.getDescription());
        plan.setPrice(planRequestDTO.getPrice());
        plan.setDurationDays(planRequestDTO.getDurationDays());
        plan.setMaxOrganizers(planRequestDTO.getMaxOrganizers());
        plan.setMaxParticipants(planRequestDTO.getMaxParticipants());
        plan.setMaxJudges(planRequestDTO.getMaxJudges());
        plan.setMaxAttendees(planRequestDTO.getMaxAttendees());
        plan.setMaxStaff(planRequestDTO.getMaxStaff());
        plan.setStatus(planRequestDTO.getStatus());

        Plan savedPlan = planRepository.save(plan);
        planAuditService.registerAudit(savedPlan, adminId, AuditAction.create,
                "Se creó el plan: " + savedPlan.getName());

        return toResponseDTO(savedPlan);
    }

    /**
     * Retorna la lista completa de planes registrados en el sistema (HU37).
     *
     * @return lista de todos los planes convertidos a DTO
     */
    public List<PlanResponseDTO> getAllPlans() {
        return planRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Busca y filtra planes combinando múltiples criterios simultáneamente (HU38).
     *
     * <p>Cada criterio es opcional y se pueden combinar entre sí.</p>
     *
     * @param name texto a buscar en el nombre del plan
     * @param status estado del plan a filtrar
     * @param minPrice precio mínimo del rango
     * @param maxPrice precio máximo del rango
     * @return lista de planes que coinciden con los criterios combinados
     */

    public List<PlanResponseDTO> filterPlans(String name, PlanStatus status, BigDecimal minPrice, BigDecimal maxPrice) {
        return planRepository.findAll(PlanSpecification.filterBy(name, status, minPrice, maxPrice))
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Busca un plan por ID y lanza excepción si no existe.
     * Método interno reutilizable por otros servicios
     *
     * @param id ID del plan
     * @return entidad Plan encontrada
     * @throws EntityNotFoundException si el plan no existe
     */
    public Plan findPlanById(Integer id) {
        return planRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Plan no encontrado con ID: " + id));
    }

    /**
     * Retorna el detalle completo de un plan específico (HU39).
     *
     * @param id ID del plan a consultar
     * @return DTO con los datos del plan
     * @throws EntityNotFoundException si el plan no existe
     */
    public PlanResponseDTO getPlanById(Integer id) {
        Plan plan = findPlanById(id);
        return toResponseDTO(plan);
    }

    /**
     * Actualiza los datos de un plan existente (HU40).
     *
     * <p>Valida que el nuevo nombre no esté en uso por otro plan.
     * Registra la acción en la bitácora de auditoría.</p>
     *
     * @param id ID del plan a actualizar
     * @param planRequestDTO datos nuevos del plan
     * @param adminId ID del administrador que realiza la acción
     * @return DTO con los datos del plan actualizado
     * @throws EntityNotFoundException si el plan no existe
     * @throws IllegalArgumentException si el nuevo nombre ya está en uso
     */

    @Transactional
    public PlanResponseDTO updatePlan(Integer id, PlanRequestDTO planRequestDTO, Integer adminId) {
        Plan existingPlan = findPlanById(id);

        if(!existingPlan.getName().equals(planRequestDTO.getName()) && planRepository.existsByName(planRequestDTO.getName())) {
            throw new IllegalArgumentException("Ya existe un plan con el nombre: " + planRequestDTO.getName());
        }

        existingPlan.setName(planRequestDTO.getName());
        existingPlan.setDescription(planRequestDTO.getDescription());
        existingPlan.setPrice(planRequestDTO.getPrice());
        existingPlan.setDurationDays(planRequestDTO.getDurationDays());
        existingPlan.setMaxOrganizers(planRequestDTO.getMaxOrganizers());
        existingPlan.setMaxParticipants(planRequestDTO.getMaxParticipants());
        existingPlan.setMaxJudges(planRequestDTO.getMaxJudges());
        existingPlan.setMaxAttendees(planRequestDTO.getMaxAttendees());
        existingPlan.setMaxStaff(planRequestDTO.getMaxStaff());
        existingPlan.setStatus(planRequestDTO.getStatus());

        Plan savedPlan = planRepository.save(existingPlan);
        planAuditService.registerAudit(savedPlan, adminId, AuditAction.update, "Se actualizó el plan: " + savedPlan.getName());

        return toResponseDTO(savedPlan);
    }

    /**
     * Elimina un plan del sistema si no está asignado a ningún organizador (HU41).
     *
     * <p>Registra la acción en la bitácora de auditoría antes de eliminar.</p>
     *
     * @param id ID del plan a eliminar
     * @param adminId ID del administrador que realiza la acción
     * @throws EntityNotFoundException si el plan no existe
     * @throws IllegalStateException si el plan está asignado a algún organizador
     */
    @Transactional
    public void deletePlan(Integer id, Integer adminId) {
        Plan plan = findPlanById(id);

        if (userPlanRepository.existsByPlanIdPlan(id)) {
            throw new IllegalStateException("No se puede elimianr el plan porque esta asignado a uno o más organizadores.");
        }

        planAuditService.registerAudit(plan, adminId, AuditAction.delete, "Se eliminó el plan: " + plan.getName());
        planRepository.deleteById(id);
    }
}

package com.plan.qv_ms_plans.service.plans;


import com.plan.qv_ms_plans.model.dto.plans.PlanResponseDTO;
import com.plan.qv_ms_plans.model.dto.plans.UserInfoDTO;
import com.plan.qv_ms_plans.model.dto.plans.UserPlanRequestDTO;
import com.plan.qv_ms_plans.model.dto.plans.UserPlanResponseDTO;
import com.plan.qv_ms_plans.model.entity.Plan;
import com.plan.qv_ms_plans.model.entity.PlanHistory;
import com.plan.qv_ms_plans.model.entity.UserPlan;
import com.plan.qv_ms_plans.model.enums.AuditAction;
import com.plan.qv_ms_plans.model.enums.NotificationType;
import com.plan.qv_ms_plans.model.enums.UserPlanStatus;
import com.plan.qv_ms_plans.repository.PlanHistoryRepository;
import com.plan.qv_ms_plans.repository.UserPlanRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio para la gestión de planes asignados a organizadores.
 *
 * <p>Contiene la lógica de negocio para asignar y renovar planes,
 * así como el registro de historial y auditoría de cada acción (HU42, HU43).</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */

@Service
@RequiredArgsConstructor
public class UserPlanService {
    private final UserPlanRepository userPlanRepository;
    private final PlanHistoryRepository planHistoryRepository;
    private final PlanService planService;
    private final PlanAuditService planAuditService;
    private final NotificationService notificationService;
    private final UserClientService userClientService;

    /**
     * Construye una entidad {@link UserPlan} con los datos proporcionados.
     * Método interno reutilizable para asignación y adquisición.
     *
     * @param userId ID del organizador
     * @param plan plan a asignar
     * @param startDate fecha de inicio de la asignación
     * @return entidad UserPlan lista para persistir
     */
    private UserPlan buildUserPlan(Long userId, Plan plan, LocalDate startDate) {
        UserPlan userPlan = new UserPlan();
        userPlan.setUserId(userId);
        userPlan.setPlan(plan);
        userPlan.setStartDate(startDate);
        userPlan.setEndDate(startDate.plusDays(plan.getDurationDays()));
        userPlan.setStatus(UserPlanStatus.active);
        userPlan.setRenewal(false);

        return userPlan;
    }

    /**
     * Guarda un registro en el historial de planes.
     *
     * @param userPlan asignación de plan a registrar
     * @param reason motivo del registro
     */
    private void saveHistory(UserPlan userPlan, String reason) {
        PlanHistory history = new PlanHistory();
        history.setUserPlan(userPlan);
        history.setPlan(userPlan.getPlan());
        history.setStartDate(userPlan.getStartDate());
        history.setEndDate(userPlan.getEndDate());
        history.setStatus(userPlan.getStatus());
        history.setReason(reason);
        planHistoryRepository.save(history);
    }

    /**
     * Convierte una entidad {@link UserPlan} a un {@link UserPlanResponseDTO}.
     *
     * @param userPlan entidad a convertir
     * @return DTO listo para enviar al cliente
     */
    private UserPlanResponseDTO toResponseDTO(UserPlan userPlan) {
        UserPlanResponseDTO userPlanResponseDTO = new UserPlanResponseDTO();
        userPlanResponseDTO.setIdUserPlan(userPlan.getIdUserPlan());
        userPlanResponseDTO.setUserId(userPlan.getUserId());
        userPlanResponseDTO.setStartDate(userPlan.getStartDate());
        userPlanResponseDTO.setEndDate(userPlan.getEndDate());
        userPlanResponseDTO.setStatus(userPlan.getStatus());
        userPlanResponseDTO.setRenewal(userPlan.getRenewal());
        userPlanResponseDTO.setCreatedAt(userPlan.getCreatedAt());

        PlanResponseDTO planResponseDTO = new PlanResponseDTO();

        planResponseDTO.setIdPlan(userPlan.getPlan().getIdPlan());
        planResponseDTO.setName(userPlan.getPlan().getName());
        planResponseDTO.setDescription(userPlan.getPlan().getDescription());
        planResponseDTO.setPrice(userPlan.getPlan().getPrice());
        planResponseDTO.setDurationDays(userPlan.getPlan().getDurationDays());
        planResponseDTO.setMaxEvents(userPlan.getPlan().getMaxEvents());
        planResponseDTO.setMaxOrganizers(userPlan.getPlan().getMaxOrganizers());
        planResponseDTO.setMaxGuests(userPlan.getPlan().getMaxGuests());
        planResponseDTO.setMaxStaff(userPlan.getPlan().getMaxStaff());
        planResponseDTO.setMaxStaff(userPlan.getPlan().getMaxStaff());
        planResponseDTO.setStatus(userPlan.getPlan().getStatus());
        planResponseDTO.setCreatedAt(userPlan.getPlan().getCreatedAt());
        planResponseDTO.setUpdatedAt(userPlan.getPlan().getUpdatedAt());
        userPlanResponseDTO.setPlan(planResponseDTO);

        return userPlanResponseDTO;
    }

    /**
     * Asigna un plan a un organizador específico (HU42).
     *
     * <p>Solo el administrador puede usar este método para asignar
     * un plan a cualquier organizador. Valida que el plan esté activo,
     * calcula la fecha de vencimiento automáticamente y registra
     * la asignación en el historial y en la auditoría.</p>
     *
     * @param userPlanRequestDTO datos de la asignación incluyendo el ID del organizador
     * @param adminId ID del administrador que realiza la acción
     * @return DTO con los datos de la asignación creada
     * @throws IllegalStateException si el plan no está activo
     */
    @Transactional
    public UserPlanResponseDTO assignPlanByAdmin(UserPlanRequestDTO userPlanRequestDTO, Long adminId) {
        Plan plan = planService.findPlanById((userPlanRequestDTO.getPlanId()));

        if (!plan.getStatus().name().equals("active")) {
            throw new IllegalStateException("No se puede asignar un plan inactivo.");
        }

        UserPlan userPlan = buildUserPlan(userPlanRequestDTO.getUserId(), plan, userPlanRequestDTO.getStartDate());
        UserPlan savedUserPlan = userPlanRepository.save(userPlan);

        saveHistory(savedUserPlan, "Asignación inicial del plan por el administrador.");

        planAuditService.registerAudit(plan, adminId, AuditAction.assign, "El administrador ID: " + adminId + " asignó el plan: " + plan.getName() + " al organizador ID: " + userPlanRequestDTO.getUserId());

        return toResponseDTO(savedUserPlan);
    }

    /**
     * Permite a un organizador adquirir su propio plan por primera vez (HU42).
     *
     * <p>El organizador se asigna a sí mismo un plan disponible.
     * Valida que el plan esté activo, calcula la fecha de vencimiento
     * automáticamente y registra la asignación en el historial y en la auditoría.</p>
     *
     * @param userPlanRequestDTO datos de la asignación con la fecha de inicio
     * @param organizerId ID del organizador que adquiere el plan
     * @return DTO con los datos de la asignación creada
     * @throws IllegalStateException si el plan no está activo
     */

    @Transactional
    public UserPlanResponseDTO acquirePlanByOrganizer(UserPlanRequestDTO userPlanRequestDTO, Long organizerId) {
        Plan plan = planService.findPlanById(userPlanRequestDTO.getPlanId());

        if (!plan.getStatus().name().equals("active")) {
            throw new IllegalStateException("No se puede adquirir un plan inactivo.");
        }

        UserInfoDTO userInfo = userClientService.getUserById(organizerId);
        String userEmail = userInfo != null ? userInfo.getEmail() : null;
        String userName = userInfo != null ? userInfo.getFullName() : null;

        UserPlan userPlan = buildUserPlan(organizerId, plan, userPlanRequestDTO.getStartDate());
        UserPlan savedUserPlan = userPlanRepository.save(userPlan);

        saveHistory(savedUserPlan, "Adquisición inicial del plan por el organizador.");
        planAuditService.registerAudit(plan, organizerId, AuditAction.assign,
                "El organizador ID: " + organizerId + " adquirió el plan: " + plan.getName());

        notificationService.createNotification(
                organizerId, userEmail, userName,
                NotificationType.PLAN_PURCHASED,
                "Compra exitosa",
                "Tu plan '" + plan.getName() + "' fue adquirido exitosamente. " +
                        "Estará activo hasta el " + savedUserPlan.getEndDate() + "."
        );

        return toResponseDTO(savedUserPlan);
    }

    /**
     * Busca una asignación por ID y lanza excepción si no existe.
     * Método interno reutilizable.
     *
     * @param id ID de la asignación
     * @return entidad UserPlan encontrada
     * @throws EntityNotFoundException si la asignación no existe
     */
    public UserPlan findUserPlanById(Long id) {
        return userPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Asignación de plan no encontrada con ID: " + id));
    }

    /**
     * Renueva el plan asignado a un organizador (HU43).
     *
     * <p>Puede ser ejecutado por el administrador o por el propio organizador.
     * Calcula la nueva fecha de vencimiento desde la fecha actual,
     * conserva el historial de renovaciones y registra la acción en auditoría.</p>
     *
     * @param userPlanId ID de la asignación a renovar
     * @param executorId ID del usuario que realiza la renovación (admin u organizador)
     * @return DTO con los datos de la asignación renovada
     * @throws EntityNotFoundException si la asignación no existe
     */
    @Transactional
    public UserPlanResponseDTO renewPlan(Long userPlanId, Long executorId) {
        UserPlan userPlan = findUserPlanById(userPlanId);

        UserInfoDTO userInfo = userClientService.getUserById(userPlan.getUserId());
        String userEmail = userInfo != null ? userInfo.getEmail() : null;
        String userName = userInfo != null ? userInfo.getFullName() : null;

        userPlan.setStartDate(LocalDate.now());
        userPlan.setEndDate(LocalDate.now().plusDays(userPlan.getPlan().getDurationDays()));
        userPlan.setStatus(UserPlanStatus.active);
        userPlan.setRenewal(true);

        UserPlan savedUserPlan = userPlanRepository.save(userPlan);

        saveHistory(savedUserPlan, "Renovación del plan.");
        planAuditService.registerAudit(userPlan.getPlan(), executorId, AuditAction.renew,
                "Se renovó el plan: " + userPlan.getPlan().getName() +
                        " del organizador ID: " + userPlan.getUserId());

        notificationService.createNotification(
                userPlan.getUserId(), userEmail, userName,
                NotificationType.PLAN_RENEWED,
                "Renovación exitosa",
                "Tu plan '" + userPlan.getPlan().getName() + "' fue renovado exitosamente. " +
                        "Estará activo hasta el " + savedUserPlan.getEndDate() + "."
        );

        return toResponseDTO(savedUserPlan);
    }

    /**
     * Cambia el plan activo de un organizador por uno nuevo (HU43).
     *
     * <p>Cancela el plan activo actual, registra el cambio en el historial
     * y asigna el nuevo plan al organizador.</p>
     *
     * @param userId ID del organizador
     * @param newPlanId ID del nuevo plan a asignar
     * @param startDate fecha de inicio del nuevo plan
     * @param reason motivo del cambio de plan
     * @param executorId ID del usuario que realiza el cambio
     * @return DTO con los datos del nuevo plan asignado
     * @throws EntityNotFoundException si no tiene plan activo
     * @throws IllegalStateException si el nuevo plan no está activo
     */
    @Transactional
    public UserPlanResponseDTO changePlan(Long userId, Long newPlanId, LocalDate startDate, String reason, Long executorId) {

        UserPlan currentPlan = userPlanRepository
                .findByUserIdAndStatus(userId, UserPlanStatus.active)
                .orElseThrow(() -> new EntityNotFoundException(
                        "El organizador con ID: " + userId + " no tiene un plan activo."));

        String oldPlanName = currentPlan.getPlan().getName();
        currentPlan.setStatus(UserPlanStatus.cancelled);
        userPlanRepository.save(currentPlan);
        saveHistory(currentPlan, "Plan cancelado por cambio de plan. Motivo: " + reason);

        Plan newPlan = planService.findPlanById(newPlanId);
        if (!newPlan.getStatus().name().equals("active")) {
            throw new IllegalStateException("No se puede asignar un plan inactivo.");
        }

        UserPlan newUserPlan = buildUserPlan(userId, newPlan, startDate);
        UserPlan savedUserPlan = userPlanRepository.save(newUserPlan);
        saveHistory(savedUserPlan, "Nuevo plan asignado por cambio de plan. Motivo: " + reason);

        planAuditService.registerAudit(newPlan, executorId, AuditAction.assign,
                "Cambio de plan: '" + oldPlanName + "' → '" + newPlan.getName() +
                        "' para el organizador ID: " + userId + ". Motivo: " + reason);

        UserInfoDTO userInfo = userClientService.getUserById(userId);
        String userEmail = userInfo != null ? userInfo.getEmail() : null;
        String userName = userInfo != null ? userInfo.getFullName() : null;

        notificationService.createNotification(
                userId, userEmail, userName,
                NotificationType.PLAN_CHANGED,
                "Cambio de plan exitoso",
                "Cambiaste del plan '" + oldPlanName + "' al plan '" + newPlan.getName() + "'. " +
                        "Tu nuevo plan estará activo hasta el " + savedUserPlan.getEndDate() + "."
        );

        return toResponseDTO(savedUserPlan);
    }

    /**
     * Retorna todos los organizadores que tienen asignado un plan activo (RF25.2).
     *
     * @param planId ID del plan
     * @return lista de asignaciones activas del plan
     */
    public List<UserPlanResponseDTO> getOrganizersByPlan(Long planId) {
        return userPlanRepository
                .findByPlanIdPlanAndStatus(planId, UserPlanStatus.active)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Retorna el plan activo de un organizador específico.
     *
     * @param userId ID del organizador
     * @return DTO con los datos del plan activo
     * @throws EntityNotFoundException si el organizador no tiene un plan activo
     */
    public UserPlanResponseDTO getActivePlanByUser(Long userId) {
        UserPlan userPlan = userPlanRepository
                .findByUserIdAndStatus(userId, UserPlanStatus.active)
                .orElseThrow(() -> new EntityNotFoundException(
                        "El organizador con ID: " + userId + " no tiene un plan activo."));
        return toResponseDTO(userPlan);
    }
}

package com.plan.qv_ms_plans.controller;

import com.plan.qv_ms_plans.model.dto.MessageResponseDTO;
import com.plan.qv_ms_plans.model.dto.UserPlanRequestDTO;
import com.plan.qv_ms_plans.model.dto.UserPlanResponseDTO;
import com.plan.qv_ms_plans.service.UserPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de planes asignados a organizadores (HU42, HU43).
 *
 * <p>Expone los endpoints para asignar, adquirir y renovar planes,
 * así como consultar los planes activos de un organizador.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Slf4j
@RestController
@RequestMapping("/api/user-plans")
@RequiredArgsConstructor
public class UserPlanController {
    private final UserPlanService userPlanService;

    /**
     * Asigna un plan a un organizador específico (HU42).
     * Solo el administrador puede usar este endpoint.
     *
     * @param adminId ID del administrador obtenido del header
     * @param userPlanRequestDTO datos de la asignación
     * @return asignación creada con HTTP 201
     */
    @PostMapping("/assign")
    public ResponseEntity<MessageResponseDTO<UserPlanResponseDTO>> assignPlanByAdmin(@RequestHeader("X-User-Id") Integer adminId, @Valid @RequestBody UserPlanRequestDTO userPlanRequestDTO) {
        log.info("Administrador ID: {} asignando plan ID: {} al organizador ID: {}", adminId, userPlanRequestDTO.getPlanId(), userPlanRequestDTO.getUserId());
        UserPlanResponseDTO userPlanResponseDTO = userPlanService.assignPlanByAdmin(userPlanRequestDTO, adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(MessageResponseDTO.success("Plan asignado exitosamente al organizador.", userPlanResponseDTO));
    }

    /**
     * Permite a un organizador adquirir su propio plan (HU42).
     *
     * @param organizerId ID del organizador obtenido del header
     * @param userPlanRequestDTO datos de la adquisición
     * @return asignación creada con HTTP 201
     */
    @PostMapping("/acquire")
    public ResponseEntity<MessageResponseDTO<UserPlanResponseDTO>> acquirePlanByOrganizer(@RequestHeader("X-User-Id") Integer organizerId, @Valid @RequestBody UserPlanRequestDTO userPlanRequestDTO) {
        log.info("Organizador ID: {} adquiriendo plan ID: {}", organizerId, userPlanRequestDTO.getPlanId());
        UserPlanResponseDTO userPlanResponseDTO = userPlanService.acquirePlanByOrganizer(userPlanRequestDTO, organizerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(MessageResponseDTO.success("Plan adquirido exitosamente.", userPlanResponseDTO));
    }

    /**
     * Renueva el plan asignado a un organizador (HU43).
     * Puede ser ejecutado por el administrador o por el propio organizador.
     *
     * @param userPlanId ID de la asignación a renovar
     * @param executorId ID del usuario que realiza la renovación
     * @return asignación renovada con HTTP 200
     */
    @PutMapping("/{userPlanId}/renew")
    public ResponseEntity<MessageResponseDTO<UserPlanResponseDTO>> renewPlan(@PathVariable Integer userPlanId, @RequestHeader("X-User-Id") Integer executorId) {
        log.info("Renovando plan asignado ID: {} por usuario ID: {}", userPlanId, executorId);
        UserPlanResponseDTO userPlanResponseDTO = userPlanService.renewPlan(userPlanId, executorId);
        return ResponseEntity.ok(MessageResponseDTO.success("Plan renovado exitosamente.", userPlanResponseDTO));
    }

    /**
     * Retorna todos los planes asignados a un organizador (HU46).
     *
     * @param userId ID del organizador
     * @return lista de planes del organizador con HTTP 200
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<MessageResponseDTO<List<UserPlanResponseDTO>>> getPlansByUser(@PathVariable Integer userId) {
        log.info("Consultando planes del organizador ID: {}", userId);
        List<UserPlanResponseDTO> listPlans = userPlanService.getPlansByUser(userId);
        return ResponseEntity.ok(MessageResponseDTO.success("Planes del organizador obtenido exitosamente.", listPlans));
    }

    /**
     * Retorna el plan activo de un organizador específico.
     *
     * @param userId ID del organizador
     * @return plan activo del organizador con HTTP 200
     */
    @GetMapping("/user/{userId}/active")
    public ResponseEntity<MessageResponseDTO<UserPlanResponseDTO>> getActivePlanByUser(@PathVariable Integer userId) {
        log.info("Consultando plan activo del organizador ID: {}", userId);
        UserPlanResponseDTO userPlanResponseDTO = userPlanService.getActivePlanByUser(userId);
        return ResponseEntity.ok(MessageResponseDTO.success("Plan activo del organizador obtenido exitosamente.", userPlanResponseDTO));
    }
}

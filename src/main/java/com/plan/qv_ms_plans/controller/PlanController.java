package com.plan.qv_ms_plans.controller;

import com.plan.qv_ms_plans.model.dto.MessageResponseDTO;
import com.plan.qv_ms_plans.model.dto.PlanRequestDTO;
import com.plan.qv_ms_plans.model.dto.PlanResponseDTO;
import com.plan.qv_ms_plans.model.enums.PlanStatus;
import com.plan.qv_ms_plans.service.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador REST para la gestión de planes de servicio (HU36-HU41).
 *
 * <p>Expone los endpoints para crear, consultar, filtrar, editar
 * y eliminar planes de servicio del sistema.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */

@RestController
@Slf4j
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;

    /**
     * Crea un nuevo plan de servicio (HU36).
     *
     * @param adminId ID del administrador obtenido del header
     * @param planRequestDTO datos del plan a crear
     * @return plan creado con HTTP 201
     */
    @PostMapping
    public ResponseEntity<MessageResponseDTO<PlanResponseDTO>> createPlan(@RequestHeader("X-User-Id") Integer adminId, @Valid @RequestBody PlanRequestDTO planRequestDTO) {
        log.info("Creando plan: {}", planRequestDTO.getName());
        PlanResponseDTO planResponseDTO = planService.createPlan(planRequestDTO, adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(MessageResponseDTO.success("Plan creado exitosamente.", planResponseDTO));
    }

    /**
     * Retorna la lista completa de planes (HU37).
     *
     * @return lista de planes con HTTP 200
     */
    @GetMapping
    public ResponseEntity<MessageResponseDTO<List<PlanResponseDTO>>> getAllPLans() {
        log.info("Consultando lista de planes");
        List<PlanResponseDTO> listPlans = planService.getAllPlans();
        return ResponseEntity.ok(MessageResponseDTO.success("Lista de planes obtenida exitosamente.", listPlans));
    }

    /**
     * Filtra planes por nombre, estado y rango de precios (HU38).
     *
     * <p>Todos los parámetros son opcionales y se pueden combinar.</p>
     *
     * @param name texto a buscar en el nombre
     * @param status estado del plan
     * @param minPrice precio mínimo
     * @param maxPrice precio máximo
     * @return lista de planes filtrados con HTTP 200
     */
    @GetMapping("/filter")
    public ResponseEntity<MessageResponseDTO<List<PlanResponseDTO>>> filterPlans(@RequestParam(required = false) String name, @RequestParam(required = false)PlanStatus status, @RequestParam(required = false)BigDecimal minPrice, @RequestParam(required = false) BigDecimal maxPrice) {
        log.info("Filtrando planes - nombre: {}, estado: {}, precioMin: {}, precioMax: {}", name, status, minPrice, maxPrice);
        List<PlanResponseDTO> listPlans = planService.filterPlans(name, status, minPrice, maxPrice);
        return ResponseEntity.ok(MessageResponseDTO.success("Planes filtrados exitosamente. ", listPlans));
    }

    /**
     * Retorna el detalle completo de un plan específico (HU39).
     *
     * @param id ID del plan a consultar
     * @return plan encontrado con HTTP 200
     */
    @GetMapping("/{id]")
    public ResponseEntity<MessageResponseDTO<PlanResponseDTO>> getPlanById(@PathVariable Integer id) {
        log.info("Consultando plan con ID: {}", id);
        PlanResponseDTO planResponseDTO = planService.getPlanById(id);
        return ResponseEntity.ok(MessageResponseDTO.success("Plan obtenido exitosamente.", planResponseDTO));
    }

    /**
     * Actualiza los datos de un plan existente (HU40).
     *
     * @param id ID del plan a actualizar
     * @param adminId ID del administrador obtenido del header
     * @param planRequestDTO datos nuevos del plan
     * @return plan actualizado con HTTP 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponseDTO<PlanResponseDTO>> updatePlan(@PathVariable Integer id, @RequestHeader("X-User-Id") Integer adminId, @Valid @RequestBody PlanRequestDTO planRequestDTO) {
        log.info("Actualizando plan con ID: {}", id);
        PlanResponseDTO planResponseDTO = planService.updatePlan(id, planRequestDTO, adminId);
        return ResponseEntity.ok(MessageResponseDTO.success("Plan actualizado exitosamente.", planResponseDTO));
    }

    /**
     * Elimina un plan del sistema (HU41).
     *
     * @param id ID del plan a eliminar
     * @param adminId ID del administrador obtenido del header
     * @return HTTP 204 sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO<Void>> deletePlan(@PathVariable Integer id, @RequestHeader("X-User-Id") Integer adminId) {
        log.info("Eliminando plan con ID: {}", id);
        planService.deletePlan( id, adminId);
        return ResponseEntity.ok(MessageResponseDTO.success("Plan eliminado exitosamente."));
    }
}
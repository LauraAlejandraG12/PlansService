package com.plan.qv_ms_plans.repository;

import com.plan.qv_ms_plans.model.entity.PlanAudit;
import com.plan.qv_ms_plans.model.enums.AuditAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la gestión de la bitácora de auditoría de planes.
 *
 * <p>Extiende {@link JpaRepository} para heredar las operaciones CRUD básicas
 * y define consultas para consultar la auditoría por diferentes criterios.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Repository
public interface PlanAuditRepository extends JpaRepository<PlanAudit, Long> {

    /**
     * Busca todos los registros de auditoría de un plan específico.
     *
     * @param planId ID del plan
     * @return lista de registros de auditoría del plan
     */
    List<PlanAudit> findByPlanIdPlan(Long planId);

    /**
     * Busca todos los registros de auditoría realizados por un administrador específico.
     *
     * @param userId ID del administrador
     * @return lista de registros de auditoría del administrador
     */
    List<PlanAudit> findByUserId(Long userId);

    /**
     * Busca todos los registros de auditoría por tipo de acción.
     *
     * @param action tipo de acción a filtrar
     * @return lista de registros de auditoría con la acción indicada
     */
    List<PlanAudit> findByAction(AuditAction action);

    /**
     * Busca registros de auditoría dentro de un rango de fechas.
     * Usado para consultar la bitácora por período (RF32.1).
     *
     * @param startDate fecha inicial del rango
     * @param endDate fecha final del rango
     * @return lista de registros de auditoría dentro del rango
     */
    List<PlanAudit> findByAuditDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}

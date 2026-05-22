package com.plan.qv_ms_plans.repository;

import com.plan.qv_ms_plans.model.entity.PlanHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la gestión del historial de planes asignados a organizadores.
 *
 * <p>Extiende {@link JpaRepository} para heredar las operaciones CRUD básicas
 * y define consultas para consultar el historial de planes por organizador.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Repository
public interface PlanHistoryRepository extends JpaRepository<PlanHistory, Long> {

    /**
     * Busca todo el historial de planes de un organizador específico.
     * Usado para consultar el historial completo por organizador (RF31).
     *
     * @param userId ID del organizador
     * @return lista de registros de historial del organizador
     */
    List<PlanHistory> findByUserPlanUserId(Long userId);

    /**
     * Busca todo el historial asociado a una asignación específica.
     *
     * @param userPlanId ID de la asignación
     * @return lista de registros de historial de la asignación
     */
    List<PlanHistory> findByUserPlanIdUserPlan(Long userPlanId);

    /**
     * Busca todo el historial asociado a un plan específico.
     *
     * @param planId ID del plan
     * @return lista de registros de historial del plan
     */
    List<PlanHistory> findByPlanIdPlan(Long planId);
}

package com.plan.qv_ms_plans.repository;

import com.plan.qv_ms_plans.model.entity.UserPlan;
import com.plan.qv_ms_plans.model.enums.UserPlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de planes asignados a organizadores.
 *
 * <p>Extiende {@link JpaRepository} para heredar las operaciones CRUD básicas
 * y define consultas adicionales para la gestión de asignaciones de planes.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Repository
public interface UserPlanRepository extends JpaRepository<UserPlan, Integer> {

    /**
     * Busca todos los planes asignados a un organizador específico.
     *
     * @param userId ID del organizador
     * @return lista de planes asignados al organizador
     */
    List<UserPlan> findByUserId(Integer userId);

    /**
     * Busca el plan activo de un organizador específico.
     *
     * @param userId ID del organizador
     * @param status estado del plan a buscar
     * @return plan asignado con el estado indicado si existe
     */
    Optional<UserPlan> findByUserIdAndStatus(Integer userId, UserPlanStatus status);

    /**
     * Busca todos los planes asignados con un estado específico.
     *
     * @param status estado a filtrar
     * @return lista de asignaciones con el estado indicado
     */
    List<UserPlan> findByStatus(UserPlanStatus status);

    /**
     * Busca planes activos cuya fecha de vencimiento esté entre dos fechas.
     * Usado para detectar planes próximos a vencer (RF30.1).
     *
     * @param status estado del plan
     * @param startDate fecha inicial del rango
     * @param endDate fecha final del rango
     * @return lista de planes que vencen en el rango indicado
     */
    List<UserPlan> findByStatusAndEndDateBetween(UserPlanStatus status, LocalDate startDate, LocalDate endDate);

    /**
     * Verifica si un plan específico está asignado a algún organizador.
     * Usado para validar si un plan puede ser eliminado (RF27).
     *
     * @param planId ID del plan a verificar
     * @return {@code true} si el plan está asignado a algún organizador
     */
    boolean existsByPlanIdPlan(Integer planId);
}

package com.plan.qv_ms_plans.repository;

import com.plan.qv_ms_plans.model.entity.Plan;
import com.plan.qv_ms_plans.model.enums.PlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de planes en la base de datos.
 *
 * <p>Extiende {@link JpaRepository} para heredar las operaciones CRUD básicas
 * y {@link JpaSpecificationExecutor} para permitir filtros combinados dinámicos.
 * Todos los métodos filtran por {@code deleted = false} para implementar soft delete.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Repository
public interface PlanRepository extends JpaRepository<Plan, Long>, JpaSpecificationExecutor<Plan> {

    /**
     * Busca todos los planes no eliminados.
     *
     * @return lista de planes activos e inactivos no eliminados
     */
    List<Plan> findByDeletedFalse();

    /**
     * Busca un plan por ID que no esté eliminado.
     *
     * @param id ID del plan
     * @return plan encontrado si existe y no está eliminado
     */
    Optional<Plan> findByIdPlanAndDeletedFalse(Long id);

    /**
     * Verifica si existe algún plan con el nombre exacto indicado que no esté eliminado.
     *
     * @param name nombre del plan a verificar
     * @return {@code true} si ya existe un plan con ese nombre
     */
    boolean existsByNameAndDeletedFalse(String name);
}
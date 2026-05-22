package com.plan.qv_ms_plans.repository;

import com.plan.qv_ms_plans.model.entity.Plan;
import com.plan.qv_ms_plans.model.enums.PlanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repositorio para la gestión de planes en la base de datos.
 *
 * <p>Extiende {@link JpaRepository} para heredar las operaciones CRUD básicas
 * y {@link JpaSpecificationExecutor} para permitir filtros combinados dinámicos.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long>, JpaSpecificationExecutor<Plan> {
    /**
     * Busca planes cuyo nombre contenga el texto indicado, sin distinguir mayúsculas.
     *
     * @param name texto a buscar en el nombre del plan
     * @return lista de planes que coinciden con el criterio
     */
    List<Plan> findByNameContainingIgnoreCase(String name);

    /**
     * Busca planes por estado.
     *
     * @param status estado del plan a filtrar
     * @return lista de planes con el estado indicado
     */
    List<Plan> findByStatus(PlanStatus status);

    /**
     * Busca planes cuyo precio esté dentro de un rango determinado.
     *
     * @param minPrice precio mínimo del rango
     * @param maxPrice precio máximo del rango
     * @return lista de planes dentro del rango de precios
     */
    List<Plan> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Verifica si existe algún plan con el nombre exacto indicado.
     *
     * @param name nombre del plan a verificar
     * @return {@code true} si ya existe un plan con ese nombre
     */
    boolean existsByName(String name);
}

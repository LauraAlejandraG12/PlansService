package com.plan.qv_ms_plans.model.specification;

import com.plan.qv_ms_plans.model.entity.Plan;
import com.plan.qv_ms_plans.model.enums.PlanStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de especificaciones para el filtrado dinámico y combinado de planes.
 *
 * <p>Permite combinar múltiples criterios de búsqueda en una sola consulta,
 * como nombre, estado y rango de precios simultáneamente (HU38).
 * Siempre filtra por {@code deleted = false} para implementar soft delete.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
public class PlanSpecification {

    /**
     * Construye una {@link Specification} con los criterios de filtrado indicados.
     *
     * @param name texto a buscar en el nombre del plan
     * @param status estado del plan a filtrar
     * @param minPrice precio mínimo del rango
     * @param maxPrice precio máximo del rango
     * @return especificación con los filtros combinados
     */
    public static Specification<Plan> filterBy(String name, PlanStatus status,
                                               BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Siempre filtrar por deleted = false
            predicates.add(criteriaBuilder.equal(root.get("deleted"), false));

            if (name != null && !name.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"), maxPrice));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
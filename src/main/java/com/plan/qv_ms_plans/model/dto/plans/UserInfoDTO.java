package com.plan.qv_ms_plans.model.dto.plans;

import lombok.Data;

/**
 * DTO con la información básica de un usuario consultada al auth-service.
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Data
public class UserInfoDTO {
    /** ID del usuario */
    private Long id;
    /** Nombre completo */
    private String fullName;
    /** Correo electrónico */
    private String email;
}
package com.plan.qv_ms_plans.service.plans;

import com.plan.qv_ms_plans.model.dto.plans.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Cliente para consultar información de usuarios en el auth-service.
 *
 * <p>Usa RestTemplate para obtener el nombre y correo de un usuario por su ID,
 * necesarios para generar notificaciones y enviar correos. Llama directamente
 * al auth-service (sin pasar por el Gateway), ya que el scheduler no tiene
 * sesión de usuario.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserClientService {

    private final RestTemplate restTemplate;

    /** URL base directa del auth-service (sin Gateway). */
    @Value("${services.auth.url:http://localhost:8080}")
    private String authUrl;

    /**
     * Consulta la información de un usuario por su ID en el auth-service.
     *
     * @param userId ID del usuario a consultar
     * @return datos del usuario, o null si no se pudo obtener
     */
    @SuppressWarnings("unchecked")
    public UserInfoDTO getUserById(Long userId) {
        try {
            String url = authUrl + "/auth/users/" + userId;
            var response = restTemplate.getForObject(url, java.util.Map.class);

            if (response == null || response.get("data") == null) {
                return null;
            }

            var data = (java.util.Map<String, Object>) response.get("data");
            UserInfoDTO info = new UserInfoDTO();
            info.setId(userId);
            info.setFullName((String) data.get("fullName"));
            info.setEmail((String) data.get("email"));
            return info;

        } catch (Exception e) {
            log.error("Error al consultar usuario ID {}: {}", userId, e.getMessage());
            return null;
        }
    }
}
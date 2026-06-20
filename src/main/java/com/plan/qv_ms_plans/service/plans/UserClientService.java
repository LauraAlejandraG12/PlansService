package com.plan.qv_ms_plans.service.plans;

import com.plan.qv_ms_plans.model.dto.plans.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import java.util.Map;

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

    @Value("${services.auth.url}")
    private String authServiceUrl;

    @Value("${internal.api-key}")
    private String internalApiKey;

    @SuppressWarnings("unchecked")
    public UserInfoDTO getUserById(Long userId) {
        try {
            String url = authServiceUrl + "/auth/internal/users/" + userId;

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Internal-Api-Key", internalApiKey);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            var response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class).getBody();

            if (response == null || response.get("data") == null) {
                return null;
            }

            var data = (Map<String, Object>) response.get("data");
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
package com.plan.qv_ms_plans.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración de beans para comunicación entre microservicios.
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Expone un {@link RestTemplate} para consumir otros microservicios
     * a través del Gateway.
     *
     * @return instancia de RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
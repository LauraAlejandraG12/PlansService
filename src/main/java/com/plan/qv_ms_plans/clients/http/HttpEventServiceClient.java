package com.plan.qv_ms_plans.clients.http;

import com.plan.qv_ms_plans.clients.EventServiceClient;
import com.plan.qv_ms_plans.model.dto.dashboard.EventByOrganizerDTO;
import com.plan.qv_ms_plans.model.dto.dashboard.EventUserDetailDTO;
import com.plan.qv_ms_plans.model.dto.dashboard.GeneralStatsDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HttpEventServiceClient implements EventServiceClient {

     private final WebClient.Builder webClientBuilder;

    @Value("${services.events.url}")
    private String eventsUrl;

    @Override
    public Long getTotalEvents() {
        return webClientBuilder.build()
            .get()
            .uri(eventsUrl + "/api/events/total")
            .retrieve()
            .bodyToMono(Long.class)
            .block();
    }

    @Override
    public List<EventByOrganizerDTO> getEventsByOrganizer(String startDate, String endDate) {
        String uri = eventsUrl + "/api/events/by-organizer";

        if (startDate != null && endDate != null) {
        uri += "?startDate=" + startDate + "&endDate=" + endDate;
        }

        return webClientBuilder.build()
            .get()
            .uri(uri)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<EventByOrganizerDTO>>() {})
            .block();
    }

    @Override
    public List<EventUserDetailDTO> getUsersByEvent() {
        return webClientBuilder.build()
            .get()
            .uri(eventsUrl + "/api/events/users-by-role")
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<EventUserDetailDTO>>() {})
            .block();
    }

    @Override
    public GeneralStatsDTO getGlobalRoleStats() {
        return webClientBuilder.build()
            .get()
            .uri(eventsUrl + "/api/events/global-roles")
            .retrieve()
            .bodyToMono(GeneralStatsDTO.class)
            .block();
    }
    
}

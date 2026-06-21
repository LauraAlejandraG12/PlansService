package com.plan.qv_ms_plans.clients.http;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.plan.qv_ms_plans.clients.UserServiceClient;
import com.plan.qv_ms_plans.model.dto.dashboard.GeneralStatsDTO;
import com.plan.qv_ms_plans.model.dto.dashboard.MonthlyGrowthDTO;
import com.plan.qv_ms_plans.model.dto.dashboard.PlanStatsDTO;
import com.plan.qv_ms_plans.repository.UserPlanRepository;


import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class HttpUserServiceClient implements UserServiceClient{
    
    private final WebClient.Builder webClientBuilder;
    private final UserPlanRepository userPlanRepository;

    @Value("${services.auth.url}")
    private String authUrl;

    @Override
    public GeneralStatsDTO getStats() {
        return webClientBuilder.build()
            .get()
            .uri(authUrl + "/api/users/stats")
            .retrieve()
            .bodyToMono(GeneralStatsDTO.class)
            .block();
    }

    @Override
    public List<PlanStatsDTO> getOrganizersByPlan(String startDate, String endDate, String plan) {
        // Datos propios — consulta directa al repo, sin llamada HTTP
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end   = endDate   != null ? LocalDate.parse(endDate)   : null;

        return userPlanRepository
            .countOrganizersByPlan(start, end, plan)
            .stream()
            .map(row -> new PlanStatsDTO((String) row[0], (Long) row[1]))
            .toList();
    }

    @Override
    public List<MonthlyGrowthDTO> getMonthlyGrowth(String startDate, String endDate) {
        String uri = authUrl + "/api/users/monthly-growth";

        if (startDate != null && endDate != null) {
            uri += "?startDate=" + startDate + "&endDate=" + endDate;
        }

        return webClientBuilder.build()
            .get()
            .uri(uri)
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<List<MonthlyGrowthDTO>>() {})
            .block();
    }


    @Override
    public String getUserNameById(Long userId) {
         try {
            return webClientBuilder.build()
                    .get()
                    .uri(authUrl + "/api/users/by-id/" + userId)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            } catch (Exception e) {
                return "Usuario " + userId;
        }
    } 
}
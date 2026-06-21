package com.plan.qv_ms_plans.service.dashboard;

import com.plan.qv_ms_plans.clients.EventServiceClient;
import com.plan.qv_ms_plans.clients.UserServiceClient;
import com.plan.qv_ms_plans.model.dto.dashboard.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserServiceClient userClient;
    private final EventServiceClient eventClient;


    /**
     * RF18 - Obtiene las estadísticas generales del sistema
     * Combina datos del microservicio de usuarios y eventos.
     * @return objeto con los totales del sistema
     */
    public GeneralStatsDTO getGeneralStats() {
        GeneralStatsDTO stats = userClient.getStats();
        stats.setTotalEvents(eventClient.getTotalEvents());

        GeneralStatsDTO roles = eventClient.getGlobalRoleStats();
        stats.setTotalOrganizers(roles.getTotalOrganizers());
        stats.setTotalStaff(roles.getTotalStaff());
        stats.setTotalAssistants(roles.getTotalAssistants());
        stats.setTotalJudges(roles.getTotalJudges());
        stats.setTotalParticipants(roles.getTotalParticipants());

        return stats;
    }



    /**
     * RF19 y RF19.1 - Obtiene la distribución de organizadores por plan
     * y calcula automáticamente el plan con mayor número de organizadores
     *
     * @return objeto con la lista de planes y el plan destacado
     */
    public PlanStatsResponseDTO getOrganizersByPlan(String startDate, String endDate, String plan){
        List<PlanStatsDTO> plans = userClient.getOrganizersByPlan(startDate, endDate, plan);

        String featuredPlan = plans.stream()
                .max(Comparator.comparingLong(PlanStatsDTO::getNumberOrganizers))
                .map(PlanStatsDTO::getPlanName)
                .orElse("");

        return new PlanStatsResponseDTO(plans, featuredPlan);
    }

    /**
     * RF20 y RF20.1 - Obtiene la distribución de organizadores por plan
     * y calcula automáticamente el plan con mayor
     *
     * @return objeto con la lista de organizadores y el top organizador
     */
    public EventByOrganizerResponseDTO getEventByOrganizer(String startDate, String endDate) {
    List<EventByOrganizerDTO> organizers = eventClient.getEventsByOrganizer(startDate, endDate);

    for (var org : organizers) {
    String realName = userClient.getUserNameById(org.getOrganizerId());
    org.setOrganizerName(realName);
    }

    Long maxEvents = organizers.stream()
        .mapToLong(EventByOrganizerDTO::getNumberEvents)
        .max()
        .orElse(0L);

    List<String> topOrganizers = organizers.stream()
        .filter(o -> o.getNumberEvents().equals(maxEvents))
        .map(EventByOrganizerDTO::getOrganizerName)  // ← y aquí
        .collect(Collectors.toList());

    String top = String.join(", ", topOrganizers);
    return new EventByOrganizerResponseDTO(organizers, top);
}

    /**
     * RF20.2 - Obtiene la cantidad de usuarios por evento
     * diferenciados por rol
     *
     * @return lista de eventos con desglose de usuarios por rol
     */
    public List<EventUserDetailDTO> getUsersByEvent(){
        return eventClient.getUsersByEvent();
    }

    /**
     * RF21 y RF21.1 - Obtiene el crecimiento mensaul de usuarios
     * y calcula el mes con mayor número de registros
     *
     * @param startDate fecha de inicio del filtro
     * @param endDate fecha fin del filtro
     * @return objeto con la lista de meses y el mes pico
     */
    public MonthlyGrowthResponseDTO getMonthlyGrowth(String startDate, String endDate){
        List<MonthlyGrowthDTO> months = userClient.getMonthlyGrowth(startDate, endDate);

        String peakMonth = months.stream()
                .max(Comparator.comparingLong(MonthlyGrowthDTO::getNewUsers))
                .map(MonthlyGrowthDTO::getMonth)
                .orElse("");

        return new MonthlyGrowthResponseDTO(months, peakMonth);

    }
}
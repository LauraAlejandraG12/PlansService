package com.plan.qv_ms_plans.clients.mock;

import com.plan.qv_ms_plans.clients.EventServiceClient;
import com.plan.qv_ms_plans.model.dto.dashboard.EventByOrganizerDTO;
import com.plan.qv_ms_plans.model.dto.dashboard.EventUserDetailDTO;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Profile("dev")
public class MockEventServiceClient implements EventServiceClient {

    @Override
    public Long getTotalEvents(){
        return 40L;
    }

    @Override
    public List<EventByOrganizerDTO> getEventsByOrganizer(String startDate, String endDate){
        List<EventByOrganizerDTO> dataList = new ArrayList<>( List.of(
                new EventByOrganizerDTO(1L, "Ana García", 8L),
                new EventByOrganizerDTO(1L, "Diego Gonzales", 8L),
                new EventByOrganizerDTO(2L, "Carlos López",   5L),
                new EventByOrganizerDTO(3L, "María Torres",   3L),
                new EventByOrganizerDTO(4L, "Juan Pérez",     1L)
        ));

         if (startDate != null && endDate != null
                && !startDate.isEmpty() && !endDate.isEmpty()) {
            return dataList.stream()
                .filter(o -> o.getNumberEvents() > 3)
                .collect(Collectors.toList());
        }

        return dataList;
    }

    @Override
    public List<EventUserDetailDTO> getUsersByEvent(){
        return List.of(
                new EventUserDetailDTO(1L, "Evento Empresarial", 4L, 80L, 3L, 25L),
                new EventUserDetailDTO(2L, "Evento Educativo", 2L, 50L, 5L, 40L),
                new EventUserDetailDTO(3L, "Evento Deportivo", 3L, 30L, 4L, 20L)
        );
    }
}
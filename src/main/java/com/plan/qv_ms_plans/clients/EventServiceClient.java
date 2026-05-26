package com.plan.qv_ms_plans.clients;

import com.plan.qv_ms_plans.model.dto.dashboard.EventByOrganizerDTO;
import com.plan.qv_ms_plans.model.dto.dashboard.EventUserDetailDTO;

import java.util.List;

public interface EventServiceClient {
    Long getTotalEvents();
    List<EventByOrganizerDTO> getEventsByOrganizer();
    List<EventUserDetailDTO> getUsersByEvent();
}

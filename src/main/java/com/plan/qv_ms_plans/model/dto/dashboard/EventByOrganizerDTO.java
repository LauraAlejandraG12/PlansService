package com.plan.qv_ms_plans.model.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventByOrganizerDTO {
    private Long organizerId;
    private String organizerEmail;
    private String organizerName;
    private Long numberEvents;
}

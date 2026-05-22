package com.plan.qv_ms_plans.model.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventUserDetailDTO {
    private Long eventId;
    private String eventName;
    private Long staff;
    private Long judges;
    private Long participants;
    private Long assistants;
}

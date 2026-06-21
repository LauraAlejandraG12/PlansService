package com.plan.qv_ms_plans.model.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralStatsDTO {
    private Long totalEvents;
    private Long totalUsers;
    private Long totalOrganizers;
    private Long totalStaff;
    private Long totalAssistants;
    private Long totalJudges;
    private Long totalParticipants;
}

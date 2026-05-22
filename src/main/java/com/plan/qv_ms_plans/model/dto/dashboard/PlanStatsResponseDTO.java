package com.plan.qv_ms_plans.model.dto.dashboard;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanStatsResponseDTO {
    private List<PlanStatsDTO> plans;
    private String featuredPlan;
}

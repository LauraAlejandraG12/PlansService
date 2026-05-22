package com.plan.qv_ms_plans.model.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyGrowthDTO {
    private String month;
    private Long newUsers;
}

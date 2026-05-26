package com.plan.qv_ms_plans.model.dto.dashboard;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyGrowthResponseDTO {
    private List<MonthlyGrowthDTO> months;
    private String peakMonth;
}

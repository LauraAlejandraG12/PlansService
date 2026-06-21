package com.plan.qv_ms_plans.clients;

import com.plan.qv_ms_plans.model.dto.dashboard.GeneralStatsDTO;
import com.plan.qv_ms_plans.model.dto.dashboard.MonthlyGrowthDTO;
import com.plan.qv_ms_plans.model.dto.dashboard.PlanStatsDTO;

import java.util.List;

public interface UserServiceClient {
    GeneralStatsDTO getStats();
    List<PlanStatsDTO> getOrganizersByPlan(String startDate, String endDate, String plan);
    List<MonthlyGrowthDTO> getMonthlyGrowth(String startDate, String endDate);
    String getUserNameById(Long UserId);
}
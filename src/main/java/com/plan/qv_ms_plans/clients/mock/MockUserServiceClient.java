package com.plan.qv_ms_plans.clients.mock;

import com.plan.qv_ms_plans.clients.UserServiceClient;
import com.plan.qv_ms_plans.model.dto.dashboard.GeneralStatsDTO;
import com.plan.qv_ms_plans.model.dto.dashboard.MonthlyGrowthDTO;
import com.plan.qv_ms_plans.model.dto.dashboard.PlanStatsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MockUserServiceClient implements UserServiceClient {
    @Override
    public GeneralStatsDTO getStats(){
        return new GeneralStatsDTO(120L, 15L, 30L, 50L, 10L, 15L, 0L);
    }

    @Override
    public List<PlanStatsDTO> getOrganizersByPlan(){
        return List.of(
                new PlanStatsDTO("Basico", 5L),
                new PlanStatsDTO("Estandar", 8L),
                new PlanStatsDTO("Premium", 2L),
                new PlanStatsDTO("Basico medio", 10L)
        );
    }

    @Override
    public List<MonthlyGrowthDTO> getMonthlyGrowth(String startDate, String endDate){
        return List.of(
                new MonthlyGrowthDTO("2026-01-14", 12L),
                new MonthlyGrowthDTO("2026-02-20", 19L),
                new MonthlyGrowthDTO("2026-03-02", 24L),
                new MonthlyGrowthDTO("2026-04-15", 17L)
        );
    }
}

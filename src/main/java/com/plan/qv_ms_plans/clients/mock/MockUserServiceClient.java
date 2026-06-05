package com.plan.qv_ms_plans.clients.mock;

import com.plan.qv_ms_plans.clients.UserServiceClient;
import com.plan.qv_ms_plans.model.dto.dashboard.GeneralStatsDTO;
import com.plan.qv_ms_plans.model.dto.dashboard.MonthlyGrowthDTO;
import com.plan.qv_ms_plans.model.dto.dashboard.PlanStatsDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MockUserServiceClient implements UserServiceClient {
    @Override
    public GeneralStatsDTO getStats(){
        return new GeneralStatsDTO(120L, 15L, 30L, 50L, 10L, 15L, 0L);
    }

    @Override
    public List<PlanStatsDTO> getOrganizersByPlan(String startDate, String endDate, String plan){
        List<PlanStatsDTO> dataList = new ArrayList<>(List.of(
                new PlanStatsDTO("Basico", 5L),
                new PlanStatsDTO("Estandar", 8L),
                new PlanStatsDTO("Premium", 2L),
                new PlanStatsDTO("Basico medio", 10L)
        ));

        if (plan != null && !plan.isEmpty()) {
            return dataList.stream()
                .filter(p -> p.getPlanName().equalsIgnoreCase(plan))
                .collect(Collectors.toList());
        }

        return dataList;
    }

    @Override
    public List<MonthlyGrowthDTO> getMonthlyGrowth(String startDate, String endDate){
        List<MonthlyGrowthDTO> dataList = new ArrayList<>(List.of(
                new MonthlyGrowthDTO("2026-01-14", 12L),
                new MonthlyGrowthDTO("2026-02-20", 19L),
                new MonthlyGrowthDTO("2026-03-02", 24L),
                new MonthlyGrowthDTO("2026-04-15", 17L)
        ));

        if (startDate != null && endDate != null
                && !startDate.isEmpty() && !endDate.isEmpty()) {
            return dataList.stream()
                .filter(m -> m.getMonth().compareTo(startDate) >= 0
                          && m.getMonth().compareTo(endDate) <= 0)
                .collect(Collectors.toList());
        }

        return dataList;
    }
}
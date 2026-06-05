package com.plan.qv_ms_plans.controller.dashboard;

import com.plan.qv_ms_plans.model.dto.dashboard.*;
import com.plan.qv_ms_plans.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    /**
     * RF18 - Retorna las estadíticas generales del sistema incluyendo
     * totales de usuarios, organizadores, persdonal, asistentes, jurados,
     * participantes y eventos.
     *
     * @Return estadísticas generales del sistema
     */
    @GetMapping("/stats")
    public ResponseEntity<GeneralStatsDTO> getGeneralStats(){
        return ResponseEntity.ok(dashboardService.getGeneralStats());
    }

    /**
     * RF19 Y RF19.1 - Retorna las estadísticas de organizadores por tipo de plan
     * identifica automáticamente el plan más utilizado
     * @Param startDate fecha de inicio del filtro
     * @param endDate fecha de fin del filtro
     * @Param plan tipo de plan a filtrar
     * @return lista de planes con cantidad de organizadores y plan destacado
     */
    @GetMapping("/plans")
    public ResponseEntity<PlanStatsResponseDTO> getOrganizersByPlan(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String plan
    ){
        return ResponseEntity.ok(dashboardService.getOrganizersByPlan(startDate, endDate, plan));
    }

    /**
     * RF20 Y RF20.1 - Retorna la cantidad de eventos creados por cada organizador
     * identifica al organizador con eventos
     *
     * @return lista de organizadores con su cantidad de eventos y top organizador
     */
    @GetMapping("/organizers")
    public ResponseEntity<EventByOrganizerResponseDTO> getEventsByOrganizer(
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        @RequestParam(required = false) String plan
    ) {
        return ResponseEntity.ok(dashboardService.getEventByOrganizer(startDate, endDate, plan));
    }

    /**
     * RF20.2 - Retonar la cantidad de usuarios asociados a ada evento
     * diferenciados por rol: personal, asistentes, jurados y participantes.
     *
     * @return lista de eventos con usuarios por rol
     */
    @GetMapping("/events/usersByRole")
    public ResponseEntity<List<EventUserDetailDTO>> getUsersByEvent(){
        return ResponseEntity.ok(dashboardService.getUsersByEvent());
    }


    /**
     * RF21 y RF21.1 - Retornar el crecimiento mensual de usuarios registrados
     * identifica el mes con mayopr crecimiento
     * @param startDate
     * @param endDate
     * @return lista de meses con nuevos usuarios y mes pico
     */
    @GetMapping("/growth")
    public ResponseEntity<MonthlyGrowthResponseDTO> getMonthlyGrowth(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        return ResponseEntity.ok(dashboardService.getMonthlyGrowth(startDate, endDate));
    }

}
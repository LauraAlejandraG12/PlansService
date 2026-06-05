package com.plan.qv_ms_plans.controller.dashboard;

import org.springframework.http.HttpHeaders;   
import org.springframework.http.MediaType;     
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.plan.qv_ms_plans.service.dashboard.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/report")
@RequiredArgsConstructor
public class ReportController {

   private final ReportService reportService;

    @GetMapping("/excel")
    public ResponseEntity<byte[]> downloadExel(
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        @RequestParam(required = false) String plan
    ) throws Exception {
        byte[] exelBytes = reportService.generateExcel(startDate, endDate, plan);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=dashboard-reporte.xlsx")
                .contentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(exelBytes);
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdf(
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        @RequestParam(required = false) String plan
    ) throws Exception {
        byte[] pdfBytes = reportService.generatePdf(startDate, endDate, plan);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=dashboard-reporte.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
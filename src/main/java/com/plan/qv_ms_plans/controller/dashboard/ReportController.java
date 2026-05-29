package com.plan.qv_ms_plans.controller.dashboard;

import org.springframework.http.HttpHeaders;   
import org.springframework.http.MediaType;     
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.plan.qv_ms_plans.service.dashboard.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

   private final ReportService reportService;

    @GetMapping("/excel")
    public ResponseEntity<byte[]> downloadExel() throws Exception {
        byte[] exelBytes = reportService.generateExel();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=dashboard-reporte.xlsx")
                .contentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(exelBytes);
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdf() throws Exception {
        byte[] pdfBytes = reportService.generatePdf();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=dashboard-reporte.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}

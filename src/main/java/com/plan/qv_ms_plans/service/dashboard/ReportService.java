package com.plan.qv_ms_plans.service.dashboard;

import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

/**
 * Servicio de generación de reportes del dashboard.
 * Genera reportes en formato exel y PDF con las métricas
 * y estadíticas del sistema (RF23)
 * 
 * @author Laura Alejandra Giraldo
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ReportService {

    private final DashboardService dashboardService;

    /**
     * Genera un reporte en formato Excel con cinco hojas:
     * Datos Gnerales, Planes, Eventos por Organizador,
     * Crecimiento Mensual y Usuarios por Evento.
     * 
     * @return arreglo de bytes con el archivo Excel generado
     * @throws IOException si ocurre un error al generar el archivo
     */
    public byte[] generateExcel(String startDate, String endDate, String plan) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.TEAL.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);

        // Hoja 1: DATOS GENERALES

        XSSFSheet statsSheet = workbook.createSheet("Datos Generales");
        String[] statsHeaders = {
                "Total Usuarios", "Organizadores", "Personal", "Invitados", "Total Eventos"
        };;

        Row statsHeader = statsSheet.createRow(0);
        for (int i = 0; i < statsHeaders.length; i++) {
            Cell cell = statsHeader.createCell(i);
            cell.setCellValue(statsHeaders[i]);
            cell.setCellStyle(headerStyle);
            statsSheet.setColumnWidth(i, 5000);
        }

        var stats = dashboardService.getGeneralStats();
        Row statsRow = statsSheet.createRow(1);
        statsRow.createCell(0).setCellValue(stats.getTotalUsers());
        statsRow.createCell(1).setCellValue(stats.getTotalOrganizers());
        statsRow.createCell(2).setCellValue(stats.getTotalStaff());
        statsRow.createCell(3).setCellValue(stats.getTotalGuests());
        statsRow.createCell(4).setCellValue(stats.getTotalEvents());

        // hoja 2: ORGANIZADORES POR PLAN

        Sheet plansSheet = workbook.createSheet("Planes");
        String[] plansHeaders = {
                "Plan", "Cantidad de Organizadores"
        };

        Row plansHeader = plansSheet.createRow(0);
        for (int i = 0; i < plansHeaders.length; i++) {
            Cell cell = plansHeader.createCell(i);
            cell.setCellValue(plansHeaders[i]);
            cell.setCellStyle(headerStyle);
            plansSheet.setColumnWidth(i, 6000);
        }

        var plansResult = dashboardService.getOrganizersByPlan(startDate, endDate, plan);
        int plansRowNum = 1;
        for (var planItem : plansResult.getPlans()) {
            Row row = plansSheet.createRow(plansRowNum++);
            row.createCell(0).setCellValue(planItem.getPlanName());
            row.createCell(1).setCellValue(planItem.getNumberOrganizers());
        }

        // hoja 3: EVENTOS POR ORGANIZADOR

        Sheet organizersSheet = workbook.createSheet("Eventos por Organizador");
        String[] organizersHeaders = {
                "Organizador", "Cantidad de Eventos"
        };

        Row organizersHeader = organizersSheet.createRow(0);
        for (int i = 0; i < organizersHeaders.length; i++) {
            Cell cell = organizersHeader.createCell(i);
            cell.setCellValue(organizersHeaders[i]);
            cell.setCellStyle(headerStyle);
            organizersSheet.setColumnWidth(i, 6000);
        }

        var organizers = dashboardService.getEventByOrganizer(startDate, endDate);
        int organizerRowNum = 1;
        for (var organizer : organizers.getOrganizers()) {
            Row row = organizersSheet.createRow(organizerRowNum++);
            row.createCell(0).setCellValue(organizer.getOrganizerName());
            row.createCell(1).setCellValue(organizer.getNumberEvents());
        }

        Sheet growthSheet = workbook.createSheet("Crecimiento Mensual");
        String[] growthHeaders = {
                "Mes", "Nuevos Usuarios"
        };

        Row growthHeader = growthSheet.createRow(0);
        for (int i = 0; i < growthHeaders.length; i++) {
            Cell cell = growthHeader.createCell(i);
            cell.setCellValue(growthHeaders[i]);
            cell.setCellStyle(headerStyle);
            growthSheet.setColumnWidth(i, 6000);
        }

        var growth = dashboardService.getMonthlyGrowth(null, null);
        int growthRowNum = 1;
        for (var month : growth.getMonths()) {
            Row row = growthSheet.createRow(growthRowNum++);
            row.createCell(0).setCellValue(month.getMonth());
            row.createCell(1).setCellValue(month.getNewUsers());
        }

        // hoja 5: USUARIOS POR EVENTO

        Sheet eventsSheet = workbook.createSheet("Usuarios por Evento");
        String[] eventsHeaders = {
                "Evento", "Personal del evento", "Asistentes", "Jurados", "Participantes"
        };

        Row eventsHeader = eventsSheet.createRow(0);
        for (int i = 0; i < eventsHeaders.length; i++) {
            Cell cell = eventsHeader.createCell(i);
            cell.setCellValue(eventsHeaders[i]);
            cell.setCellStyle(headerStyle);
            eventsSheet.setColumnWidth(i, 6000);
        }

        var events = dashboardService.getUsersByEvent();
        int eventsRowNum = 1;
        for (var event : events) {
            Row row = eventsSheet.createRow(eventsRowNum++);
            row.createCell(0).setCellValue(event.getEventName());
            row.createCell(1).setCellValue(event.getStaff());
            row.createCell(2).setCellValue(event.getGuests());
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }



    /**
     * Genera un reporte en formato Excel con cinco hojas:
     * Datos Gnerales, Planes, Eventos por Organizador,
     * Crecimiento Mensual y Usuarios por Evento.
     * 
     * @Return arreglo de bytes con el archivo PDF generado
     * @throws IOException si ocurre un error al generar el archivo
     */
    private static final float[] TEAL    = { 0.18f, 0.56f, 0.56f };   // teal suave
    private static final float[] TEAL_LT = { 0.88f, 0.95f, 0.95f };   // teal muy claro
    private static final float[] DARK    = { 0.13f, 0.16f, 0.21f };
    private static final float[] GRAY    = { 0.45f, 0.47f, 0.52f };
    private static final float[] LIGHT   = { 0.96f, 0.97f, 0.98f };
    private static final float[] WHITE   = { 1f, 1f, 1f };
    private static final float[] BORDER  = { 0.87f, 0.87f, 0.87f };

    public byte[] generatePdf(String startDate, String endDate, String plan) throws IOException {
        PDDocument document = new PDDocument();
 
        PDType1Font fontBold    = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        PDType1Font fontRegular = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
 
        // ── PORTADA ──────────────────────────────────────
        PDPage cover = new PDPage(PDRectangle.A4);
        document.addPage(cover);
        try (PDPageContentStream cs = new PDPageContentStream(document, cover)) {
 
            // Franja superior teal (más estrecha y limpia)
            cs.setNonStrokingColor(TEAL[0], TEAL[1], TEAL[2]);
            cs.addRect(0, 742, 595, 100);
            cs.fill();
 
            // Nombre app en franja
            cs.setFont(fontBold, 28);
            cs.setNonStrokingColor(WHITE[0], WHITE[1], WHITE[2]);
            cs.beginText();
            cs.newLineAtOffset(50, 800);
            cs.showText("Qvenly");
            cs.endText();
 
            cs.setFont(fontRegular, 11);
            cs.beginText();
            cs.newLineAtOffset(50, 778);
            cs.showText("Plataforma de gestión de eventos");
            cs.endText();
 
            // Línea divisora teal clara debajo de la franja
            cs.setStrokingColor(TEAL_LT[0], TEAL_LT[1], TEAL_LT[2]);
            cs.setLineWidth(1f);
            cs.moveTo(0, 742);
            cs.lineTo(595, 742);
            cs.stroke();
 
            // Bloque central de título
            cs.setFont(fontBold, 30);
            cs.setNonStrokingColor(DARK[0], DARK[1], DARK[2]);
            cs.beginText();
            cs.newLineAtOffset(50, 640);
            cs.showText("Reporte del Dashboard");
            cs.endText();
 
            cs.setFont(fontBold, 16);
            cs.setNonStrokingColor(TEAL[0], TEAL[1], TEAL[2]);
            cs.beginText();
            cs.newLineAtOffset(50, 610);
            cs.showText("Administrador");
            cs.endText();
 
            // Línea separadora simple
            cs.setStrokingColor(BORDER[0], BORDER[1], BORDER[2]);
            cs.setLineWidth(0.8f);
            cs.moveTo(50, 598);
            cs.lineTo(545, 598);
            cs.stroke();
 
            // Descripción
            cs.setFont(fontRegular, 11);
            cs.setNonStrokingColor(GRAY[0], GRAY[1], GRAY[2]);
            cs.beginText();
            cs.newLineAtOffset(50, 578);
            cs.showText("Este reporte contiene las métricas y estadísticas generales del sistema,");
            cs.endText();
            cs.beginText();
            cs.newLineAtOffset(50, 561);
            cs.showText("generadas automáticamente por la plataforma Qvenly.");
            cs.endText();
 
            // Fecha
            cs.setFont(fontBold, 10);
            cs.setNonStrokingColor(TEAL[0], TEAL[1], TEAL[2]);
            cs.beginText();
            cs.newLineAtOffset(50, 535);
            cs.showText("Fecha de generación:  " + java.time.LocalDate.now());
            cs.endText();
 
            // Caja de contenido con fondo claro
            cs.setNonStrokingColor(LIGHT[0], LIGHT[1], LIGHT[2]);
            cs.addRect(50, 340, 495, 160);
            cs.fill();
 
            // Borde izquierdo teal de la caja
            cs.setNonStrokingColor(TEAL[0], TEAL[1], TEAL[2]);
            cs.addRect(50, 340, 4, 160);
            cs.fill();
 
            // Título de contenido
            cs.setFont(fontBold, 11);
            cs.setNonStrokingColor(DARK[0], DARK[1], DARK[2]);
            cs.beginText();
            cs.newLineAtOffset(65, 485);
            cs.showText("Contenido del reporte");
            cs.endText();
 
            String[] sections = {
                    "1. Datos Generales del Sistema",
                    "2. Organizadores por Plan",
                    "3. Eventos por Organizador",
                    "4. Crecimiento Mensual de Usuarios",
                    "5. Usuarios por Evento"
            };
            float iy = 465;
            for (String section : sections) {
                cs.setFont(fontRegular, 10);
                cs.setNonStrokingColor(GRAY[0], GRAY[1], GRAY[2]);
                cs.beginText();
                cs.newLineAtOffset(70, iy);
                cs.showText(section);
                cs.endText();
                iy -= 22;
            }
 
            // Footer portada
            dibujarFooter(cs, fontRegular, 1);
        }
 
        // ── PÁGINA 2: DATOS GENERALES ─────────────────────
        PDPage page2 = new PDPage(PDRectangle.A4);
        document.addPage(page2);
        try (PDPageContentStream cs = new PDPageContentStream(document, page2)) {
            dibujarEncabezado(cs, fontBold, fontRegular, "Datos Generales");
 
            var stats = dashboardService.getGeneralStats();
            String[] headers = { "Indicador", "Valor" };
            float[] widths   = { 350f, 145f };
            String[][] rows  = {
                    { "Total Usuarios",  String.valueOf(stats.getTotalUsers()) },
                    { "Organizadores",   String.valueOf(stats.getTotalOrganizers()) },
                    { "Personal",        String.valueOf(stats.getTotalStaff()) },
                    { "Invitados",       String.valueOf(stats.getTotalGuests()) },
                    { "Total Eventos",   String.valueOf(stats.getTotalEvents()) }
            };
 
            float ty = 700;
            dibujarCabeceraMultiple(cs, fontBold, headers, widths, ty);
            ty -= 28;
            boolean alt = false;
            for (String[] row : rows) {
                dibujarFilaMultiple(cs, fontRegular, row, widths, ty, alt);
                ty -= 26;
                alt = !alt;
            }
            dibujarFooter(cs, fontRegular, 2);
        }
 
        // ── PÁGINA 3: ORGANIZADORES POR PLAN ─────────────
        PDPage page3 = new PDPage(PDRectangle.A4);
        document.addPage(page3);
        try (PDPageContentStream cs = new PDPageContentStream(document, page3)) {
            dibujarEncabezado(cs, fontBold, fontRegular, "Organizadores por Plan");
 
            var plansResult = dashboardService.getOrganizersByPlan(startDate, endDate, null);
            dibujarBadge(cs, fontBold, "Plan más utilizado: " + plansResult.getFeaturedPlan(), 700);
 
            String[] headers = { "Plan", "Organizadores" };
            float[] widths   = { 350f, 145f };
 
            float ty = 668;
            dibujarCabeceraMultiple(cs, fontBold, headers, widths, ty);
            ty -= 28;
            boolean alt = false;
            for (var planItem : plansResult.getPlans()) {
                dibujarFilaMultiple(cs, fontRegular,
                        new String[]{ planItem.getPlanName(), String.valueOf(planItem.getNumberOrganizers()) },
                        widths, ty, alt);
                ty -= 26;
                alt = !alt;
            }
            dibujarFooter(cs, fontRegular, 3);
        }
 
        // ── PÁGINA 4: EVENTOS POR ORGANIZADOR ────────────
        PDPage page4 = new PDPage(PDRectangle.A4);
        document.addPage(page4);
        try (PDPageContentStream cs = new PDPageContentStream(document, page4)) {
            dibujarEncabezado(cs, fontBold, fontRegular, "Eventos por Organizador");
 
            var orgs = dashboardService.getEventByOrganizer(startDate, endDate);
            dibujarBadge(cs, fontBold, "Top organizador: " + orgs.getTopOrganizer(), 700);
 
            String[] headers = { "Organizador", "Eventos" };
            float[] widths   = { 350f, 145f };
 
            float ty = 668;
            dibujarCabeceraMultiple(cs, fontBold, headers, widths, ty);
            ty -= 28;
            boolean alt = false;
            for (var org : orgs.getOrganizers()) {
                dibujarFilaMultiple(cs, fontRegular,
                        new String[]{ org.getOrganizerName(), String.valueOf(org.getNumberEvents()) },
                        widths, ty, alt);
                ty -= 26;
                alt = !alt;
            }
            dibujarFooter(cs, fontRegular, 4);
        }
 
        // ── PÁGINA 5: CRECIMIENTO MENSUAL ────────────────
        PDPage page5 = new PDPage(PDRectangle.A4);
        document.addPage(page5);
        try (PDPageContentStream cs = new PDPageContentStream(document, page5)) {
            dibujarEncabezado(cs, fontBold, fontRegular, "Crecimiento Mensual");
 
            var growth = dashboardService.getMonthlyGrowth(startDate, endDate);
            dibujarBadge(cs, fontBold, "Mes con mayor crecimiento: " + growth.getPeakMonth(), 700);
 
            String[] headers = { "Mes", "Nuevos Usuarios" };
            float[] widths   = { 350f, 145f };
 
            float ty = 668;
            dibujarCabeceraMultiple(cs, fontBold, headers, widths, ty);
            ty -= 28;
            boolean alt = false;
            for (var month : growth.getMonths()) {
                dibujarFilaMultiple(cs, fontRegular,
                        new String[]{ month.getMonth(), String.valueOf(month.getNewUsers()) },
                        widths, ty, alt);
                ty -= 26;
                alt = !alt;
            }
            dibujarFooter(cs, fontRegular, 5);
        }
 
        // ── PÁGINA 6: USUARIOS POR EVENTO ────────────────
        PDPage page6 = new PDPage(PDRectangle.A4);
        document.addPage(page6);
        try (PDPageContentStream cs = new PDPageContentStream(document, page6)) {
            dibujarEncabezado(cs, fontBold, fontRegular, "Usuarios por Evento");
 
            var events = dashboardService.getUsersByEvent();
            String[] headers = { "Evento", "Personal", "Invitados" };
            float[] widths   = { 250f, 145f, 145f };
 
            float ty = 700;
            dibujarCabeceraMultiple(cs, fontBold, headers, widths, ty);
            ty -= 28;
            boolean alt = false;
            for (var event : events) {
                dibujarFilaMultiple(cs, fontRegular,
                        new String[]{
                                event.getEventName(),
                                String.valueOf(event.getStaff()),
                                String.valueOf(event.getGuests())
                        },
                        widths, ty, alt);
            }
            dibujarFooter(cs, fontRegular, 6);
        }
 
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.save(out);
        document.close();
        return out.toByteArray();
    }
 
    // ══════════════════════════════════════
    // MÉTODOS AUXILIARES
    // ══════════════════════════════════════
 
    private void dibujarEncabezado(PDPageContentStream cs,
            PDType1Font fontBold, PDType1Font fontRegular,
            String titulo) throws IOException {
 
        // Barra superior
        cs.setNonStrokingColor(TEAL[0], TEAL[1], TEAL[2]);
        cs.addRect(0, 800, 595, 42);
        cs.fill();
 
        // Nombre en header
        cs.setFont(fontBold, 12);
        cs.setNonStrokingColor(WHITE[0], WHITE[1], WHITE[2]);
        cs.beginText();
        cs.newLineAtOffset(50, 814);
        cs.showText("Qvenly Admin");
        cs.endText();
 
        // Título sección
        cs.setFont(fontBold, 22);
        cs.setNonStrokingColor(DARK[0], DARK[1], DARK[2]);
        cs.beginText();
        cs.newLineAtOffset(50, 762);
        cs.showText(titulo);
        cs.endText();
 
        // Línea teal simple bajo el título
        cs.setStrokingColor(TEAL[0], TEAL[1], TEAL[2]);
        cs.setLineWidth(2f);
        cs.moveTo(50, 750);
        cs.lineTo(545, 750);
        cs.stroke();
    }
 
    private void dibujarBadge(PDPageContentStream cs,
            PDType1Font fontBold,
            String texto, float y) throws IOException {
 
        // Fondo teal claro
        cs.setNonStrokingColor(TEAL_LT[0], TEAL_LT[1], TEAL_LT[2]);
        cs.addRect(50, y - 18, 400, 26);
        cs.fill();
 
        // Borde izquierdo teal
        cs.setNonStrokingColor(TEAL[0], TEAL[1], TEAL[2]);
        cs.addRect(50, y - 18, 4, 26);
        cs.fill();
 
        cs.setFont(fontBold, 10);
        cs.setNonStrokingColor(DARK[0], DARK[1], DARK[2]);
        cs.beginText();
        cs.newLineAtOffset(62, y - 8);
        cs.showText(texto);
        cs.endText();
    }
 
    private void dibujarCabeceraMultiple(PDPageContentStream cs,
            PDType1Font fontBold,
            String[] headers, float[] widths, float y) throws IOException {
 
        float x = 50;
        for (int i = 0; i < headers.length; i++) {
            cs.setNonStrokingColor(TEAL[0], TEAL[1], TEAL[2]);
            cs.addRect(x, y - 20, widths[i] - 2, 28);
            cs.fill();
 
            cs.setFont(fontBold, 10);
            cs.setNonStrokingColor(WHITE[0], WHITE[1], WHITE[2]);
            cs.beginText();
            cs.newLineAtOffset(x + 8, y - 10);
            cs.showText(headers[i]);
            cs.endText();
 
            x += widths[i];
        }
    }
 
    private void dibujarFilaMultiple(PDPageContentStream cs,
            PDType1Font fontRegular,
            String[] values, float[] widths,
            float y, boolean alternate) throws IOException {
 
        float x = 50;
        float[] bg = alternate ? LIGHT : WHITE;
 
        for (int i = 0; i < values.length; i++) {
            // Fondo fila
            cs.setNonStrokingColor(bg[0], bg[1], bg[2]);
            cs.addRect(x, y - 18, widths[i] - 2, 24);
            cs.fill();
 
            // Borde inferior sutil
            cs.setStrokingColor(BORDER[0], BORDER[1], BORDER[2]);
            cs.setLineWidth(0.3f);
            cs.moveTo(x, y - 18);
            cs.lineTo(x + widths[i] - 2, y - 18);
            cs.stroke();
 
            cs.setFont(fontRegular, 10);
            cs.setNonStrokingColor(DARK[0], DARK[1], DARK[2]);
            cs.beginText();
            cs.newLineAtOffset(x + 8, y - 10);
            cs.showText(values[i]);
            cs.endText();
 
            x += widths[i];
        }
    }
 
    private void dibujarFooter(PDPageContentStream cs,
            PDType1Font fontRegular,
            int pageNum) throws IOException {
 
        cs.setStrokingColor(BORDER[0], BORDER[1], BORDER[2]);
        cs.setLineWidth(0.8f);
        cs.moveTo(50, 52);
        cs.lineTo(545, 52);
        cs.stroke();
 
        cs.setFont(fontRegular, 8);
        cs.setNonStrokingColor(GRAY[0], GRAY[1], GRAY[2]);
        cs.beginText();
        cs.newLineAtOffset(50, 38);
        cs.showText("Qvenly Admin — Reporte generado automáticamente");
        cs.endText();
 
        cs.beginText();
        cs.newLineAtOffset(510, 38);
        cs.showText("Pág. " + pageNum);
        cs.endText();
    }

}
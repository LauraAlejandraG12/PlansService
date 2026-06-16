package com.plan.qv_ms_plans.service.plans;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Servicio de envío de correos electrónicos para las notificaciones de planes.
 *
 * <p>Envía correos HTML con la identidad visual de Qvenly cuando ocurren eventos
 * relacionados con los planes: compra, renovación, cambio y vencimiento.
 * Todos los métodos son asíncronos para no bloquear la petición HTTP.</p>
 *
 * @author Equipo Qvenly
 * @version Eilyn Florez
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PlanEmailService {

    /** Cliente SMTP para crear y enviar mensajes. */
    private final JavaMailSender mailSender;

    /** Dirección de correo remitente. */
    @Value("${spring.mail.username}")
    private String fromEmail;

    /** URL base del frontend. */
    @Value("${app.frontend-url}")
    private String frontendUrl;

    // ─── Paleta de colores Qvenly ─────────────────────────────
    private static final String COLOR_PRIMARY   = "#14b8a6";
    private static final String COLOR_TEXT      = "#111827";
    private static final String COLOR_TEXT_SOFT = "#6b7280";
    private static final String COLOR_BG        = "#f9fafb";
    private static final String COLOR_WHITE     = "#ffffff";
    private static final String COLOR_BORDER    = "#e5e7eb";

    /**
     * Envía un correo de notificación de plan al usuario.
     *
     * @param toEmail  correo del destinatario
     * @param userName nombre del destinatario
     * @param title    título del correo
     * @param message  mensaje principal del correo
     */
    @Async
    public void sendPlanNotificationEmail(String toEmail, String userName,
                                          String title, String message) {
        if (toEmail == null || toEmail.isBlank()) return;
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true, "UTF-8");

            String body = greeting(userName)
                    + paragraph(message)
                    + ctaButton(frontendUrl + "/dashboard-user", "Ir a mi cuenta");

            String html = baseTemplate(headerTitle(title), body);

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(title + " - Qvenly");
            helper.setText(html, true);
            mailSender.send(mail);
            log.info("Correo de notificación de plan enviado a: {}", toEmail);

        } catch (Exception e) {
            log.error("Error al enviar correo de plan a {}: {}", toEmail, e.getMessage());
        }
    }

    // ─── Plantilla y componentes ──────────────────────────────

    /**
     * Genera el HTML completo del correo con la plantilla base de Qvenly.
     */
    private String baseTemplate(String headerContent, String bodyContent) {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head><meta charset="UTF-8"><title>Qvenly</title></head>
            <body style="margin:0; padding:0; background-color:%s; font-family:'Segoe UI', Arial, sans-serif;">
              <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:%s; padding: 40px 16px;">
                <tr><td align="center">
                  <table width="600" cellpadding="0" cellspacing="0"
                         style="max-width:600px; width:100%%; background-color:%s;
                                border-radius:12px; overflow:hidden; box-shadow: 0 4px 24px rgba(0,0,0,0.08);">
                    <tr>
                      <td style="background-color:%s; padding: 32px 40px; text-align:center;">
                        <p style="margin:0 0 12px; font-size:13px; font-weight:600;
                                  color:rgba(255,255,255,0.8); letter-spacing:2px; text-transform:uppercase;">QVENLY</p>
                        %s
                      </td>
                    </tr>
                    <tr><td style="padding: 40px;">%s</td></tr>
                    <tr>
                      <td style="padding: 24px 40px; border-top: 1px solid %s; text-align:center;">
                        <p style="margin:0; font-size:12px; color:%s;">
                          &copy; 2025 Qvenly &mdash; Plataforma de gesti&oacute;n de eventos
                        </p>
                      </td>
                    </tr>
                  </table>
                </td></tr>
              </table>
            </body>
            </html>
            """.formatted(
                COLOR_BG, COLOR_BG, COLOR_WHITE,
                COLOR_PRIMARY, headerContent, bodyContent,
                COLOR_BORDER, COLOR_TEXT_SOFT
        );
    }

    /** Genera el título del encabezado. */
    private String headerTitle(String title) {
        return "<h1 style=\"margin:0; font-size:22px; font-weight:700; color:" + COLOR_WHITE + ";\">"
                + title + "</h1>";
    }

    /** Genera el saludo personalizado. */
    private String greeting(String userName) {
        String name = (userName != null && !userName.isBlank()) ? userName : "usuario";
        return "<p style=\"margin:0 0 16px; font-size:16px; color:" + COLOR_TEXT + ";\">Hola <strong>"
                + name + "</strong>,</p>";
    }

    /** Genera un párrafo de texto. */
    private String paragraph(String text) {
        return "<p style=\"margin:0 0 16px; font-size:15px; color:" + COLOR_TEXT_SOFT
                + "; line-height:1.6;\">" + text + "</p>";
    }

    /** Genera un botón de llamada a la acción. */
    private String ctaButton(String href, String label) {
        return """
            <table width="100%%" cellpadding="0" cellspacing="0" style="margin: 28px 0;">
              <tr><td align="center">
                <a href="%s" style="display:inline-block; background-color:%s; color:%s;
                          padding: 14px 36px; border-radius:8px; font-size:15px;
                          font-weight:600; text-decoration:none;">%s</a>
              </td></tr>
            </table>
            """.formatted(href, COLOR_PRIMARY, COLOR_WHITE, label);
    }
}
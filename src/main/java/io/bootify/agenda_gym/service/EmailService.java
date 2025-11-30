package io.bootify.agenda_gym.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de enviar correos electrónicos.
 * Permite envío asíncrono para no bloquear transacciones.
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envía un correo electrónico de forma asíncrona.
     *
     * @param destinatario Correo del destinatario
     * @param asunto       Asunto del correo
     * @param mensajeHtml  Contenido HTML del correo
     */
    @Async
    public void enviarCorreo(String destinatario, String asunto, String mensajeHtml) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(mensajeHtml, true); // true = HTML
            mailSender.send(mimeMessage);
            log.info("Correo enviado a {}", destinatario);
        } catch (MessagingException e) {
            log.error("Error al enviar correo a {}: {}", destinatario, e.getMessage(), e);
        }
    }
}

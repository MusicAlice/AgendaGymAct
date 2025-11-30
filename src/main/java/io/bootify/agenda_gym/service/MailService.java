package io.bootify.agenda_gym.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envío simple e informativo para notificar cambio de contraseña.
     * No lanza excepciones hacia el controlador (se debe capturar allí para no romper la UX).
     */
    public void sendPasswordChangedNotification(final String toEmail, final String username) {
        if (toEmail == null || toEmail.isBlank()) {
            return;
        }
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(toEmail);
            msg.setSubject("Confirmación de cambio de contraseña - GymTrack");
            msg.setText(
                    "Hola " + (username != null ? username : "") + ",\n\n" +
                    "Te informamos que la contraseña de tu cuenta ha sido cambiada correctamente. " +
                    "Si no realizaste este cambio, por favor contacta al soporte inmediatamente.\n\n" +
                    "Saludos,\nEl equipo de GymTrack"
            );
            // FROM por defecto lo toma spring.mail.username o app.mail.from
            mailSender.send(msg);
        } catch (Exception ex) {
            // No propagar: registrar y continuar (evitar fallos en la operación principal)
            // Si quieres logging, inyecta Logger o usa System.err temporalmente:
            System.err.println("Warning: no se pudo enviar el correo de confirmación: " + ex.getMessage());
        }
    }
}
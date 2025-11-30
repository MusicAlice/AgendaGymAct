package io.bootify.agenda_gym.rest;

import io.bootify.agenda_gym.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador temporal para probar el envío de correos.
 * Puedes eliminarlo después de confirmar que el correo funciona.
 */
@RestController
@RequestMapping("/api/email")
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/test")
    public ResponseEntity<String> testEmail(@RequestParam("to") String destinatario) {
        try {
            String asunto = "🔹 Prueba de correo desde Agenda Gym";
            String mensaje = "Hola!\n\nEste es un correo de prueba enviado correctamente desde tu aplicación Agenda Gym. 🎉\n\nSi ves este mensaje, la configuración SMTP funciona correctamente.";

            emailService.enviarCorreo(destinatario, asunto, mensaje);
            return ResponseEntity.ok("✅ Correo de prueba enviado correctamente a: " + destinatario);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("❌ Error al enviar el correo: " + e.getMessage());
        }
    }
}

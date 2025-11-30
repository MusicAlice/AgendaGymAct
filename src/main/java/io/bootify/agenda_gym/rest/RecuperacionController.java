package io.bootify.agenda_gym.rest;

import io.bootify.agenda_gym.service.RecuperacionClaveService;
import org.springframework.http.ResponseEntity;
import org. springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/recuperar")
public class RecuperacionController {

    private final RecuperacionClaveService recuperacionService;

    public RecuperacionController(RecuperacionClaveService recuperacionService) {
        this.recuperacionService = recuperacionService;
    }

    /**
     * PASO 1: Enviar código al correo
     */
    @PostMapping("/enviar-codigo")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> enviarCodigo(@RequestBody Map<String, String> request) {
        String email = request. get("email");
        
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "El correo es obligatorio"
            ));
        }
        
        boolean enviado = recuperacionService.enviarCodigoRecuperacion(email. trim());
        
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Si el correo está registrado, recibirás un código de verificación."
        ));
    }

    /**
     * PASO 2: Verificar código
     */
    @PostMapping("/verificar-codigo")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verificarCodigo(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String codigo = request. get("codigo");
        
        if (email == null || codigo == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Correo y código son obligatorios"
            ));
        }
        
        boolean valido = recuperacionService.verificarCodigo(email.trim(), codigo.trim());
        
        if (valido) {
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Código verificado correctamente"
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "Código inválido o expirado"
            ));
        }
    }

    /**
     * PASO 3: Cambiar contraseña
     */
    @PostMapping("/cambiar-password")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cambiarPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String codigo = request.get("codigo");
        String nuevaPassword = request.get("nuevaPassword");
        
        if (email == null || codigo == null || nuevaPassword == null) {
            return ResponseEntity.badRequest(). body(Map.of(
                "success", false,
                "message", "Todos los campos son obligatorios"
            ));
        }
        
        // Validar contraseña
        if (nuevaPassword.length() < 12) {
            return ResponseEntity. ok(Map.of(
                "success", false,
                "message", "La contraseña debe tener al menos 12 caracteres"
            ));
        }
        
        boolean cambiado = recuperacionService.cambiarPassword(email.trim(), codigo.trim(), nuevaPassword);
        
        if (cambiado) {
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "¡Contraseña actualizada!  Ya puedes iniciar sesión."
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                "success", false,
                "message", "Error al cambiar la contraseña.  Intenta de nuevo."
            ));
        }
    }
}

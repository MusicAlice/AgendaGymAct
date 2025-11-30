package io.bootify.agenda_gym.rest;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;

import io.bootify.agenda_gym.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Controlador para AJAX del frontend (endpoints con prefijo /api/mi-perfil)
 * Evita colisiones con controladores existentes que usan /mi-perfil.
 */
@RestController
@RequestMapping("/api/mi-perfil")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(final UserService userService, final PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@RequestBody Map<String,String> body, final Principal principal) {
        final String current = body.get("currentPassword");
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("valid", false, "message", "No autenticado"));
        }

        final String username = principal.getName();
        final boolean ok = userService.verifyPassword(username, current);

        if (ok) {
            return ResponseEntity.ok(Map.of("valid", true));
        } else {
            return ResponseEntity.ok(Map.of("valid", false, "message", "Contraseña incorrecta"));
        }
    }

    @PostMapping("/cambiar-clave")
    public ResponseEntity<?> cambiarClave(@RequestBody Map<String,String> body, final Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message","No autenticado"));
        }

        final String current = body.get("currentPassword");
        final String nueva = body.get("newPassword");
        final String username = principal.getName();

        if (!isValidPassword(nueva)) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "La nueva contraseña no cumple requisitos"));
        }

        final boolean changed = userService.changePassword(username, current, nueva);
        if (changed) {
            return ResponseEntity.ok(Map.of("success", true));
        } else {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "No se pudo cambiar la contraseña (clave actual incorrecta o error)"));
        }
    }

    private boolean isValidPassword(final String p) {
        if (p == null) return false;
        if (p.length() < 12) return false;
        if (!p.matches(".*[A-Z].*")) return false;
        if (!p.matches(".*[a-z].*")) return false;
        if (!p.matches(".*\\d.*")) return false;
        if (!p.matches(".*[!@#$%^&*(),.?\":{}|<>_\\-\\[\\]\\\\/~`+=;:].*")) return false;
        return true;
    }
}
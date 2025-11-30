package io.bootify. agenda_gym.rest;

import io.bootify.agenda_gym.domain. VerificationToken;
import io. bootify.agenda_gym.service. VerificationService;
import org.springframework.http.ResponseEntity;
import org. springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dev")
public class DevController {

    private final VerificationService verificationService;

    public DevController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping("/tokens")
    public ResponseEntity<List<VerificationToken>> tokens() {
        return ResponseEntity.ok(verificationService.listarTokensRecientes());
    }

    @PostMapping("/tokens/{usuarioId}/regenerate")
    public ResponseEntity<Map<String, Object>> regenerate(@PathVariable Long usuarioId) {
        VerificationToken t = verificationService.crearTokenYEnviarParaUsuarioId(usuarioId);
        if (t == null) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "message", "Usuario no encontrado"));
        }
        return ResponseEntity.ok(Map.of("ok", true, "token", t.getToken(), "id", t.getId()));
    }
}
package io. bootify.agenda_gym.rest;

import io.bootify.agenda_gym. domain.Usuario;
import io.bootify. agenda_gym.domain.VerificationToken;
import io.bootify.agenda_gym.repos.UsuarioRepository;
import io.bootify.agenda_gym.service.VerificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation. GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class VerificationController {

    private final VerificationService verificationService;
    private final UsuarioRepository usuarioRepository;

    public VerificationController(VerificationService verificationService,
                                   UsuarioRepository usuarioRepository) {
        this.verificationService = verificationService;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Verificación para usuarios de TIENDA (registro normal)
     */
    @GetMapping("/verify-account")
    public String verifyAccountHtml(@RequestParam("token") String token, 
                                     HttpSession session,
                                     Model model) {
        System.out.println("=== VERIFICANDO TOKEN (TIENDA) ===");
        System.out.println("Token recibido: " + token);
        
        Long usuarioId = verificationService.verificarTokenYObtenerUsuarioId(token);
        
        if (usuarioId != null) {
            System.out.println("Verificacion exitosa.  Usuario ID: " + usuarioId);
            
            // Login automático
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                session.setAttribute("usuarioLogueado", usuario);
                session.setAttribute("usuarioId", usuario.getId());
                session.setAttribute("usuarioNombre", usuario.getNombre());
                session.setAttribute("usuarioEmail", usuario.getEmail());
                
                System.out.println("✅ Usuario logueado automáticamente: " + usuario.getEmail());
            }
            
            model.addAttribute("estado", "ok");
            model. addAttribute("usuarioId", usuarioId);
            model.addAttribute("mensaje", "¡Cuenta verificada exitosamente!");
        } else {
            System.out.println("Verificacion fallida");
            model.addAttribute("estado", "error");
            model. addAttribute("mensaje", "El enlace de verificacion es invalido o ha expirado.");
        }

        return "verificacion";
    }

    /**
     * Verificación para usuarios de AGENDA
     */
    @GetMapping("/verify-account-agenda")
    public String verifyAccountAgendaHtml(@RequestParam("token") String token, 
                                           HttpSession session,
                                           Model model) {
        System. out.println("=== VERIFICANDO TOKEN (AGENDA) ===");
        System.out.println("Token recibido: " + token);
        
        Long usuarioId = verificationService.verificarTokenYObtenerUsuarioId(token);
        
        if (usuarioId != null) {
            System.out.println("Verificacion exitosa. Usuario ID: " + usuarioId);
            
            // Login automático
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                session.setAttribute("usuarioLogueado", usuario);
                session.setAttribute("usuarioId", usuario.getId());
                session. setAttribute("usuarioNombre", usuario.getNombre());
                session. setAttribute("usuarioEmail", usuario. getEmail());
                
                System.out.println("✅ Usuario logueado automáticamente: " + usuario.getEmail());
            }
            
            model. addAttribute("estado", "ok");
            model.addAttribute("usuarioId", usuarioId);
            model.addAttribute("mensaje", "¡Cuenta verificada exitosamente!");
        } else {
            System.out.println("Verificacion fallida");
            model.addAttribute("estado", "error");
            model.addAttribute("mensaje", "El enlace de verificacion es invalido o ha expirado.");
        }

        return "verificacion-agenda";
    }

    /**
     * Endpoint para debug
     */
    @GetMapping("/debug-tokens")
    public String debugTokens(Model model) {
        List<VerificationToken> tokens = verificationService.listarTokensRecientes();
        
        System.out.println("=== TOKENS EN BASE DE DATOS ===");
        System.out.println("Total tokens encontrados: " + tokens.size());
        
        for (VerificationToken tkn : tokens) {
            System.out.println("Token: " + tkn.getToken());
            System.out.println("Usuario: " + tkn.getUsuario().getEmail());
            System.out. println("Expira: " + tkn.getFechaExpiracion());
            System.out.println("Verificado: " + tkn.getUsuario().isVerificado());
            System.out.println("---");
        }
        
        model.addAttribute("tokens", tokens);
        return "debug-tokens";
    }

    @GetMapping("/debug-verification")
    public String debugVerification(Model model) {
        List<VerificationToken> tokens = verificationService.listarTokensRecientes();
        
        model.addAttribute("totalTokens", tokens.size());
        model.addAttribute("tokens", tokens);
        model.addAttribute("servicioActivo", true);
        
        return "debug-verification";
    }
}
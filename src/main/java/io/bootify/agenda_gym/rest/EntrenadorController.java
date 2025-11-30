package io.bootify.agenda_gym.rest;

import io.bootify.agenda_gym. domain.Rol;
import io.bootify. agenda_gym.domain.Usuario;
import io.bootify.agenda_gym.repos. UsuarioRepository;
import io. bootify.agenda_gym.service.RolService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory. annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/entrenador")
public class EntrenadorController {

    private final RolService rolService;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public EntrenadorController(RolService rolService, 
                                UsuarioRepository usuarioRepository) {
        this.rolService = rolService;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Panel del entrenador
     */
    @GetMapping
    public String panelEntrenador(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuarioLogueado = obtenerUsuarioLogueado(session);
        
        if (usuarioLogueado == null || !usuarioLogueado.tienePermisosEntrenador()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para acceder a esta sección");
            return "redirect:/";
        }

        List<Usuario> clientesAsignados = usuarioRepository.findByEntrenadorAsignadoId(usuarioLogueado.getId());
        List<Usuario> clientesConPermisoAgenda = usuarioRepository
                .findByEntrenadorAsignadoIdAndPermitirVerAgendaTrue(usuarioLogueado.getId());

        model.addAttribute("usuarioLogueado", usuarioLogueado);
        model.addAttribute("clientesAsignados", clientesAsignados);
        model.addAttribute("clientesConPermisoAgenda", clientesConPermisoAgenda);
        model.addAttribute("totalClientes", clientesAsignados.size());

        return "entrenador/panel";
    }

    /**
     * Ver agenda de un cliente (si tiene permiso)
     */
    @GetMapping("/cliente/{id}/agenda")
    public String verAgendaCliente(@PathVariable Long id, 
                                   Model model, 
                                   HttpSession session, 
                                   RedirectAttributes redirectAttributes) {
        Usuario entrenador = obtenerUsuarioLogueado(session);
        
        if (entrenador == null || ! entrenador.tienePermisosEntrenador()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para acceder a esta sección");
            return "redirect:/";
        }

        Usuario cliente = usuarioRepository.findById(id). orElse(null);
        
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado");
            return "redirect:/entrenador";
        }

        if (!rolService.puedeVerAgenda(entrenador, cliente)) {
            redirectAttributes. addFlashAttribute("error", "No tienes permiso para ver la agenda de este cliente");
            return "redirect:/entrenador";
        }

        model.addAttribute("usuarioLogueado", entrenador);
        model.addAttribute("cliente", cliente);
        // Usar la relación directa de la entidad
        model.addAttribute("calendarios", cliente.getUsuarioCalendarios());

        return "entrenador/agenda-cliente";
    }

    private Usuario obtenerUsuarioLogueado(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return null;
        }
        return usuarioRepository.findById(usuarioId).orElse(null);
    }
}
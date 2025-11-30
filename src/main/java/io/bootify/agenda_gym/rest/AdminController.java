package io. bootify.agenda_gym.rest;

import io.bootify. agenda_gym.domain.Rol;
import io.bootify. agenda_gym.domain.Usuario;
import io.bootify.agenda_gym.repos.UsuarioRepository;
import io.bootify.agenda_gym.service.RolService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation. Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final RolService rolService;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public AdminController(RolService rolService, UsuarioRepository usuarioRepository) {
        this.rolService = rolService;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Panel principal de administración
     */
    @GetMapping
    public String panelAdmin(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuarioLogueado = obtenerUsuarioLogueado(session);
        
        if (usuarioLogueado == null || !usuarioLogueado.tienePermisosAdmin()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para acceder a esta sección");
            return "redirect:/";
        }

        model.addAttribute("usuarioLogueado", usuarioLogueado);
        model.addAttribute("totalUsuarios", usuarioRepository.countByRolAndActivoTrue(Rol. USUARIO));
        model.addAttribute("totalEntrenadores", usuarioRepository.countByRolAndActivoTrue(Rol. ENTRENADOR));
        model. addAttribute("totalGerentes", usuarioRepository.countByRolAndActivoTrue(Rol. GERENTE));
        model. addAttribute("totalAdmins", usuarioRepository.countByRolAndActivoTrue(Rol.ADMIN));

        return "admin/panel";
    }

    /**
     * Gestión de usuarios (listar todos)
     */
    @GetMapping("/usuarios")
    public String gestionUsuarios(Model model, HttpSession session, 
                                  @RequestParam(required = false) String rol,
                                  RedirectAttributes redirectAttributes) {
        Usuario usuarioLogueado = obtenerUsuarioLogueado(session);
        
        if (usuarioLogueado == null || !usuarioLogueado.tienePermisosGerente()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para acceder a esta sección");
            return "redirect:/";
        }

        List<Usuario> usuarios;
        if (rol != null && !rol.isEmpty()) {
            usuarios = rolService.obtenerUsuariosPorRol(Rol.valueOf(rol.toUpperCase()));
        } else {
            usuarios = usuarioRepository.findAll();
        }

        model. addAttribute("usuarioLogueado", usuarioLogueado);
        model.addAttribute("usuarios", usuarios);
        model. addAttribute("roles", Rol.values());
        model.addAttribute("filtroRol", rol);

        return "admin/usuarios";
    }

    /**
     * Cambiar rol de usuario
     */
    @PostMapping("/usuarios/{id}/cambiar-rol")
    public String cambiarRol(@PathVariable Long id,
                             @RequestParam String nuevoRol,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Usuario usuarioLogueado = obtenerUsuarioLogueado(session);
        
        if (usuarioLogueado == null) {
            return "redirect:/registro? modo=login";
        }

        try {
            Rol rol = Rol.valueOf(nuevoRol.toUpperCase());
            rolService.cambiarRol(id, rol, usuarioLogueado);
            redirectAttributes.addFlashAttribute("success", "Rol actualizado correctamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/usuarios";
    }

    /**
     * Gestión de entrenadores
     */
    @GetMapping("/entrenadores")
    public String gestionEntrenadores(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuarioLogueado = obtenerUsuarioLogueado(session);
        
        if (usuarioLogueado == null || ! usuarioLogueado.tienePermisosGerente()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para acceder a esta sección");
            return "redirect:/";
        }

        model.addAttribute("usuarioLogueado", usuarioLogueado);
        model.addAttribute("entrenadores", rolService.obtenerEntrenadoresActivos());
        model.addAttribute("candidatos", rolService.obtenerCandidatosEntrenador());

        return "admin/entrenadores";
    }

    /**
     * Promover usuario a entrenador
     */
    @PostMapping("/entrenadores/promover/{id}")
    public String promoverAEntrenador(@PathVariable Long id,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes) {
        Usuario usuarioLogueado = obtenerUsuarioLogueado(session);
        
        if (usuarioLogueado == null || !usuarioLogueado.tienePermisosGerente()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para esta acción");
            return "redirect:/";
        }

        try {
            rolService.cambiarRol(id, Rol. ENTRENADOR, usuarioLogueado);
            redirectAttributes.addFlashAttribute("success", "Usuario promovido a entrenador correctamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/entrenadores";
    }

    /**
     * Gestión de gerentes (solo ADMIN)
     */
    @GetMapping("/gerentes")
    public String gestionGerentes(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuarioLogueado = obtenerUsuarioLogueado(session);
        
        if (usuarioLogueado == null || ! usuarioLogueado.tienePermisosAdmin()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para acceder a esta sección");
            return "redirect:/";
        }

        model.addAttribute("usuarioLogueado", usuarioLogueado);
        model.addAttribute("gerentes", rolService.obtenerGerentesActivos());
        model.addAttribute("candidatos", rolService.obtenerCandidatosGerente());

        return "admin/gerentes";
    }

    /**
     * Promover usuario a gerente
     */
    @PostMapping("/gerentes/promover/{id}")
    public String promoverAGerente(@PathVariable Long id,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Usuario usuarioLogueado = obtenerUsuarioLogueado(session);
        
        if (usuarioLogueado == null || !usuarioLogueado.tienePermisosAdmin()) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para esta acción");
            return "redirect:/";
        }

        try {
            rolService.cambiarRol(id, Rol.GERENTE, usuarioLogueado);
            redirectAttributes. addFlashAttribute("success", "Usuario promovido a gerente correctamente");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/gerentes";
    }

    /**
     * Obtener usuario logueado desde la sesión
     */
    private Usuario obtenerUsuarioLogueado(HttpSession session) {
        Long usuarioId = (Long) session. getAttribute("usuarioId");
        if (usuarioId == null) {
            return null;
        }
        return usuarioRepository.findById(usuarioId). orElse(null);
    }
}
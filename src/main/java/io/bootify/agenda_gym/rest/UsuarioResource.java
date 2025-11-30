package io.bootify.agenda_gym.rest;

import io.bootify.agenda_gym.domain.Usuario;
import io.bootify.agenda_gym.model.UsuarioDTO;
import io.bootify.agenda_gym.repos.UsuarioRepository;
import io.bootify.agenda_gym.service.UsuarioService;
import io.bootify.agenda_gym.service.VerificationService;
import io.bootify.agenda_gym.repos.ConfiguracionInicialRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping
public class UsuarioResource {

    private static final Logger log = LoggerFactory.getLogger(UsuarioResource.class);

    private final UsuarioService usuarioService;
    private final VerificationService verificationService;
    private final UsuarioRepository usuarioRepository;
    private final ConfiguracionInicialRepository configuracionInicialRepository;

    public UsuarioResource(final UsuarioService usuarioService,
                       final VerificationService verificationService,
                       final UsuarioRepository usuarioRepository,
                       final ConfiguracionInicialRepository configuracionInicialRepository) {
        this.usuarioService = usuarioService;
        this.verificationService = verificationService;
        this.usuarioRepository = usuarioRepository;
        this.configuracionInicialRepository = configuracionInicialRepository;
    }

    // ==============================
    // REST API
    // ==============================
    @GetMapping(path = "/api/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @GetMapping(path = "/api/usuarios/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UsuarioDTO> getUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.get(id));
    }

    @PostMapping(path = "/api/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Long> createUsuario(@RequestBody @Valid UsuarioDTO usuarioDTO) {
        log.info("POST /api/usuarios payload: nombre='{}' email='{}' fechaNacimiento='{}'",
                usuarioDTO.getNombre(), usuarioDTO.getEmail(), usuarioDTO.getFechaNacimiento());
        Long createdId = usuarioService.create(usuarioDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping(path = "/api/usuarios/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Void> updateUsuario(@PathVariable Long id, @RequestBody @Valid UsuarioDTO usuarioDTO) {
        usuarioService.update(id, usuarioDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/api/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/api/usuarios/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verifyAccountApi(@RequestParam("token") String token) {
        boolean verificado = verificationService.verificarToken(token);
        return ResponseEntity.ok(Map.of("verificado", verificado));
    }

    // ==============================
    // Registro web (TIENDA)
    // ==============================
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String fechaNacimiento,
            Model model
    ) {
        LocalDate fechaNac;
        try {
            fechaNac = LocalDate.parse(fechaNacimiento);
        } catch (Exception e) {
            model.addAttribute("error", "Fecha de nacimiento inválida");
            return "registro";
        }

        int edad = Period.between(fechaNac, LocalDate.now()).getYears();
        if (edad < 16) {
            model.addAttribute("error", "Debes tener al menos 16 años para registrarte.");
            return "registro";
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre(nombre);
        usuarioDTO.setEmail(email);
        usuarioDTO.setPassword(password);
        usuarioDTO.setFechaNacimiento(fechaNac);

        try {
            usuarioService.create(usuarioDTO);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al registrar el usuario");
            return "registro";
        }

        model.addAttribute("exito", "Registro exitoso. Revisa tu correo para verificar la cuenta.");
        return "registro";
    }

    // ==============================
    // Registro web (AGENDA)
    // ==============================
    @GetMapping("/registro-agenda")
    public String mostrarRegistroAgenda() {
        return "registro-agenda";
    }

    @PostMapping("/registro-agenda")
    public String registrarUsuarioAgenda(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String fechaNacimiento,
            Model model
    ) {
        LocalDate fechaNac;
        try {
            fechaNac = LocalDate.parse(fechaNacimiento);
        } catch (Exception e) {
            model.addAttribute("error", "Fecha de nacimiento inválida");
            return "registro-agenda";
        }

        int edad = Period.between(fechaNac, LocalDate.now()).getYears();
        if (edad < 16) {
            model.addAttribute("error", "Debes tener al menos 16 años para registrarte.");
            return "registro-agenda";
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre(nombre);
        usuarioDTO.setEmail(email);
        usuarioDTO.setPassword(password);
        usuarioDTO.setFechaNacimiento(fechaNac);

        try {
            usuarioService.createAgenda(usuarioDTO);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "registro-agenda";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al registrar el usuario");
            return "registro-agenda";
        }

        model.addAttribute("exito", "Registro exitoso. Revisa tu correo para verificar la cuenta y configurar tu agenda.");
        return "registro-agenda";
    }

    // ==============================
    // Login web
    // ==============================
    @GetMapping("/login")
    public String mostrarLogin() {
        return "registro";
    }

    @PostMapping("/login")
    public String loginUsuario(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        boolean valid;
        try {
            valid = usuarioService.validarLogin(email, password);
        } catch (Exception e) {
            valid = false;
        }

        if (!valid) {
            model.addAttribute("loginError", "Correo o contraseña incorrectos");
            return "registro";
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            // Verificar si la cuenta está activa
            if (usuario.getActivo() != null && !usuario.getActivo()) {
                model.addAttribute("loginError", "Tu cuenta ha sido desactivada. Contacta soporte para reactivarla.");
                return "registro";
            }
            
            session.setAttribute("usuarioLogueado", usuario);
            session.setAttribute("usuarioId", usuario.getId());
            session.setAttribute("usuarioNombre", usuario.getNombre());
            session.setAttribute("usuarioEmail", usuario.getEmail());
        }

        return "redirect:/";
    }

    // ==============================
    // Selector (después del login)
    // ==============================
    @GetMapping("/inicio")
    public String mostrarSelector(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        
        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("nombreUsuario", usuario.getNombre());
        model.addAttribute("usuario", usuario);
        
        return "selector";
    }

    // ==============================
    // Mi Perfil
    // ==============================
    @GetMapping("/mi-perfil")
    public String mostrarMiPerfil(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        
        if (usuario == null) {
            return "redirect:/registro?modo=login";
        }
        
        // Recargar usuario desde BD para tener datos actualizados
        Optional<Usuario> usuarioActualizado = usuarioRepository.findById(usuario.getId());
        if (usuarioActualizado.isPresent()) {
            usuario = usuarioActualizado.get();
            session.setAttribute("usuarioLogueado", usuario);
        }
        
        model.addAttribute("usuario", usuario);
        
        // Por ahora, valores en 0 (luego los calcularemos con las entidades reales)
        model.addAttribute("pedidosCount", 0);
        model.addAttribute("deseosCount", 0);
        model.addAttribute("historialCount", 0);
        model.addAttribute("carritoCount", 0);
        model.addAttribute("notificacionesCount", 0);
        
        // ========== VERIFICAR SI TIENE AGENDA ==========
        boolean tieneAgenda = configuracionInicialRepository.existsByUsuarioId(usuario.getId());
        model.addAttribute("tieneAgenda", tieneAgenda);
        // ===============================================
        
        return "mi-perfil";
    }

    // ==============================
    // Actualizar Perfil
    // ==============================
    @PostMapping("/mi-perfil/actualizar")
    public String actualizarPerfil(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String fechaNacimiento,
            HttpSession session,
            Model model
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        
        if (usuario == null) {
            return "redirect:/registro?modo=login";
        }
        
        try {
            // Actualizar datos
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setTelefono(telefono);
            
            if (fechaNacimiento != null && !fechaNacimiento.isEmpty()) {
                usuario.setFechaNacimiento(LocalDate.parse(fechaNacimiento));
            }
            
            usuarioRepository.save(usuario);
            
            // Actualizar sesión
            session.setAttribute("usuarioLogueado", usuario);
            session.setAttribute("usuarioNombre", usuario.getNombre());
            session.setAttribute("usuarioEmail", usuario.getEmail());
            
            return "redirect:/mi-perfil?exito=perfil";
            
        } catch (Exception e) {
            log.error("Error al actualizar perfil: ", e);
            return "redirect:/mi-perfil?error=perfil";
        }
    }

    // ==============================
    // Cambiar Contraseña
    // ==============================
    @PostMapping("/mi-perfil/cambiar-clave")
    public String cambiarClave(
            @RequestParam String claveActual,
            @RequestParam String claveNueva,
            @RequestParam String claveConfirmar,
            HttpSession session,
            Model model
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        
        if (usuario == null) {
            return "redirect:/registro?modo=login";
        }
        
        // Verificar que las nuevas claves coincidan
        if (!claveNueva.equals(claveConfirmar)) {
            return "redirect:/mi-perfil?error=claves-no-coinciden";
        }
        
        // Verificar clave actual
        try {
            boolean claveValida = usuarioService.validarLogin(usuario.getEmail(), claveActual);
            if (!claveValida) {
                return "redirect:/mi-perfil?error=clave-incorrecta";
            }
            
            // ✅ USAR EL SERVICIO para actualizar la contraseña (esto la encriptará)
            usuarioService.actualizarPassword(usuario.getId(), claveNueva);
            
            return "redirect:/mi-perfil?exito=clave";
            
        } catch (Exception e) {
            log.error("Error al cambiar contraseña: ", e);
            return "redirect:/mi-perfil?error=clave";
        }
    }

    // ==============================
    // Verificar Contraseña Actual (AJAX)
    // ==============================
    @PostMapping("/mi-perfil/verificar-clave-actual")
    @ResponseBody
    public Map<String, Object> verificarClaveActual(@RequestBody Map<String, String> request, 
                                                   HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        String claveIngresada = request.get("claveActual");
        
        if (usuario != null) {
            boolean claveValida = usuarioService.validarLogin(usuario.getEmail(), claveIngresada);
            response.put("valida", claveValida);
        } else {
            response.put("valida", false);
        }
        
        return response;
    }

    // ==============================
    // Eliminar (Desactivar) Cuenta
    // ==============================
    @PostMapping("/mi-perfil/eliminar")
    public String eliminarCuenta(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        
        if (usuario == null) {
            return "redirect:/registro?modo=login";
        }
        
        try {
            // Desactivar cuenta (no eliminar)
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
            
            // Cerrar sesión
            session.invalidate();
            
            return "redirect:/?cuenta=eliminada";
            
        } catch (Exception e) {
            log.error("Error al desactivar cuenta: ", e);
            return "redirect:/mi-perfil?error=eliminar";
        }
    }

    // ==============================
    // Logout
    // ==============================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
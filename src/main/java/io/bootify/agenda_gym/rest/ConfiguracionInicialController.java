package io.bootify. agenda_gym.rest;

import io.bootify. agenda_gym.domain.ConfiguracionInicial;
import io. bootify.agenda_gym.domain.Usuario;
import io.bootify. agenda_gym.model.ConfiguracionInicialDTO;
import io.bootify.agenda_gym.repos.ConfiguracionInicialRepository;
import io.bootify. agenda_gym.repos.UsuarioRepository;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation. GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework. web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class ConfiguracionInicialController {

    private final UsuarioRepository usuarioRepository;
    private final ConfiguracionInicialRepository configuracionInicialRepository;

    public ConfiguracionInicialController(UsuarioRepository usuarioRepository,
                                          ConfiguracionInicialRepository configuracionInicialRepository) {
        this.usuarioRepository = usuarioRepository;
        this.configuracionInicialRepository = configuracionInicialRepository;
    }

    /**
 * Muestra el formulario usando la sesión (sin parámetro en URL)
 */
@GetMapping("/formulario")
public String mostrarFormularioDesdeSession(HttpSession session, Model model) {
    System.out.println("=== MOSTRANDO FORMULARIO DESDE SESIÓN ===");
    
    Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
    
    if (usuario == null) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return "redirect:/registro-agenda";
        }
        return "redirect:/configuracion-inicial? usuarioId=" + usuarioId;
    }
    
    Long usuarioId = usuario.getId();
    System.out.println("Usuario ID desde sesión: " + usuarioId);
    
    // Verificar si ya tiene configuración
    if (configuracionInicialRepository.existsByUsuarioId(usuarioId)) {
        System.out.println("Usuario ya tiene configuración inicial, redirigiendo al dashboard");
        return "redirect:/dashboard";
    }
    
    ConfiguracionInicialDTO dto = new ConfiguracionInicialDTO();
    dto.setUsuarioId(usuarioId);
    model.addAttribute("configuracionInicial", dto);
    
    return "formulario";
}

    /**
     * Recibe el formulario de configuracion inicial desde bienvenida (POST)
     */
    @PostMapping("/configuracion-inicial/obtener-usuario")
    public String obtenerUsuario(@RequestParam(required = false) Long usuarioId, Model model) {
        System. out.println("=== POST OBTENER USUARIO ===");
        System.out.println("Usuario ID recibido: " + usuarioId);

        if (usuarioId == null) {
            model.addAttribute("estado", "error");
            model.addAttribute("mensaje", "No se pudo identificar el usuario.");
            return "verificacion";
        }

        return "redirect:/configuracion-inicial?usuarioId=" + usuarioId;
    }

    /**
     * Guarda la configuracion inicial del usuario
     */
    @PostMapping("/configuracion-inicial/nuevo-usuario")
    public String guardarConfiguracion(ConfiguracionInicialDTO dto, Model model) {
        System.out.println("=== GUARDANDO CONFIGURACION INICIAL ===");
        System.out.println("Usuario ID: " + dto.getUsuarioId());
        
        // DEBUG: Mostrar datos recibidos
        System.out.println("Objetivos: " + dto.getObjetivosPrincipales());
        System.out.println("Config días: " + dto.getConfiguracionDias());
        System. out.println("Hábitos metas: " + dto.getHabitosMetas());
        System.out.println("Calorías actual: " + dto.getCaloriasActual());
        System. out.println("Calorías meta: " + dto. getCaloriasMeta());

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(dto.getUsuarioId());
        if (usuarioOpt.isEmpty()) {
            model.addAttribute("estado", "error");
            model.addAttribute("mensaje", "Usuario no encontrado.");
            return "verificacion";
        }

        Usuario usuario = usuarioOpt.get();

        // Crear la entidad ConfiguracionInicial
        ConfiguracionInicial config = new ConfiguracionInicial();
        config.setUsuario(usuario);

        // OBJETIVOS Y CONFIGURACIÓN (JSON)
        config.setObjetivosPrincipales(dto.getObjetivosPrincipales());
        config.setConfiguracionDias(dto.getConfiguracionDias());
        config.setHabitosMetas(dto.getHabitosMetas());
        
        // CALORÍAS
        config.setCaloriasActual(dto.getCaloriasActual());
        config.setCaloriasMeta(dto.getCaloriasMeta());
        config.setObjetivoCalorias(dto.getObjetivoCalorias());
        
        // PESO Y FECHA META
        config.setPesoInicial(dto.getPesoInicial());
        config.setMetaPeso(dto.getMetaPeso());
        config.setMetaFecha(dto.getMetaFecha());
        
        // MEDIDAS - BRAZO
        config.setMedidaBrazoActual(dto.getMedidaBrazoActual());
        config.setMedidaBrazoObjetivo(dto.getMedidaBrazoObjetivo());
        config.setMetaBrazo(dto.getMetaBrazo());
        
        // MEDIDAS - ABDOMEN
        config.setMedidaAbdomenActual(dto.getMedidaAbdomenActual());
        config. setMedidaAbdomenObjetivo(dto.getMedidaAbdomenObjetivo());
        config.setMetaAbdomen(dto.getMetaAbdomen());
        
        // MEDIDAS - CINTURA
        config.setMedidaCinturaActual(dto.getMedidaCinturaActual());
        config.setMedidaCinturaObjetivo(dto.getMedidaCinturaObjetivo());
        config.setMetaCintura(dto.getMetaCintura());
        
        // MEDIDAS - CADERA
        config.setMedidaCaderaActual(dto.getMedidaCaderaActual());
        config. setMedidaCaderaObjetivo(dto.getMedidaCaderaObjetivo());
        config.setMetaCadera(dto.getMetaCadera());
        
        // MEDIDAS - MUSLO
        config.setMedidaMusloActual(dto.getMedidaMusloActual());
        config.setMedidaMusloObjetivo(dto.getMedidaMusloObjetivo());
        config.setMetaMuslo(dto.getMetaMuslo());
        
        // MEDIDAS - PECHO
        config.setMedidaPechoActual(dto.getMedidaPechoActual());
        config.setMedidaPechoObjetivo(dto.getMedidaPechoObjetivo());
        config.setMetaPecho(dto.getMetaPecho());
        
        // MEDIDAS - GLÚTEO
        config.setMedidaGluteoActual(dto.getMedidaGluteoActual());
        config.setMedidaGluteoObjetivo(dto. getMedidaGluteoObjetivo());
        config. setMetaGluteo(dto.getMetaGluteo());

        // HÁBITOS ALIMENTICIOS
        config.setDesayunoHabito(dto.getDesayunoHabito());
        config.setAlmuerzoHabito(dto.getAlmuerzoHabito());
        config.setCenaHabito(dto.getCenaHabito());
        config. setSnackHabito(dto.getSnackHabito());
        
        // BIENESTAR
        config.setHidratacionActual(dto.getHidratacionActual());
        config.setSuenioActual(dto.getSuenioActual());
        config.setAnimoActual(dto.getAnimoActual());

        // Guardar en la base de datos
        try {
            ConfiguracionInicial configGuardada = configuracionInicialRepository.save(config);
            System.out.println("✅ Configuracion guardada exitosamente con ID: " + configGuardada.getId());
            
            return "redirect:/dashboard";
        } catch (Exception e) {
            System.out.println("❌ Error al guardar configuración: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("estado", "error");
            model.addAttribute("mensaje", "Error al guardar la configuración: " + e.getMessage());
            return "verificacion";
        }
    }
}
package io.bootify.agenda_gym. service;

import io.bootify.agenda_gym.domain.RecuperacionClave;
import io.bootify.agenda_gym. domain.Usuario;
import io. bootify.agenda_gym.events.BeforeDeleteUsuario;
import io.bootify. agenda_gym.model.RecuperacionClaveDTO;
import io.bootify.agenda_gym.repos.RecuperacionClaveRepository;
import io.bootify.agenda_gym.repos. UsuarioRepository;
import io. bootify.agenda_gym.util. NotFoundException;
import io.bootify.agenda_gym. util.ReferencedException;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.time. OffsetDateTime;
import java. util. List;
import java.util.Optional;

@Service
public class RecuperacionClaveService {

    private static final Logger log = LoggerFactory.getLogger(RecuperacionClaveService.class);

    private final RecuperacionClaveRepository recuperacionClaveRepository;
    private final UsuarioRepository usuarioRepository;
    private final JavaMailSender mailSender;

    public RecuperacionClaveService(final RecuperacionClaveRepository recuperacionClaveRepository,
                                    final UsuarioRepository usuarioRepository,
                                    final JavaMailSender mailSender) {
        this.recuperacionClaveRepository = recuperacionClaveRepository;
        this.usuarioRepository = usuarioRepository;
        this.mailSender = mailSender;
    }

    /**
     * Genera un código de 6 dígitos y lo envía al correo del usuario
     */
    @Transactional
    public boolean enviarCodigoRecuperacion(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (usuarioOpt. isEmpty()) {
            log.warn("Intento de recuperación para correo no registrado: {}", email);
            // Por seguridad, no revelamos si el correo existe o no
            return true;
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Verificar si la cuenta está activa
        if (usuario.getActivo() != null && !usuario.getActivo()) {
            log.warn("Intento de recuperación para cuenta desactivada: {}", email);
            return true;
        }
        
        // Generar código de 6 dígitos
        String codigo = generarCodigo6Digitos();
        
        // Invalidar códigos anteriores del usuario
        Optional<RecuperacionClave> codigoAnterior = recuperacionClaveRepository
                .findFirstByUsuarioAndUsadoFalseOrderByFechaExpiracionDesc(usuario);
        codigoAnterior.ifPresent(rc -> {
            rc.setUsado(true);
            recuperacionClaveRepository.save(rc);
        });
        
        // Crear nuevo registro
        RecuperacionClave recuperacion = new RecuperacionClave();
        recuperacion.setUsuario(usuario);
        recuperacion. setToken(codigo);
        recuperacion. setFechaSolicitud(OffsetDateTime.now());
        recuperacion.setFechaExpiracion(OffsetDateTime.now().plusMinutes(15)); // Expira en 15 min
        recuperacion.setUsado(false);
        
        recuperacionClaveRepository.save(recuperacion);
        
        // Enviar correo
        enviarCorreoCodigo(email, codigo, usuario. getNombre());
        
        log.info("✅ Código de recuperación enviado a: {}", email);
        return true;
    }

    /**
     * Verifica si el código es válido
     */
    public boolean verificarCodigo(String email, String codigo) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        
        if (usuarioOpt.isEmpty()) {
            return false;
        }
        
        Usuario usuario = usuarioOpt.get();
        
        Optional<RecuperacionClave> recuperacionOpt = recuperacionClaveRepository
                .findFirstByUsuarioAndUsadoFalseOrderByFechaExpiracionDesc(usuario);
        
        if (recuperacionOpt.isEmpty()) {
            log.warn("No hay código pendiente para: {}", email);
            return false;
        }
        
        RecuperacionClave recuperacion = recuperacionOpt.get();
        
        // Verificar si expiró
        if (recuperacion.getFechaExpiracion(). isBefore(OffsetDateTime.now())) {
            log.warn("Código expirado para: {}", email);
            return false;
        }
        
        // Verificar código
        if (!recuperacion.getToken().equals(codigo)) {
            log.warn("Código incorrecto para: {}", email);
            return false;
        }
        
        log.info("✅ Código verificado correctamente para: {}", email);
        return true;
    }

    /**
     * Cambia la contraseña después de verificar el código
     */
    @Transactional
    public boolean cambiarPassword(String email, String codigo, String nuevaPassword) {
        // Primero verificar el código
        if (!verificarCodigo(email, codigo)) {
            return false;
        }
        
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return false;
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Marcar código como usado
        Optional<RecuperacionClave> recuperacionOpt = recuperacionClaveRepository
                .findFirstByUsuarioAndUsadoFalseOrderByFechaExpiracionDesc(usuario);
        
        recuperacionOpt.ifPresent(rc -> {
            rc.setUsado(true);
            recuperacionClaveRepository.save(rc);
        });
        
        // Cambiar contraseña (usando BCrypt)
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(nuevaPassword, org.mindrot.jbcrypt.BCrypt.gensalt(12));
        usuario.setPassword(hashedPassword);
        usuarioRepository.save(usuario);
        
        log.info("✅ Contraseña actualizada para: {}", email);
        return true;
    }

    /**
     * Genera código aleatorio de 6 dígitos
     */
    private String generarCodigo6Digitos() {
        SecureRandom random = new SecureRandom();
        int codigo = 100000 + random.nextInt(900000); // Entre 100000 y 999999
        return String.valueOf(codigo);
    }

    /**
     * Envía el correo con el código
     */
    private void enviarCorreoCodigo(String email, String codigo, String nombre) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(email);
            mensaje.setSubject("🔐 Código de recuperación - GymTrack");
            mensaje.setText(
                "¡Hola " + nombre + "!\n\n" +
                "Recibimos una solicitud para restablecer tu contraseña.\n\n" +
                "Tu código de verificación es:\n\n" +
                "        " + codigo + "\n\n" +
                "Este código expira en 15 minutos.\n\n" +
                "Si no solicitaste este cambio, ignora este correo.\n\n" +
                "Saludos,\n" +
                "El equipo de GymTrack 💪"
            );
            
            mailSender.send(mensaje);
            log.info("📧 Correo de recuperación enviado a: {}", email);
        } catch (Exception e) {
            log.error("❌ Error al enviar correo de recuperación: {}", e. getMessage());
        }
    }

    // ========== MÉTODOS ORIGINALES ==========

    public List<RecuperacionClaveDTO> findAll() {
        final List<RecuperacionClave> recuperacionClaves = recuperacionClaveRepository.findAll(Sort.by("id"));
        return recuperacionClaves.stream()
                .map(recuperacionClave -> mapToDTO(recuperacionClave, new RecuperacionClaveDTO()))
                .toList();
    }

    public RecuperacionClaveDTO get(final Long id) {
        return recuperacionClaveRepository.findById(id)
                .map(recuperacionClave -> mapToDTO(recuperacionClave, new RecuperacionClaveDTO()))
                . orElseThrow(NotFoundException::new);
    }

    public Long create(final RecuperacionClaveDTO recuperacionClaveDTO) {
        final RecuperacionClave recuperacionClave = new RecuperacionClave();
        mapToEntity(recuperacionClaveDTO, recuperacionClave);
        return recuperacionClaveRepository.save(recuperacionClave).getId();
    }

    public void update(final Long id, final RecuperacionClaveDTO recuperacionClaveDTO) {
        final RecuperacionClave recuperacionClave = recuperacionClaveRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(recuperacionClaveDTO, recuperacionClave);
        recuperacionClaveRepository.save(recuperacionClave);
    }

    public void delete(final Long id) {
        final RecuperacionClave recuperacionClave = recuperacionClaveRepository. findById(id)
                . orElseThrow(NotFoundException::new);
        recuperacionClaveRepository.delete(recuperacionClave);
    }

    private RecuperacionClaveDTO mapToDTO(final RecuperacionClave recuperacionClave,
                                          final RecuperacionClaveDTO recuperacionClaveDTO) {
        recuperacionClaveDTO.setId(recuperacionClave.getId());
        recuperacionClaveDTO.setToken(recuperacionClave.getToken());
        recuperacionClaveDTO.setFechaSolicitud(recuperacionClave.getFechaSolicitud());
        recuperacionClaveDTO. setFechaExpiracion(recuperacionClave.getFechaExpiracion());
        recuperacionClaveDTO.setUsado(recuperacionClave.getUsado());
        recuperacionClaveDTO.setUsuario(recuperacionClave.getUsuario() == null ? null : recuperacionClave.getUsuario().getId());
        return recuperacionClaveDTO;
    }

    private RecuperacionClave mapToEntity(final RecuperacionClaveDTO recuperacionClaveDTO,
                                          final RecuperacionClave recuperacionClave) {
        recuperacionClave.setToken(recuperacionClaveDTO.getToken());
        recuperacionClave.setFechaSolicitud(recuperacionClaveDTO.getFechaSolicitud());
        recuperacionClave.setFechaExpiracion(recuperacionClaveDTO. getFechaExpiracion());
        recuperacionClave.setUsado(recuperacionClaveDTO. getUsado());
        final Usuario usuario = recuperacionClaveDTO.getUsuario() == null ? null : usuarioRepository. findById(recuperacionClaveDTO.getUsuario())
                . orElseThrow(() -> new NotFoundException("usuario not found"));
        recuperacionClave.setUsuario(usuario);
        return recuperacionClave;
    }

    @EventListener(BeforeDeleteUsuario.class)
    public void on(final BeforeDeleteUsuario event) {
        final ReferencedException referencedException = new ReferencedException();
        final RecuperacionClave usuarioRecuperacionClave = recuperacionClaveRepository. findFirstByUsuarioId(event.getId());
        if (usuarioRecuperacionClave != null) {
            referencedException.setKey("usuario. recuperacionClave.usuario.referenced");
            referencedException.addParam(usuarioRecuperacionClave.getId());
            throw referencedException;
        }
    }
}
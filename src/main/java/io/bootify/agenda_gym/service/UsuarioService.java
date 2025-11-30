package io.bootify.agenda_gym.service;

import io.bootify.agenda_gym.domain.Usuario;
import io.bootify. agenda_gym.events.BeforeDeleteUsuario;
import io.bootify.agenda_gym.model.UsuarioDTO;
import io.bootify.agenda_gym.repos.UsuarioRepository;
import io.bootify.agenda_gym.util.NotFoundException;
import org. springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework. data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.time.LocalDate;
import java. time.Period;
import java. util.List;
import java. util.Optional;
import java. util.Set;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    private static final Logger log = LoggerFactory. getLogger(UsuarioService. class);

    private final UsuarioRepository usuarioRepository;
    private final ApplicationEventPublisher publisher;
    private final VerificationService verificationService;

    @Autowired
    public UsuarioService(final UsuarioRepository usuarioRepository,
                          final ApplicationEventPublisher publisher,
                          final VerificationService verificationService) {
        this.usuarioRepository = usuarioRepository;
        this.publisher = publisher;
        this.verificationService = verificationService;
    }

    public List<UsuarioDTO> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll(Sort.by("id"));
        return usuarios.stream()
                .map(u -> mapToDTO(u, new UsuarioDTO()))
                . toList();
    }

    public UsuarioDTO get(final Long id) {
        return usuarioRepository.findById(id)
                .map(u -> mapToDTO(u, new UsuarioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    /**
     * Crear usuario para TIENDA (registro normal)
     * Si el correo existe pero está desactivado, reactiva la cuenta y actualiza datos
     */
    @Transactional
    public Long create(final UsuarioDTO usuarioDTO) {
        validarUsuarioDTO(usuarioDTO);

        // Verificar si existe el correo
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuarioDTO.getEmail());
        
        if (usuarioExistente. isPresent()) {
            Usuario usuario = usuarioExistente. get();
            
            // Si la cuenta está ACTIVA, no puede registrarse
            if (usuario.getActivo() != null && usuario.getActivo()) {
                throw new IllegalArgumentException("Ya existe un usuario con ese correo");
            }
            
            // Si la cuenta está DESACTIVADA, reactivar y actualizar datos
            System. out.println("=== REACTIVANDO CUENTA DESACTIVADA ===");
            System.out. println("Email: " + usuario. getEmail());
            
            usuario.setNombre(usuarioDTO.getNombre());
            usuario. setPassword(hashPassword(usuarioDTO.getPassword()));
            usuario.setFechaNacimiento(usuarioDTO.getFechaNacimiento());
            usuario.setActivo(true);
            usuario. setVerificado(false);
            usuario.setFotoUrl(null);
            usuario. setTelefono(null);
            
            usuarioRepository.save(usuario);
            
            // Enviar nuevo correo de verificación
            verificationService.crearYEnviarToken(usuario, false);
            
            System.out.println("✅ Cuenta reactivada con ID: " + usuario.getId());
            return usuario.getId();
        }

        // Si no existe, crear nuevo usuario
        final Usuario usuario = new Usuario();
        mapToEntity(usuarioDTO, usuario);

        usuario.setVerificado(false);
        usuario.setActivo(true);
        usuario.setPassword(hashPassword(usuarioDTO. getPassword()));

        if (usuario.getFechaRegistro() == null) {
            usuario.setFechaRegistro(OffsetDateTime.now());
        }

        System.out.println("=== GUARDANDO USUARIO EN BD ===");
        System. out.println("Email: " + usuario.getEmail());
        System.out.println("Nombre: " + usuario.getNombre());
        System.out.println("Fecha Nacimiento: " + usuario.getFechaNacimiento());

        try {
            final Usuario nuevoUsuario = usuarioRepository. save(usuario);
            System. out.println("✅ Usuario guardado con ID: " + nuevoUsuario.getId());
            
            System.out.println("=== INICIANDO CREACIÓN DE TOKEN (TIENDA) ===");
            verificationService.crearYEnviarToken(nuevoUsuario, false);
            System. out.println("✅ Proceso de verificación iniciado");

            return nuevoUsuario.getId();
        } catch (DataIntegrityViolationException ex) {
            System.out.println("❌ Error de integridad al guardar usuario: " + ex.getMessage());
            log. error("Error de integridad al guardar usuario: {}", ex. getMessage(), ex);
            throw ex;
        } catch (Exception e) {
            System.out. println("❌ Error general al guardar usuario: " + e. getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Crear usuario para AGENDA (envía email con enlace a verificación de agenda)
     * Si el correo existe pero está desactivado, reactiva la cuenta y actualiza datos
     */
    @Transactional
    public Long createAgenda(final UsuarioDTO usuarioDTO) {
        validarUsuarioDTO(usuarioDTO);

        // Verificar si existe el correo
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuarioDTO.getEmail());
        
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            
            // Si la cuenta está ACTIVA, no puede registrarse
            if (usuario.getActivo() != null && usuario.getActivo()) {
                throw new IllegalArgumentException("Ya existe un usuario con ese correo");
            }
            
            // Si la cuenta está DESACTIVADA, reactivar y actualizar datos
            System. out.println("=== REACTIVANDO CUENTA DESACTIVADA (AGENDA) ===");
            System.out.println("Email: " + usuario.getEmail());
            
            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setPassword(hashPassword(usuarioDTO.getPassword()));
            usuario.setFechaNacimiento(usuarioDTO. getFechaNacimiento());
            usuario.setActivo(true);
            usuario.setVerificado(false);
            usuario.setFotoUrl(null);
            usuario.setTelefono(null);
            
            usuarioRepository.save(usuario);
            
            // Enviar nuevo correo de verificación (AGENDA)
            verificationService.crearYEnviarToken(usuario, true);
            
            System.out.println("✅ Cuenta reactivada (AGENDA) con ID: " + usuario.getId());
            return usuario.getId();
        }

        // Si no existe, crear nuevo usuario
        final Usuario usuario = new Usuario();
        mapToEntity(usuarioDTO, usuario);

        usuario.setVerificado(false);
        usuario.setActivo(true);
        usuario.setPassword(hashPassword(usuarioDTO. getPassword()));

        if (usuario.getFechaRegistro() == null) {
            usuario.setFechaRegistro(OffsetDateTime.now());
        }

        System.out.println("=== GUARDANDO USUARIO EN BD (AGENDA) ===");
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Nombre: " + usuario. getNombre());
        System.out.println("Fecha Nacimiento: " + usuario.getFechaNacimiento());

        try {
            final Usuario nuevoUsuario = usuarioRepository.save(usuario);
            System.out.println("✅ Usuario guardado con ID: " + nuevoUsuario. getId());
            
            System. out.println("=== INICIANDO CREACIÓN DE TOKEN (AGENDA) ===");
            verificationService.crearYEnviarToken(nuevoUsuario, true);
            System.out. println("✅ Proceso de verificación AGENDA iniciado");

            return nuevoUsuario.getId();
        } catch (DataIntegrityViolationException ex) {
            System.out.println("❌ Error de integridad al guardar usuario: " + ex.getMessage());
            log.error("Error de integridad al guardar usuario: {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception e) {
            System.out.println("❌ Error general al guardar usuario: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public void update(final Long id, final UsuarioDTO usuarioDTO) {
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        if (usuarioDTO.getNombre() != null) {
            Pattern nombrePattern = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,100}$");
            if (!nombrePattern.matcher(usuarioDTO. getNombre()).matches()) {
                throw new IllegalArgumentException("Nombre inválido");
            }
            usuario.setNombre(usuarioDTO.getNombre());
        }

        if (usuarioDTO. getEmail() != null) {
            if (usuarioDTO.getEmail().isBlank()) {
                throw new IllegalArgumentException("El correo es obligatorio");
            }
            Pattern emailPattern = Pattern.compile("^[A-Za-z0-9. _%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
            if (!emailPattern.matcher(usuarioDTO. getEmail()).matches()) {
                throw new IllegalArgumentException("Correo inválido");
            }
            if (! usuario.getEmail().equals(usuarioDTO.getEmail()) &&
                    usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
                throw new IllegalArgumentException("Ya existe un usuario con ese correo");
            }
            usuario.setEmail(usuarioDTO.getEmail());
        }

        if (usuarioDTO.getFechaNacimiento() != null) {
            LocalDate fechaNac = usuarioDTO.getFechaNacimiento();
            LocalDate hoy = LocalDate.now();
            int edad = Period.between(fechaNac, hoy).getYears();
            
            if (edad < 16) {
                throw new IllegalArgumentException("El usuario debe tener al menos 16 años");
            }
            
            if (edad > 75) {
                throw new IllegalArgumentException("La edad máxima permitida es 75 años");
            }
            usuario.setFechaNacimiento(fechaNac);
        }

        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword(). isEmpty()) {
            if (usuarioDTO.getPassword(). length() < 6) {
                throw new IllegalArgumentException("Contraseña inválida, mínimo 6 caracteres");
            }
            usuario.setPassword(hashPassword(usuarioDTO.getPassword()));
        }

        usuarioRepository.save(usuario);
    }

    /**
     * Actualizar contraseña del usuario
     */
    @Transactional
    public void actualizarPassword(final Long id, final String nuevaPassword) {
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        
        validarPassword(nuevaPassword);
        
        usuario.setPassword(hashPassword(nuevaPassword));
        usuarioRepository.save(usuario);
        
        log.info("✅ Contraseña actualizada para usuario ID: {}", id);
    }

    /**
     * Actualizar foto de perfil del usuario
     */
    @Transactional
    public void actualizarFoto(final Long id, final String fotoUrl) {
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        
        usuario.setFotoUrl(fotoUrl);
        usuarioRepository.save(usuario);
        
        log. info("✅ Foto de perfil actualizada para usuario ID: {}", id);
    }

    /**
     * Desactivar cuenta del usuario (no elimina de la BD)
     */
    @Transactional
    public void desactivarCuenta(final Long id) {
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        
        log.info("⚠️ Cuenta desactivada para usuario ID: {}", id);
    }

    @Transactional
    public void delete(final Long id) {
        final Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteUsuario(id));
        usuarioRepository.delete(usuario);
    }

    public boolean validarLogin(String email, String password) {
        final Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(NotFoundException::new);
        
        // Verificar si la cuenta está activa
        if (usuario.getActivo() != null && !usuario.getActivo()) {
            throw new IllegalStateException("La cuenta está desactivada");
        }
        
        return checkPassword(password, usuario.getPassword());
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    private boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    /**
     * Validar requisitos de contraseña
     */
    private void validarPassword(String pwd) {
        if (pwd == null) throw new IllegalArgumentException("La contraseña es obligatoria");
        if (pwd.length() < 12) throw new IllegalArgumentException("La contraseña debe tener al menos 12 caracteres");
        if (!pwd.chars().anyMatch(Character::isUpperCase)) throw new IllegalArgumentException("La contraseña debe contener al menos una letra MAYÚSCULA");
        if (!pwd.chars().anyMatch(Character::isLowerCase)) throw new IllegalArgumentException("La contraseña debe contener al menos una letra minúscula");
        if (!pwd.chars().anyMatch(Character::isDigit)) throw new IllegalArgumentException("La contraseña debe contener al menos un número");
        if (!pwd.matches(".*[! @#$%^&*(),.?\":{}|<>_\\-\\[\\]\\\\/~`+=;:].*")) throw new IllegalArgumentException("La contraseña debe contener al menos un símbolo");
        
        Set<String> comunes = Set.of("123456","12345678","password","qwerty","abc123","111111","1234567890","iloveyou","admin","welcome","monkey","000000","password1");
        if (comunes.contains(pwd. toLowerCase())) throw new IllegalArgumentException("Contraseña demasiado común");
    }

    private UsuarioDTO mapToDTO(final Usuario usuario, final UsuarioDTO usuarioDTO) {
        usuarioDTO.setId(usuario. getId());
        usuarioDTO. setNombre(usuario.getNombre());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setPassword(null);
        usuarioDTO.setFechaNacimiento(usuario.getFechaNacimiento());
        usuarioDTO.setFechaRegistro(usuario.getFechaRegistro());
        return usuarioDTO;
    }

    private Usuario mapToEntity(final UsuarioDTO dto, final Usuario usuario) {
        if (dto.getNombre() != null) usuario.setNombre(dto.getNombre());
        if (dto. getEmail() != null) usuario. setEmail(dto.getEmail());
        if (dto.getFechaNacimiento() != null) usuario.setFechaNacimiento(dto.getFechaNacimiento());
        if (dto.getFechaRegistro() != null) usuario.setFechaRegistro(dto.getFechaRegistro());
        return usuario;
    }

    private void validarUsuarioDTO(UsuarioDTO dto) {
        if (dto. getNombre() == null || dto.getNombre().isBlank()) throw new IllegalArgumentException("El nombre de usuario es obligatorio");
        String nombre = dto.getNombre(). trim();
        if (nombre. length() < 3 || nombre.length() > 30) throw new IllegalArgumentException("El nombre debe tener entre 3 y 30 caracteres");
        Pattern nombrePattern = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s']+$");
        if (!nombrePattern.matcher(nombre).matches()) throw new IllegalArgumentException("El nombre solo puede contener letras, espacios y apóstrofes");

        if (dto.getEmail() == null || dto.getEmail(). isBlank()) throw new IllegalArgumentException("El correo es obligatorio");
        String email = dto.getEmail().trim();
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", Pattern.CASE_INSENSITIVE);
        if (!emailPattern.matcher(email).matches()) throw new IllegalArgumentException("Correo inválido");

        validarPassword(dto.getPassword());

        if (dto.getFechaNacimiento() == null) throw new IllegalArgumentException("La fecha de nacimiento es obligatoria");
        LocalDate fechaNac = dto. getFechaNacimiento();
        LocalDate hoy = LocalDate. now();
        int edad = Period.between(fechaNac, hoy).getYears();
        
        if (edad < 16) throw new IllegalArgumentException("Debes tener al menos 16 años");
        if (edad > 75) throw new IllegalArgumentException("La edad máxima permitida es 75 años");
    }
}
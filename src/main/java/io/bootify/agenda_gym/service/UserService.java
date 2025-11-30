package io.bootify.agenda_gym.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.bootify.agenda_gym.domain.Usuario;

/**
 * UserService "compatibilidad": expone operaciones que el controlador necesita.
 *
 * Actualmente es un stub que debes implementar conectándolo a tu repositorio real
 * (UserRepository). Las dos operaciones principales son:
 *  - verifyPassword(username, plainPassword) : boolean
 *  - changePassword(username, currentPlain, newPlain) : boolean
 *
 * Ejemplos de TODO dentro del código indican dónde conectar tu UserRepository y cómo
 * usar PasswordEncoder para comparar/guardar hashes.
 */
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    public UserService(final PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Buscar usuario por username/email.
     * Implementa esto usando tu repositorio (ej. userRepository.findByEmail(username)).
     * Aquí devolvemos null por defecto (stub) para que la app arranque; reemplaza con la implementación real.
     */
    public Usuario findByEmail(final String username) {
        // TODO: Reemplazar por llamada real a UserRepository
        return null;
    }

    /**
     * Verifica si la contraseña en texto plano coincide con la almacenada para el usuario.
     * Implementa con tu repository y passwordEncoder.matches.
     *
     * Ejemplo (cuando tengas userRepository):
     *   Usuario u = userRepository.findByEmail(username);
     *   if (u == null) return false;
     *   return passwordEncoder.matches(plainPassword, u.getPasswordHash());
     */
    public boolean verifyPassword(final String username, final String plainPassword) {
        // TODO: reemplazar: consultar DB y comparar hashes
        // Forma segura: usar passwordEncoder.matches(plainPassword, storedHash)
        // Por ahora devuelve false (no verificado) para que no falle el arranque.
        return false;
    }

    /**
     * Cambia la contraseña del usuario: primero verifica la contraseña actual, luego guarda el hash nuevo.
     * Retorna true si se actualizó correctamente.
     *
     * Ejemplo (cuando implementes con repo):
     *   Usuario u = userRepository.findByEmail(username);
     *   if (u == null) return false;
     *   if (!passwordEncoder.matches(currentPlain, u.getPasswordHash())) return false;
     *   u.setPasswordHash(passwordEncoder.encode(newPlain));
     *   userRepository.save(u);
     *   return true;
     */
    public boolean changePassword(final String username, final String currentPlain, final String newPlain) {
        // TODO: implementar actualización real usando tu repositorio
        return false;
    }

    /**
     * Guardar usuario (placeholder). Reemplazar por userRepository.save(u) en implementación real.
     */
    public Usuario save(final Usuario usuario) {
        // TODO: persistir usando repository
        return usuario;
    }
}
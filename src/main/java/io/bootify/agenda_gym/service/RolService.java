package io.bootify.agenda_gym.service;

import io. bootify.agenda_gym.domain.Rol;
import io. bootify.agenda_gym.domain.Usuario;
import io.bootify.agenda_gym.repos. UsuarioRepository;
import io.bootify.agenda_gym. util. NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util. List;

@Service
public class RolService {

    private static final Logger log = LoggerFactory.getLogger(RolService.class);

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public RolService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Cambiar rol de un usuario
     * Solo ADMIN puede asignar rol GERENTE
     * ADMIN y GERENTE pueden asignar rol ENTRENADOR
     */
    @Transactional
    public void cambiarRol(Long usuarioId, Rol nuevoRol, Usuario solicitante) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                . orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // Validar permisos
        validarPermisosCambioRol(solicitante, nuevoRol);

        // No permitir que un usuario se cambie el rol a sí mismo
        if (usuario. getId().equals(solicitante. getId())) {
            throw new IllegalArgumentException("No puedes cambiar tu propio rol");
        }

        // No permitir degradar a un ADMIN
        if (usuario.getRol() == Rol.ADMIN && solicitante.getRol() != Rol.ADMIN) {
            throw new IllegalArgumentException("No tienes permisos para modificar a un administrador");
        }

        Rol rolAnterior = usuario.getRol();
        usuario.setRol(nuevoRol);
        usuarioRepository.save(usuario);

        log.info("✅ Rol cambiado: Usuario {} ({}) de {} a {} por {}",
                usuario.getNombre(), usuario.getEmail(), rolAnterior, nuevoRol, solicitante.getEmail());
    }

    /**
     * Validar que el solicitante tiene permisos para asignar el rol
     */
    private void validarPermisosCambioRol(Usuario solicitante, Rol nuevoRol) {
        switch (nuevoRol) {
            case ADMIN:
                // Solo ADMIN puede crear otros ADMIN
                if (solicitante.getRol() != Rol.ADMIN) {
                    throw new IllegalArgumentException("Solo un administrador puede asignar el rol de administrador");
                }
                break;
            case GERENTE:
                // Solo ADMIN puede crear GERENTES
                if (solicitante. getRol() != Rol. ADMIN) {
                    throw new IllegalArgumentException("Solo un administrador puede asignar el rol de gerente");
                }
                break;
            case ENTRENADOR:
                // ADMIN y GERENTE pueden crear ENTRENADORES
                if (solicitante.getRol() != Rol.ADMIN && solicitante.getRol() != Rol. GERENTE) {
                    throw new IllegalArgumentException("No tienes permisos para asignar el rol de entrenador");
                }
                break;
            case USUARIO:
                // ADMIN, GERENTE pueden degradar a USUARIO
                if (solicitante.getRol() != Rol.ADMIN && solicitante.getRol() != Rol.GERENTE) {
                    throw new IllegalArgumentException("No tienes permisos para cambiar este rol");
                }
                break;
        }
    }

    /**
     * Obtener todos los usuarios por rol
     */
    public List<Usuario> obtenerUsuariosPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    /**
     * Obtener todos los entrenadores activos
     */
    public List<Usuario> obtenerEntrenadoresActivos() {
        return usuarioRepository.findByRolAndActivoTrue(Rol.ENTRENADOR);
    }

    /**
     * Obtener todos los gerentes activos
     */
    public List<Usuario> obtenerGerentesActivos() {
        return usuarioRepository.findByRolAndActivoTrue(Rol.GERENTE);
    }

    /**
     * Obtener usuarios que pueden ser promovidos a entrenador
     * (usuarios normales verificados y activos)
     */
    public List<Usuario> obtenerCandidatosEntrenador() {
        return usuarioRepository.findByRolAndActivoTrueAndVerificadoTrue(Rol.USUARIO);
    }

    /**
     * Obtener usuarios que pueden ser promovidos a gerente
     * (usuarios normales o entrenadores verificados y activos)
     */
    public List<Usuario> obtenerCandidatosGerente() {
        return usuarioRepository. findCandidatosGerente();
    }

    /**
     * Asignar entrenador a un usuario
     */
    @Transactional
    public void asignarEntrenador(Long usuarioId, Long entrenadorId, Usuario solicitante) {
        // Validar permisos (solo el propio usuario, ADMIN o GERENTE)
        Usuario usuario = usuarioRepository.findById(usuarioId)
                . orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        Usuario entrenador = usuarioRepository. findById(entrenadorId)
                .orElseThrow(() -> new NotFoundException("Entrenador no encontrado"));

        // Verificar que el entrenador tiene rol de ENTRENADOR
        if (entrenador.getRol() != Rol.ENTRENADOR) {
            throw new IllegalArgumentException("El usuario seleccionado no es un entrenador");
        }

        // Validar permisos
        if (!solicitante.getId().equals(usuarioId) && 
            solicitante.getRol() != Rol.ADMIN && 
            solicitante.getRol() != Rol.GERENTE) {
            throw new IllegalArgumentException("No tienes permisos para asignar entrenador a este usuario");
        }

        usuario.setEntrenadorAsignado(entrenador);
        usuarioRepository.save(usuario);

        log.info("✅ Entrenador {} asignado a usuario {} por {}",
                entrenador. getNombre(), usuario.getNombre(), solicitante.getEmail());
    }

    /**
     * Remover entrenador asignado
     */
    @Transactional
    public void removerEntrenador(Long usuarioId, Usuario solicitante) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                . orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // Validar permisos
        if (! solicitante.getId().equals(usuarioId) && 
            solicitante.getRol() != Rol.ADMIN && 
            solicitante.getRol() != Rol.GERENTE) {
            throw new IllegalArgumentException("No tienes permisos para remover el entrenador de este usuario");
        }

        usuario.setEntrenadorAsignado(null);
        usuarioRepository.save(usuario);

        log.info("✅ Entrenador removido del usuario {} por {}", usuario.getNombre(), solicitante. getEmail());
    }

    /**
     * Cambiar permiso de ver agenda para entrenador
     */
    @Transactional
    public void cambiarPermisoVerAgenda(Long usuarioId, Boolean permitir, Usuario solicitante) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // Solo el propio usuario puede cambiar este permiso
        if (!solicitante.getId().equals(usuarioId)) {
            throw new IllegalArgumentException("Solo puedes cambiar tus propios permisos de privacidad");
        }

        usuario.setPermitirVerAgenda(permitir);
        usuarioRepository.save(usuario);

        log.info("✅ Permiso ver agenda cambiado a {} para usuario {}", permitir, usuario.getNombre());
    }

    /**
     * Verificar si un entrenador puede ver la agenda de un usuario
     */
    public boolean puedeVerAgenda(Usuario entrenador, Usuario usuario) {
        // El usuario debe tener al entrenador asignado
        if (usuario.getEntrenadorAsignado() == null) {
            return false;
        }

        // Verificar que es SU entrenador asignado
        if (! usuario.getEntrenadorAsignado().getId().equals(entrenador.getId())) {
            return false;
        }

        // Verificar que el usuario ha dado permiso
        return usuario.getPermitirVerAgenda() != null && usuario.getPermitirVerAgenda();
    }
}
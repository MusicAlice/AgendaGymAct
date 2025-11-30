package io.bootify.agenda_gym.repos;

import io.bootify.agenda_gym.domain.Rol;
import io.bootify.agenda_gym.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data. jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    // ========== MÉTODOS PARA ROLES ==========

    List<Usuario> findByRol(Rol rol);

    List<Usuario> findByRolAndActivoTrue(Rol rol);

    List<Usuario> findByRolAndActivoTrueAndVerificadoTrue(Rol rol);

    // Candidatos a gerente: usuarios y entrenadores activos y verificados
    @Query("SELECT u FROM Usuario u WHERE u.rol IN ('USUARIO', 'ENTRENADOR') AND u.activo = true AND u.verificado = true")
    List<Usuario> findCandidatosGerente();

    // Obtener clientes de un entrenador
    List<Usuario> findByEntrenadorAsignadoId(Long entrenadorId);

    // Obtener clientes que permiten ver su agenda
    List<Usuario> findByEntrenadorAsignadoIdAndPermitirVerAgendaTrue(Long entrenadorId);

    // Contar usuarios por rol
    long countByRol(Rol rol);

    // Contar usuarios activos por rol
    long countByRolAndActivoTrue(Rol rol);
}
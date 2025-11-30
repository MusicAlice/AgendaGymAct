package io.bootify.agenda_gym. repos;

import io.bootify.agenda_gym.domain.RecuperacionClave;
import io.bootify.agenda_gym. domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecuperacionClaveRepository extends JpaRepository<RecuperacionClave, Long> {

    RecuperacionClave findFirstByUsuarioId(Long id);
    
    Optional<RecuperacionClave> findByTokenAndUsadoFalse(String token);
    
    Optional<RecuperacionClave> findFirstByUsuarioAndUsadoFalseOrderByFechaExpiracionDesc(Usuario usuario);
    
    void deleteByUsuario(Usuario usuario);
}
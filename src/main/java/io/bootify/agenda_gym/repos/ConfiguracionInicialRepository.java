package io.bootify.agenda_gym.repos;

import io.bootify.agenda_gym.domain. ConfiguracionInicial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfiguracionInicialRepository extends JpaRepository<ConfiguracionInicial, Long> {
    
    boolean existsByUsuarioId(Long usuarioId);
    
    Optional<ConfiguracionInicial> findByUsuarioId(Long usuarioId);  // ← AGREGAR ESTE
    
}
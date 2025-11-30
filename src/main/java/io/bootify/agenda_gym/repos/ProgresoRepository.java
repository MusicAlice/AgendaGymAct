package io.bootify.agenda_gym.repos;

import io.bootify.agenda_gym.domain.Progreso;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProgresoRepository extends JpaRepository<Progreso, Long> {

    Progreso findFirstByUsuarioId(Long id);

}

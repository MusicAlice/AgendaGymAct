package io.bootify.agenda_gym.repos;

import io.bootify.agenda_gym.domain.Calendario;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CalendarioRepository extends JpaRepository<Calendario, Long> {

    Calendario findFirstByUsuarioId(Long id);

}

package io.bootify.agenda_gym.repos;

import io.bootify.agenda_gym.domain.Alimentacion;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AlimentacionRepository extends JpaRepository<Alimentacion, Long> {

    Alimentacion findFirstByUsuarioId(Long id);

}

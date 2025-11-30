package io.bootify.agenda_gym.repos;

import io.bootify.agenda_gym.domain.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CheckInRepository extends JpaRepository<CheckIn, Long> {

    CheckIn findFirstByUsuarioId(Long id);

}

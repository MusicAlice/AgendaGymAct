package io.bootify.agenda_gym.repos;

import io.bootify.agenda_gym.domain.Logro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LogroRepository extends JpaRepository<Logro, Long> {
    List<Logro> findByUsuarioId(Long usuarioId);
}

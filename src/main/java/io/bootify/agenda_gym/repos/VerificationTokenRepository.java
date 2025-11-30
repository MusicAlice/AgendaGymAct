package io.bootify.agenda_gym.repos;

import io.bootify.agenda_gym.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para manejar tokens de verificación de usuarios.
 * Permite buscar, listar y eliminar tokens asociados a un usuario.
 */
@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    /**
     * Busca un token por su valor exacto.
     * @param token token único generado
     * @return Optional con el token si existe
     */
    Optional<VerificationToken> findByToken(String token);

    /**
     * Lista los últimos 100 tokens creados, ordenados por ID descendente.
     * Útil para debugging o administración.
     * @return lista de tokens recientes
     */
    List<VerificationToken> findTop100ByOrderByIdDesc();

    /**
     * Elimina todos los tokens asociados a un usuario específico.
     * @param usuarioId ID del usuario
     */
    void deleteByUsuario_Id(Long usuarioId);
}

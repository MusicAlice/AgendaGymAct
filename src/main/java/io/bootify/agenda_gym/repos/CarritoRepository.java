package io.bootify.agenda_gym.repos;

import io.bootify.agenda_gym.domain.Carrito;
import io.bootify.agenda_gym.domain. Usuario;
import org. springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query. Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    
    // Buscar carrito por usuario
    Optional<Carrito> findByUsuario(Usuario usuario);
    
    // Buscar carrito por ID de usuario
    Optional<Carrito> findByUsuarioId(Long usuarioId);
    
    // Verificar si el usuario tiene carrito
    boolean existsByUsuarioId(Long usuarioId);
    
    // Obtener carrito con items (evitar N+1)
    @Query("SELECT c FROM Carrito c LEFT JOIN FETCH c.items WHERE c.usuario.id = :usuarioId")
    Optional<Carrito> findByUsuarioIdWithItems(@Param("usuarioId") Long usuarioId);
}
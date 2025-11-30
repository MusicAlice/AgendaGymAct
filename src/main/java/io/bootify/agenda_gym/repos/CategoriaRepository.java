package io. bootify.agenda_gym.repos;

import io.bootify.agenda_gym. domain.Categoria;
import org.springframework. data.jpa. repository.JpaRepository;
import org. springframework.stereotype.Repository;

import java.util.List;
import java.util. Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    // Buscar categoría por nombre
    Optional<Categoria> findByNombre(String nombre);
    
    // Obtener categorías activas ordenadas
    List<Categoria> findByActivoTrueOrderByOrdenAsc();
    
    // Verificar si existe una categoría con ese nombre
    boolean existsByNombre(String nombre);
}
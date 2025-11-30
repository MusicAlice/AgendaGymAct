package io.bootify.agenda_gym.repos;

import io.bootify.agenda_gym.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Devuelve productos por categoría (sin filtrar por activo)
    List<Producto> findByCategoriaId(Long categoriaId);

    // Buscar por nombre (case-insensitive) - usado por TiendaService.searchByNombre
    @Query("SELECT p FROM Producto p WHERE lower(p.nombre) LIKE lower(concat('%', :q, '%'))")
    List<Producto> searchByNombre(@Param("q") String q);

    // Filtrado con params opcionales: categoria, genero, precioMin, precioMax
    // CORRECCIÓN: usar p.categoria.id en lugar de p.categoriaId (Producto no tiene atributo directo categoriaId)
    @Query("""
        SELECT p FROM Producto p
        WHERE (:categoriaId IS NULL OR p.categoria.id = :categoriaId)
          AND (:genero IS NULL OR p.genero = :genero)
          AND (:precioMin IS NULL OR p.precio >= :precioMin)
          AND (:precioMax IS NULL OR p.precio <= :precioMax)
    """)
    List<Producto> filter(
            @Param("categoriaId") Long categoriaId,
            @Param("genero") String genero,
            @Param("precioMin") BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax
    );

    // Productos destacados (sin filtro activo)
    List<Producto> findByDestacadoTrue();

    /* Métodos adicionales que usan los servicios en tu proyecto.
       Los añado para evitar futuros NoSuchMethod/Mapping errors en tiempo de ejecución. */

    List<Producto> findByActivoTrue();

    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);

    List<Producto> findByDestacadoTrueAndActivoTrue();

    List<Producto> findByNuevoTrueAndActivoTrue();

    List<Producto> findByDescuentoPorcentajeGreaterThanAndActivoTrue(Integer porcentaje);

    List<Producto> findByNombreContainingIgnoreCaseAndActivoTrue(String termino);
}
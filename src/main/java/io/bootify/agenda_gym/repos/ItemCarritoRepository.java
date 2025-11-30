package io.bootify.agenda_gym. repos;

import io.bootify.agenda_gym.domain. Carrito;
import io.bootify.agenda_gym. domain.ItemCarrito;
import io.bootify.agenda_gym.domain. Producto;
import org.springframework.data. jpa.repository. JpaRepository;
import org.springframework. stereotype.Repository;

import java.util.List;
import java.util. Optional;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    
    // Buscar items por carrito
    List<ItemCarrito> findByCarrito(Carrito carrito);
    
    // Buscar items por ID de carrito
    List<ItemCarrito> findByCarritoId(Long carritoId);
    
    // Buscar item específico en un carrito
    Optional<ItemCarrito> findByCarritoAndProducto(Carrito carrito, Producto producto);
    
    // Buscar item por carrito y producto ID
    Optional<ItemCarrito> findByCarritoIdAndProductoId(Long carritoId, Long productoId);
    
    // Eliminar todos los items de un carrito
    void deleteByCarritoId(Long carritoId);
    
    // Contar items en un carrito
    Long countByCarritoId(Long carritoId);
}
package io.bootify.agenda_gym.service;

import io.bootify.agenda_gym.domain.Carrito;
import io.bootify.agenda_gym.domain. ItemCarrito;
import io.bootify. agenda_gym.domain.Producto;
import io.bootify.agenda_gym. domain.Usuario;
import io. bootify.agenda_gym.repos.CarritoRepository;
import io.bootify.agenda_gym.repos. ItemCarritoRepository;
import io.bootify.agenda_gym.repos.ProductoRepository;
import io.bootify.agenda_gym.repos. UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation. Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final ItemCarritoRepository itemCarritoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public CarritoService(CarritoRepository carritoRepository,
                         ItemCarritoRepository itemCarritoRepository,
                         ProductoRepository productoRepository,
                         UsuarioRepository usuarioRepository) {
        this.carritoRepository = carritoRepository;
        this.itemCarritoRepository = itemCarritoRepository;
        this. productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Obtener o crear carrito para un usuario
    public Carrito obtenerOCrearCarrito(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    Usuario usuario = usuarioRepository.findById(usuarioId)
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUsuario(usuario);
                    return carritoRepository.save(nuevoCarrito);
                });
    }

    // Obtener carrito con items
    public Optional<Carrito> obtenerCarritoConItems(Long usuarioId) {
        return carritoRepository.findByUsuarioIdWithItems(usuarioId);
    }

    // Agregar producto al carrito
    public Carrito agregarProducto(Long usuarioId, Long productoId, Integer cantidad) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Verificar stock
        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente.  Disponible: " + producto.getStock());
        }

        // Buscar si ya existe el producto en el carrito
        Optional<ItemCarrito> itemExistente = itemCarritoRepository
                .findByCarritoIdAndProductoId(carrito. getId(), productoId);

        if (itemExistente. isPresent()) {
            // Actualizar cantidad
            ItemCarrito item = itemExistente.get();
            int nuevaCantidad = item.getCantidad() + cantidad;
            if (producto.getStock() < nuevaCantidad) {
                throw new RuntimeException("Stock insuficiente para la cantidad total");
            }
            item.setCantidad(nuevaCantidad);
            itemCarritoRepository. save(item);
        } else {
            // Crear nuevo item
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(cantidad);
            itemCarritoRepository.save(nuevoItem);
            carrito.getItems().add(nuevoItem);
        }

        return carritoRepository.save(carrito);
    }

    // Actualizar cantidad de un item
    public Carrito actualizarCantidad(Long usuarioId, Long productoId, Integer nuevaCantidad) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        
        if (nuevaCantidad <= 0) {
            return removerProducto(usuarioId, productoId);
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getStock() < nuevaCantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        itemCarritoRepository. findByCarritoIdAndProductoId(carrito.getId(), productoId)
                .ifPresent(item -> {
                    item.setCantidad(nuevaCantidad);
                    itemCarritoRepository.save(item);
                });

        return carrito;
    }

    // Remover producto del carrito
    public Carrito removerProducto(Long usuarioId, Long productoId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        
        itemCarritoRepository. findByCarritoIdAndProductoId(carrito.getId(), productoId)
                .ifPresent(item -> {
                    carrito.getItems(). remove(item);
                    itemCarritoRepository.delete(item);
                });

        return carritoRepository.save(carrito);
    }

    // Vaciar carrito
    public void vaciarCarrito(Long usuarioId) {
        carritoRepository.findByUsuarioId(usuarioId)
                .ifPresent(carrito -> {
                    itemCarritoRepository. deleteByCarritoId(carrito.getId());
                    carrito.getItems(). clear();
                    carritoRepository.save(carrito);
                });
    }

    // Obtener cantidad de items en el carrito
    public int obtenerCantidadItems(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .map(Carrito::getCantidadTotal)
                .orElse(0);
    }

    // Obtener subtotal del carrito
    public BigDecimal obtenerSubtotal(Long usuarioId) {
        return carritoRepository.findByUsuarioIdWithItems(usuarioId)
                .map(Carrito::getSubtotal)
                .orElse(BigDecimal. ZERO);
    }

    // Verificar si el carrito está vacío
    public boolean estaVacio(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .map(carrito -> carrito. getItems().isEmpty())
                .orElse(true);
    }
}
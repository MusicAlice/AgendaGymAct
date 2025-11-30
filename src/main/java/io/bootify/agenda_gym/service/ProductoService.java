package io.bootify.agenda_gym.service;

import io.bootify.agenda_gym.domain.Categoria;
import io.bootify.agenda_gym.domain.Producto;
import io.bootify.agenda_gym.repos.CategoriaRepository;
import io.bootify.agenda_gym.repos.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(final ProductoRepository productoRepository,
                           final CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // Obtener todos los productos activos
    public List<Producto> obtenerProductosActivos() {
        return productoRepository.findByActivoTrue();
    }

    // Obtener productos por categoría
    public List<Producto> obtenerPorCategoria(final Long categoriaId) {
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId);
    }

    // Obtener productos destacados
    public List<Producto> obtenerDestacados() {
        return productoRepository.findByDestacadoTrueAndActivoTrue();
    }

    // Obtener productos nuevos
    public List<Producto> obtenerNuevos() {
        return productoRepository.findByNuevoTrueAndActivoTrue();
    }

    // Obtener productos en oferta
    public List<Producto> obtenerEnOferta() {
        return productoRepository.findByDescuentoPorcentajeGreaterThanAndActivoTrue(0);
    }

    // Buscar productos por nombre
    public List<Producto> buscar(final String termino) {
        return productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(termino);
    }

    // Buscar por ID
    public Optional<Producto> buscarPorId(final Long id) {
        return productoRepository.findById(id);
    }

    // Crear producto
    public Producto crear(final Producto producto) {
        return productoRepository.save(producto);
    }

    // Actualizar producto
    public Producto actualizar(final Long id, final Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setDescripcion(productoActualizado.getDescripcion());
                    producto.setPrecio(productoActualizado.getPrecio());
                    producto.setPrecioOferta(productoActualizado.getPrecioOferta());
                    producto.setDescuentoPorcentaje(productoActualizado.getDescuentoPorcentaje());
                    producto.setImagenUrl(productoActualizado.getImagenUrl());
                    producto.setStock(productoActualizado.getStock());
                    producto.setActivo(productoActualizado.isActivo());
                    producto.setDestacado(productoActualizado.isDestacado());
                    producto.setNuevo(productoActualizado.isNuevo());
                    producto.setTalla(productoActualizado.getTalla());
                    producto.setColor(productoActualizado.getColor());
                    producto.setMarca(productoActualizado.getMarca());
                    producto.setPeso(productoActualizado.getPeso());
                    producto.setSabor(productoActualizado.getSabor());
                    producto.setCategoria(productoActualizado.getCategoria());
                    return productoRepository.save(producto);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // Actualizar stock
    public void actualizarStock(final Long productoId, final Integer cantidad) {
        productoRepository.findById(productoId)
                .ifPresent(producto -> {
                    producto.setStock(producto.getStock() - cantidad);
                    productoRepository.save(producto);
                });
    }

    // Verificar disponibilidad
    public boolean hayStock(final Long productoId, final Integer cantidadSolicitada) {
        return productoRepository.findById(productoId)
                .map(producto -> producto.getStock() >= cantidadSolicitada)
                .orElse(false);
    }

    // Desactivar producto
    public void desactivar(final Long id) {
        productoRepository.findById(id)
                .ifPresent(producto -> {
                    producto.setActivo(false);
                    productoRepository.save(producto);
                });
    }

    // Eliminar producto
    public void eliminar(final Long id) {
        productoRepository.deleteById(id);
    }

    // Inicializar productos de ejemplo
    public void inicializarProductosEjemplo() {
        if (productoRepository.count() == 0) {
            final List<Categoria> categorias = categoriaRepository.findByActivoTrueOrderByOrdenAsc();

            if (categorias.isEmpty()) return;

            final Categoria ropa = categorias.stream()
                    .filter(c -> c.getNombre().contains("Ropa"))
                    .findFirst().orElse(null);

            final Categoria suplementos = categorias.stream()
                    .filter(c -> c.getNombre().contains("Suplementos"))
                    .findFirst().orElse(null);

            final Categoria equipos = categorias.stream()
                    .filter(c -> c.getNombre().contains("Equipos"))
                    .findFirst().orElse(null);

            // Productos de Ropa
            if (ropa != null) {
                crearProductoEjemplo("Camiseta Dry-Fit Pro", "Camiseta deportiva transpirable",
                        new BigDecimal("89900"), new BigDecimal("76415"), 15, ropa, "M", "Negro", "GymTrack", true, true);
                crearProductoEjemplo("Leggings Compression", "Leggings de compresión para entrenamiento",
                        new BigDecimal("129900"), null, null, ropa, "S", "Gris", "GymTrack", true, false);
                crearProductoEjemplo("Shorts Training", "Shorts ligeros para cardio",
                        new BigDecimal("69900"), new BigDecimal("59415"), 15, ropa, "L", "Azul", "GymTrack", false, true);
                crearProductoEjemplo("Sudadera Hoodie Gym", "Sudadera con capucha para calentamiento",
                        new BigDecimal("149900"), null, null, ropa, "M", "Negro", "GymTrack", true, false);
            }

            // Productos de Suplementos
            if (suplementos != null) {
                crearProductoEjemplo("Proteína Whey Gold 2lb", "Proteína de suero de alta calidad",
                        new BigDecimal("189900"), new BigDecimal("161415"), 15, suplementos, null, null, "OptimumNutrition", true, false);
                crearProductoEjemplo("Creatina Monohidratada 300g", "Creatina pura para fuerza",
                        new BigDecimal("79900"), null, null, suplementos, null, null, "MuscleTech", true, true);
                crearProductoEjemplo("Pre-Entreno C4 Original", "Energía explosiva para tu entrenamiento",
                        new BigDecimal("129900"), new BigDecimal("103920"), 20, suplementos, null, null, "Cellucor", true, false);
                crearProductoEjemplo("BCAA Powder 400g", "Aminoácidos ramificados para recuperación",
                        new BigDecimal("99900"), null, null, suplementos, null, null, "BSN", false, true);
            }

            // Productos de Equipos
            if (equipos != null) {
                crearProductoEjemplo("Mancuernas Ajustables 20kg", "Set de mancuernas ajustables",
                        new BigDecimal("299900"), new BigDecimal("254915"), 15, equipos, null, null, "Bowflex", true, false);
                crearProductoEjemplo("Banda de Resistencia Set", "Set de 5 bandas con diferentes resistencias",
                        new BigDecimal("59900"), null, null, equipos, null, null, "GymTrack", true, true);
                crearProductoEjemplo("Colchoneta Yoga Premium", "Colchoneta antideslizante 6mm",
                        new BigDecimal("89900"), new BigDecimal("71920"), 20, equipos, null, null, "Manduka", true, false);
                crearProductoEjemplo("Cuerda de Saltar Pro", "Cuerda de velocidad con rodamientos",
                        new BigDecimal("39900"), null, null, equipos, null, null, "GymTrack", false, true);
            }
        }
    }

    private void crearProductoEjemplo(final String nombre, final String descripcion, final BigDecimal precio,
                                      final BigDecimal precioOferta, final Integer descuento, final Categoria categoria,
                                      final String talla, final String color, final String marca, final boolean destacado, final boolean nuevo) {
        final Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setPrecioOferta(precioOferta);
        producto.setDescuentoPorcentaje(descuento);
        producto.setCategoria(categoria);
        producto.setTalla(talla);
        producto.setColor(color);
        producto.setMarca(marca);
        producto.setStock(50);
        producto.setActivo(true);
        producto.setDestacado(destacado);
        producto.setNuevo(nuevo);
        producto.setImagenUrl("/img/productos/default.png");
        productoRepository.save(producto);
    }
}
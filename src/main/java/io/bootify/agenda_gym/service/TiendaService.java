package io.bootify.agenda_gym.service;

import io.bootify.agenda_gym.domain.Categoria;
import io.bootify.agenda_gym.domain.Producto;
import io.bootify.agenda_gym.model.CategoriaTiendaDTO;
import io.bootify.agenda_gym.model.ProductoTiendaDTO;
import io.bootify.agenda_gym.repos.CategoriaRepository;
import io.bootify.agenda_gym.repos.ProductoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TiendaService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public TiendaService(final ProductoRepository productoRepository,
                         final CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaTiendaDTO> getCategorias() {
        // StreamSupport para trabajar seguro con Iterable retornado por findAll()
        return StreamSupport.stream(categoriaRepository.findAll().spliterator(), false)
                .map(this::mapCategoria)
                // Orden null-safe: usamos Comparator.nullsLast con Integer::compareTo
                .sorted(Comparator.comparing(CategoriaTiendaDTO::getOrden,
                        Comparator.nullsLast(Integer::compareTo)))
                .collect(Collectors.toList());
    }

    public List<ProductoTiendaDTO> getProductosDestacados() {
        return productoRepository.findByDestacadoTrue()
                .stream()
                .map(this::mapProducto)
                .collect(Collectors.toList());
    }

    public List<ProductoTiendaDTO> getProductosPorCategoria(final Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId)
                .stream()
                .map(this::mapProducto)
                .collect(Collectors.toList());
    }

    public List<ProductoTiendaDTO> buscarProductos(final String q) {
        return productoRepository.searchByNombre(q)
                .stream()
                .map(this::mapProducto)
                .collect(Collectors.toList());
    }

    public List<ProductoTiendaDTO> filtrar(final Long categoriaId, final String genero,
                                           final BigDecimal precioMin, final BigDecimal precioMax, final String orden) {

        final List<ProductoTiendaDTO> lista = productoRepository.filter(categoriaId, genero, precioMin, precioMax)
                .stream()
                .map(this::mapProducto)
                .collect(Collectors.toList());

        // Ordenamiento null-safe por precio
        if ("precioAsc".equals(orden)) {
            lista.sort(Comparator.comparing(ProductoTiendaDTO::getPrecio,
                    Comparator.nullsLast(BigDecimal::compareTo)));
        } else if ("precioDesc".equals(orden)) {
            lista.sort(Comparator.comparing(ProductoTiendaDTO::getPrecio,
                    Comparator.nullsLast(BigDecimal::compareTo)).reversed());
        } else if ("nombreAsc".equals(orden)) {
            // Explicitamos el tipo del lambda para evitar inferencia ambigua
            lista.sort(Comparator.comparing((ProductoTiendaDTO dto) ->
                    dto.getNombre() == null ? "" : dto.getNombre().toLowerCase()));
        } else if ("nombreDesc".equals(orden)) {
            lista.sort(Comparator.comparing((ProductoTiendaDTO dto) ->
                    dto.getNombre() == null ? "" : dto.getNombre().toLowerCase()).reversed());
        }

        return lista;
    }

    private ProductoTiendaDTO mapProducto(final Producto p) {
        final ProductoTiendaDTO dto = new ProductoTiendaDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setImagenUrl(p.getImagenUrl());
        dto.setPrecio(p.getPrecio() == null ? BigDecimal.ZERO : p.getPrecio());
        dto.setPrecioOferta(p.getPrecioOferta());
        dto.setStock(p.getStock() == null ? 0 : p.getStock());
        dto.setGenero(p.getGenero());

        if (p.getCategoria() != null) {
            dto.setCategoriaId(p.getCategoria().getId());
            dto.setCategoriaNombre(p.getCategoria().getNombre());
        }

        return dto;
    }

    private CategoriaTiendaDTO mapCategoria(final Categoria c) {
        final CategoriaTiendaDTO dto = new CategoriaTiendaDTO();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setDescripcion(c.getDescripcion());
        dto.setIcono(c.getIcono());
        dto.setImagenUrl(c.getImagenUrl());
        dto.setOrden(c.getOrden());
        return dto;
    }
}
package io.bootify.agenda_gym.service;

import io.bootify.agenda_gym.domain.Categoria;
import io. bootify.agenda_gym.repos.CategoriaRepository;
import org. springframework.stereotype.Service;
import org.springframework. transaction.annotation. Transactional;

import java.util.List;
import java.util. Optional;

@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Obtener todas las categorías activas
    public List<Categoria> obtenerCategoriasActivas() {
        return categoriaRepository.findByActivoTrueOrderByOrdenAsc();
    }

    // Obtener todas las categorías
    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    // Buscar por ID
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository. findById(id);
    }

    // Buscar por nombre
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    // Crear nueva categoría
    public Categoria crear(Categoria categoria) {
        if (categoriaRepository.existsByNombre(categoria. getNombre())) {
            throw new RuntimeException("Ya existe una categoría con ese nombre");
        }
        return categoriaRepository.save(categoria);
    }

    // Actualizar categoría
    public Categoria actualizar(Long id, Categoria categoriaActualizada) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    categoria.setNombre(categoriaActualizada.getNombre());
                    categoria.setDescripcion(categoriaActualizada.getDescripcion());
                    categoria.setIcono(categoriaActualizada.getIcono());
                    categoria.setImagenUrl(categoriaActualizada.getImagenUrl());
                    categoria.setOrden(categoriaActualizada.getOrden());
                    categoria.setActivo(categoriaActualizada. isActivo());
                    return categoriaRepository.save(categoria);
                })
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    // Desactivar categoría (soft delete)
    public void desactivar(Long id) {
        categoriaRepository.findById(id)
                .ifPresent(categoria -> {
                    categoria. setActivo(false);
                    categoriaRepository.save(categoria);
                });
    }

    // Eliminar categoría (hard delete)
    public void eliminar(Long id) {
        categoriaRepository. deleteById(id);
    }

    // Inicializar categorías por defecto
    public void inicializarCategoriasPorDefecto() {
        if (categoriaRepository.count() == 0) {
            Categoria ropa = new Categoria();
            ropa. setNombre("Ropa Deportiva");
            ropa.setDescripcion("Camisetas, leggings, shorts y más");
            ropa.setIcono("👕");
            ropa.setOrden(1);
            ropa.setActivo(true);
            categoriaRepository.save(ropa);

            Categoria suplementos = new Categoria();
            suplementos.setNombre("Suplementos");
            suplementos.setDescripcion("Proteínas, creatina, pre-entrenos");
            suplementos.setIcono("💊");
            suplementos.setOrden(2);
            suplementos. setActivo(true);
            categoriaRepository.save(suplementos);

            Categoria equipos = new Categoria();
            equipos.setNombre("Equipos");
            equipos.setDescripcion("Mancuernas, bandas, colchonetas");
            equipos.setIcono("🏋️");
            equipos.setOrden(3);
            equipos.setActivo(true);
            categoriaRepository.save(equipos);
        }
    }
}
package io.bootify.agenda_gym.rest;

import io.bootify.agenda_gym.domain.Producto;
import io.bootify.agenda_gym.repos.ProductoRepository;
import io.bootify.agenda_gym.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoImageController {

    private final FileStorageService fileStorageService;
    private final ProductoRepository productoRepository;
    private static final String SUBDIR = "productos";

    public ProductoImageController(final FileStorageService fileStorageService,
                                   final ProductoRepository productoRepository) {
        this.fileStorageService = fileStorageService;
        this.productoRepository = productoRepository;
    }

    /**
     * Subir imagen para producto con id.
     * Form field: file (multipart/form-data)
     * Response: { "imagenUrl": "/img/productos/uuid.ext" }
     */
    @PostMapping("/{id}/imagen")
    public ResponseEntity<?> uploadProductoImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        final Optional<Producto> maybe = productoRepository.findById(id);
        if (maybe.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado");
        }
        Producto producto = maybe.get();

        try {
            String filename = fileStorageService.store(file, SUBDIR);
            String publicUrl = "/img/productos/" + filename;

            // Si había una imagen previa y quieres borrarla del disco, puedes hacerlo aquí (opcional)

            producto.setImagenUrl(publicUrl);
            productoRepository.save(producto);

            return ResponseEntity.ok(Map.of("imagenUrl", publicUrl));
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo guardar la imagen: " + ex.getMessage(), ex);
        }
    }

    /**
     * Servir imagen desde disco.
     * Acceso público: /img/productos/{filename}
     */
    @GetMapping(value = "/../img/productos/{filename:.+}") // no se utiliza esta ruta directamente, se define mapping por @GetMapping en /img/productos/* abajo
    public void noop() {
        // método placeholder para claridad en código; no se usa.
    }

    // Endpoint público para servir imágenes (ruta raíz fuera de /api)
    @GetMapping(value = "/../img/productos/serve/{filename:.+}")
    public ResponseEntity<Resource> serveImageIndirect(@PathVariable String filename) throws MalformedURLException {
        // Nota: preferimos exponer el endpoint real en /img/productos/{filename} (ver abajo)
        Resource resource = fileStorageService.loadAsResource(SUBDIR, filename);
        if (resource == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen no encontrada");
        }
        // Determinar Content-Type por extensión simple
        String lc = filename.toLowerCase();
        MediaType mediaType = MediaType.IMAGE_PNG;
        if (lc.endsWith(".jpg") || lc.endsWith(".jpeg")) mediaType = MediaType.IMAGE_JPEG;
        else if (lc.endsWith(".gif")) mediaType = MediaType.IMAGE_GIF;
        else if (lc.endsWith(".svg")) mediaType = MediaType.valueOf("image/svg+xml");

        return ResponseEntity.ok()
                .contentType(mediaType)
                .body(resource);
    }
}
package io.bootify.agenda_gym.rest;

import io.bootify.agenda_gym.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;

@RestController
public class ImageController {

    private final FileStorageService fileStorageService;
    private static final String SUBDIR = "productos";

    public ImageController(final FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/img/productos/{filename:.+}")
    public ResponseEntity<?> serveProductoImage(@PathVariable String filename) {
        try {
            Resource resource = fileStorageService.loadAsResource(SUBDIR, filename);
            if (resource != null) {
                String lc = filename.toLowerCase();
                MediaType mediaType = MediaType.IMAGE_PNG;
                if (lc.endsWith(".jpg") || lc.endsWith(".jpeg")) mediaType = MediaType.IMAGE_JPEG;
                else if (lc.endsWith(".gif")) mediaType = MediaType.IMAGE_GIF;
                else if (lc.endsWith(".svg")) mediaType = MediaType.valueOf("image/svg+xml");

                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .body(resource);
            } else {
                // fallback: inline SVG placeholder (no excepción)
                String svg = "<svg xmlns='http://www.w3.org/2000/svg' width='200' height='150' viewBox='0 0 200 150'>" +
                        "<rect width='200' height='150' fill='#f5f5f5'/>" +
                        "<text x='50%' y='50%' dominant-baseline='middle' text-anchor='middle' fill='#999' font-family='Segoe UI, Arial' font-size='14'>Sin imagen</text>" +
                        "</svg>";
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.valueOf("image/svg+xml"));
                return new ResponseEntity<>(svg, headers, HttpStatus.OK);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error leyendo la imagen");
        }
    }
}
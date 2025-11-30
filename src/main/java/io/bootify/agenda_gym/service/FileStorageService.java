package io.bootify.agenda_gym.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path uploadBaseDir;

    public FileStorageService(@Value("${app.upload.dir:${user.home}/agenda_gym_uploads}") String uploadDir) {
        this.uploadBaseDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadBaseDir);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear directorio de uploads: " + this.uploadBaseDir, e);
        }
    }

    /**
     * Guarda un MultipartFile en subDir (p.ej. "productos") y devuelve el nombre físico generado.
     */
    public String store(MultipartFile file, String subDir) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("Archivo vacío");
        }

        String contentType = file.getContentType() == null ? "" : file.getContentType();
        if (!contentType.startsWith("image/")) {
            throw new IOException("Tipo de archivo inválido: solo se permiten imágenes");
        }

        String original = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
        String ext = "";
        int idx = original.lastIndexOf('.');
        if (idx >= 0) {
            ext = original.substring(idx);
        }

        // Nombre único
        String filename = UUID.randomUUID().toString() + ext;

        Path dir = this.uploadBaseDir.resolve(subDir).normalize();
        Files.createDirectories(dir);

        Path target = dir.resolve(filename);
        // Guardar archivo
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return filename;
    }

    public Resource loadAsResource(String subDir, String filename) throws MalformedURLException {
        Path file = this.uploadBaseDir.resolve(subDir).resolve(filename).normalize();
        if (!Files.exists(file) || !Files.isReadable(file)) {
            return null;
        }
        Resource resource = new UrlResource(file.toUri());
        return resource.exists() ? resource : null;
    }

    public Path getBaseDir() {
        return uploadBaseDir;
    }
}
package com.naapi.naapi.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Guarda um ficheiro no disco e retorna a URL para acedê-lo.
     * @param file O ficheiro enviado (ex: foto de perfil)
     * @return A URL acessível do ficheiro (ex: /uploads/nome-do-ficheiro.png)
     */
    public String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Cria um nome de ficheiro único para evitar conflitos
            String originalFilename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);
            String uniqueFilename = UUID.randomUUID().toString() + "." + extension;

            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Retorna a URL relativa para ser servida pelo FileController
            // Ex: /uploads/123e4567-e89b-12d3-a456-426614174000.png
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(uniqueFilename)
                    .toUriString();

        } catch (IOException e) {
            throw new RuntimeException("Não foi possível guardar o ficheiro. " + e.getMessage());
        }
    }
}
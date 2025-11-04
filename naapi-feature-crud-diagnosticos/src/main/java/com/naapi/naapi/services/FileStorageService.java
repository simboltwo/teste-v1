package com.naapi.naapi.services;

import com.naapi.naapi.services.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final String uploadDir;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.uploadDir = uploadDir;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new BusinessException("Não foi possível criar o diretório para salvar os arquivos.");
        }
    }

    /**
     * Salva um arquivo em uma subpasta (ex: "fotos" ou "laudos")
     * @return A URL de acesso ao arquivo salvo.
     */
    public String saveFile(MultipartFile file, String subfolder) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // Normaliza o nome do arquivo (remove caracteres como ".." que podem causar problemas)
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        try {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        } catch (Exception e) {
            throw new BusinessException("Arquivo inválido. Nome de arquivo: " + originalFileName);
        }

        // Cria um nome de arquivo único para evitar colisões
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        try {
            // Cria a subpasta se ela não existir (ex: /uploads/fotos)
            Path targetLocation = this.fileStorageLocation.resolve(subfolder);
            Files.createDirectories(targetLocation);
            
            // Copia o arquivo para o local de destino
            Path filePath = targetLocation.resolve(uniqueFileName);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            // Retorna a URL completa para acessar o arquivo
            // Ex: http://localhost:8080/uploads/fotos/arquivo-unico.jpg
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(subfolder)
                    .path("/")
                    .path(uniqueFileName)
                    .toUriString();

        } catch (IOException ex) {
            throw new BusinessException("Não foi possível salvar o arquivo " + originalFileName);
        }
    }
}
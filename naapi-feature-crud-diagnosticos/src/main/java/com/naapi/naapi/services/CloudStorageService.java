package com.naapi.naapi.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.naapi.naapi.services.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudStorageService {

    private final Cloudinary cloudinary;

    /**
     * Faz o upload de um arquivo para o Cloudinary
     * @param file O arquivo (foto ou laudo)
     * @param folderName A pasta no Cloudinary (ex: "naapi/fotos" ou "naapi/laudos")
     * @return A URL segura (https) do arquivo salvo.
     */
    public String uploadFile(MultipartFile file, String folderName) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            // Constrói as opções de upload
            Map<?, ?> options = ObjectUtils.asMap(
                "folder", folderName,          // Define a pasta de destino
                "resource_type", "auto"      // Detecta automaticamente se é imagem, pdf, etc.
            );

            // Faz o upload e pega os resultados
            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

            // Retorna a URL segura (https)
            return uploadResult.get("secure_url").toString();

        } catch (IOException e) {
            throw new BusinessException("Erro ao fazer upload do arquivo: " + e.getMessage());
        }
    }

    // TODO: Criar um método "deleteFile(String publicId)"
    // para deletar arquivos antigos quando o usuário atualizar a foto ou excluir um laudo.
}
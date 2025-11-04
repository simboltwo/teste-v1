package com.naapi.naapi.config;

import com.cloudinary.Cloudinary;
// --- NOVOS IMPORTS ---
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// --- FIM NOVOS IMPORTS ---
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary; // Importar

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudConfig {

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        config.put("secure", "true");

        return new Cloudinary(config);
    }

    // --- ADICIONE ESTE MÉTODO ---
    // Este Bean vai ensinar o Jackson (usado pelo objectMapper no AlunoController)
    // a converter "2025-10-01" para um objeto LocalDate
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // Também é uma boa prática desabilitar isso para datas:
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}
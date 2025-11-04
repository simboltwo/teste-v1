package com.naapi.naapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    // Pega o caminho do application.properties
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = Paths.get(uploadDir).toFile().getAbsolutePath();
        String resourceLocation = "file:/" + absolutePath + "/";

        // Mapeia a URL /uploads/** para o diretório físico no servidor
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourceLocation);
    }
}
package com.naapi.naapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebugConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public CommandLineRunner debugJwtSecret() {
        return args -> {
            System.out.println("============================================");
            System.out.println("DEBUG JWT SECRET NO INÍCIO DA APLICAÇÃO:");
            if (jwtSecret == null) {
                System.out.println("ERRO: jwt.secret é NULL!");
            } else if (jwtSecret.equals("${JWT_SECRET}")) {
                System.out.println("ERRO: A variável não foi substituída! O Java leu a string literal '${JWT_SECRET}'.");
                System.out.println("Verifique se o nome da variável no Render é EXATAMENTE 'JWT_SECRET'.");
            } else {
                System.out.println("SUCESSO: Variável carregada.");
                System.out.println("Tamanho da chave: " + jwtSecret.length() + " caracteres.");
                if (jwtSecret.length() < 64) {
                    System.err.println("PERIGO: A chave é muito curta para HS512! Mínimo necessário: 64. Atual: " + jwtSecret.length());
                } else {
                    System.out.println("Validação de tamanho: OK (>= 64)");
                }
            }
            System.out.println("============================================");
        };
    }
}
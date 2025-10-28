package com.naapi.naapi.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.security.config.Customizer;

import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/setup/**").permitAll()
                
                .requestMatchers(HttpMethod.GET, "/usuarios/me").authenticated()
                .requestMatchers(HttpMethod.PUT, "/usuarios/me").authenticated()

                .requestMatchers(HttpMethod.POST, "/usuarios").hasRole("COORDENADOR_NAAPI")
                .requestMatchers(HttpMethod.GET, "/usuarios/**").hasRole("COORDENADOR_NAAPI")
                .requestMatchers(HttpMethod.PUT, "/usuarios/**").hasRole("COORDENADOR_NAAPI") // Adicionado
                .requestMatchers(HttpMethod.DELETE, "/usuarios/**").hasRole("COORDENADOR_NAAPI") // Adicionado
                
                .requestMatchers("/diagnosticos/**").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO")
                .requestMatchers("/cursos/**").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO")
                .requestMatchers("/turmas/**").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO")
                .requestMatchers("/tipos-atendimento/**").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO")

                .requestMatchers(HttpMethod.GET, "/alunos/**").hasAnyRole(
                    "COORDENADOR_NAAPI", "MEMBRO_TECNICO", "ESTAGIARIO_NAAPI",
                    "COORDENADOR_CURSO", "PROFESSOR"
                )
                .requestMatchers(HttpMethod.POST, "/alunos").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO", "ESTAGIARIO_NAAPI")
                .requestMatchers(HttpMethod.PUT, "/alunos/**").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO", "ESTAGIARIO_NAAPI")
                .requestMatchers(HttpMethod.DELETE, "/alunos/**").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO", "ESTAGIARIO_NAAPI")
                
                .requestMatchers(HttpMethod.GET, "/laudos/**").hasAnyRole(
                    "COORDENADOR_NAAPI", "MEMBRO_TECNICO", "ESTAGIARIO_NAAPI",
                    "COORDENADOR_CURSO", "PROFESSOR"
                )
                .requestMatchers(HttpMethod.POST, "/laudos").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO", "ESTAGIARIO_NAAPI")
                .requestMatchers(HttpMethod.PUT, "/laudos/**").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO", "ESTAGIARIO_NAAPI")
                .requestMatchers(HttpMethod.DELETE, "/laudos/**").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO", "ESTAGIARIO_NAAPI")

                .requestMatchers(HttpMethod.GET, "/peis/**").hasAnyRole(
                    "COORDENADOR_NAAPI", "MEMBRO_TECNICO", "ESTAGIARIO_NAAPI",
                    "COORDENADOR_CURSO", "PROFESSOR"
                )
                .requestMatchers(HttpMethod.POST, "/peis").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO")
                .requestMatchers(HttpMethod.PUT, "/peis/**").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO")
                .requestMatchers(HttpMethod.DELETE, "/peis/**").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO")

                .requestMatchers(HttpMethod.GET, "/relatorios/**").hasAnyRole(
                    "COORDENADOR_CURSO", 
                    "COORDENADOR_NAAPI", 
                    "MEMBRO_TECNICO", 
                    "ESTAGIARIO_NAAPI"
                )

                .requestMatchers(HttpMethod.GET, "/atendimentos/**").hasAnyRole(
                    "COORDENADOR_NAAPI", "MEMBRO_TECNICO", "ESTAGIARIO_NAAPI",
                    "COORDENADOR_CURSO", "PROFESSOR"
                )
                .requestMatchers(HttpMethod.POST, "/atendimentos").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO", "ESTAGIARIO_NAAPI")
                .requestMatchers(HttpMethod.PUT, "/atendimentos/**").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO", "ESTAGIARIO_NAAPI")
                .requestMatchers(HttpMethod.DELETE, "/atendimentos/**").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO", "ESTAGIARIO_NAAPI")

                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite pedidos do teu servidor Angular local
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); 
        // Permite os métodos HTTP comuns
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS")); 
        // Permite cabeçalhos comuns, incluindo Authorization para o Basic Auth
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuração a todos os caminhos da tua API ("/**")
        source.registerCorsConfiguration("/**", configuration); 
        return source;
    }
}
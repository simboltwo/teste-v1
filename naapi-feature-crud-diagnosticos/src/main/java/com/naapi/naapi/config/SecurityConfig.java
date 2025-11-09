package com.naapi.naapi.config;

import java.util.Arrays;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
import org.springframework.security.config.Customizer; // <<< Verifique se este import existe

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
            .cors(Customizer.withDefaults()) // <<< ADICIONE ESTA LINHA PARA HABILITAR CORS
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
            
            .httpBasic(httpBasic -> httpBasic
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException {
                        // Define o status como 401 Unauthorized
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        // Envia uma mensagem de erro JSON (opcional, mas bom)
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(
                            "{\"timestamp\": \"" + java.time.Instant.now() + "\", " +
                            "\"status\": 401, " +
                            "\"error\": \"Unauthorized\", " +
                            "\"message\": \"Credenciais inválidas\", " +
                            "\"path\": \"" + request.getRequestURI() + "\"}"
                        );

                        // O mais importante: NÃO adicionamos o cabeçalho 'WWW-Authenticate'
                        // Isto impede o popup do navegador de aparecer.
                    }
                })
            );

        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())); // Para H2 console

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Permite seu frontend
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "OPTIONS")); // Métodos permitidos
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); // Cabeçalhos permitidos

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica a todos os endpoints
        return source;
    }
}
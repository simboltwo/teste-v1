// simboltwo/teste-v1/teste-v1-60a965f68afab6e5940ba599fb03c96b4df194e0/naapi-feature-crud-diagnosticos/src/main/java/com/naapi/naapi/config/SecurityConfig.java

package com.naapi.naapi.config;

import java.util.Arrays;
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
import org.springframework.security.config.Customizer; 
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// Imports para o JWT e AuthenticationManager
import com.naapi.naapi.config.security.JwtAuthFilter; 
import lombok.RequiredArgsConstructor; 
import org.springframework.security.authentication.AuthenticationManager; 
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; 
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; 

// Imports para o EntryPoint (Tratamento de 401)
import org.springframework.security.web.AuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // Injeta o JwtAuthFilter
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults()) 
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/setup/**").permitAll()
                .requestMatchers("/health").permitAll()
                
                // --- MUDANÇA AQUI ---
                .requestMatchers("/auth/login").permitAll() // Permitir login (removido /api)
                .requestMatchers(HttpMethod.GET, "/usuarios/me").authenticated()
                .requestMatchers(HttpMethod.PUT, "/usuarios/me/detalhes").authenticated() 
                .requestMatchers(HttpMethod.PUT, "/usuarios/me/senha").authenticated() 
                // --- FIM DA MUDANÇA ---

                .requestMatchers(HttpMethod.POST, "/usuarios").hasRole("COORDENADOR_NAAPI")
                .requestMatchers(HttpMethod.GET, "/usuarios/**").hasRole("COORDENADOR_NAAPI")
                .requestMatchers(HttpMethod.PUT, "/usuarios/**").hasRole("COORDENADOR_NAAPI") 
                .requestMatchers(HttpMethod.DELETE, "/usuarios/**").hasRole("COORDENADOR_NAAPI") 

                .requestMatchers(HttpMethod.GET, "/papeis").hasRole("COORDENADOR_NAAPI")
                
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
                // --- NOVA REGRA PATCH ---
                .requestMatchers(HttpMethod.PATCH, "/alunos/**").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO", "ESTAGIARIO_NAAPI")
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
                .requestMatchers(HttpMethod.PATCH, "/atendimentos/**").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO", "ESTAGIARIO_NAAPI")
                .requestMatchers(HttpMethod.DELETE, "/atendimentos/**").hasAnyRole("COORDENADOR_NAAPI", "MEMBRO_TECNICO", "ESTAGIARIO_NAAPI")

                .anyRequest().authenticated()
            )
            // REMOVEMOS O .httpBasic() 
            
            // ADICIONAMOS O FILTRO JWT
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            
            // MANTEMOS O ENTRYPOINT (para customizar o erro 401)
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(
                            "{\"timestamp\": \"" + java.time.Instant.now() + "\", " +
                            "\"status\": 401, " +
                            "\"error\": \"Unauthorized\", " +
                            "\"message\": \"Token inválido ou expirado\", " +
                            "\"path\": \"" + request.getRequestURI() + "\"}"
                        );
                    }
                })
            );

        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())); // Para H2 console

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200", 
            "https://naapi.netlify.app",
            "https://naapi-front-v2.netlify.app" // <-- ADICIONE ESTA LINHA
        ));
        
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // Adicionei PATCH
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); 
        return source;
    }
}
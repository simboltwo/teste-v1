// Local: simboltwo/teste-v1/teste-v1-a5b4e926a89efc615cbd516b2e46746bdbf29eaa/naapi-feature-crud-diagnosticos/src/main/java/com/naapi/naapi/controllers/DataSeedController.java

package com.naapi.naapi.controllers;

import com.naapi.naapi.entities.*;
import com.naapi.naapi.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/setup")
@RequiredArgsConstructor
public class DataSeedController {

    private final PapelRepository papelRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final TurmaRepository turmaRepository;
    private final DiagnosticoRepository diagnosticoRepository;
    private final TipoAtendimentoRepository tipoAtendimentoRepository;
    private final AlunoRepository alunoRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/seed-all")
    public ResponseEntity<String> seedDatabase() {
        
        // --- INÍCIO DA CORREÇÃO ---
        // Vamos remover a verificação 'if (papelRepository.count() > 0)'
        // e tornar o seed inteligente (idempotente), verificando item por item.
        // --- FIM DA CORREÇÃO ---

        try {
            // 1. Inserir Papéis (Roles) - Verifica se já existe
            Papel p1 = papelRepository.findByAuthority("ROLE_COORDENADOR_NAAPI");
            if (p1 == null) p1 = papelRepository.save(Papel.builder().authority("ROLE_COORDENADOR_NAAPI").build());

            Papel p2 = papelRepository.findByAuthority("ROLE_MEMBRO_TECNICO");
            if (p2 == null) p2 = papelRepository.save(Papel.builder().authority("ROLE_MEMBRO_TECNICO").build());
            
            Papel p3 = papelRepository.findByAuthority("ROLE_ESTAGIARIO_NAAPI");
            if (p3 == null) p3 = papelRepository.save(Papel.builder().authority("ROLE_ESTAGIARIO_NAAPI").build());
            
            Papel p4 = papelRepository.findByAuthority("ROLE_COORDENADOR_CURSO");
            if (p4 == null) p4 = papelRepository.save(Papel.builder().authority("ROLE_COORDENADOR_CURSO").build());

            Papel p5 = papelRepository.findByAuthority("ROLE_PROFESSOR");
            if (p5 == null) p5 = papelRepository.save(Papel.builder().authority("ROLE_PROFESSOR").build());

            // 2. Inserir Usuários (Senha para todos é "123456") - Verifica se já existe
            String senhaCriptografada = passwordEncoder.encode("123456");
            
            UserDetails userDetails1 = usuarioRepository.findByEmail("coordenador@naapi.com");
            Usuario u1 = null;

            if (userDetails1 != null) {
                u1 = (Usuario) userDetails1; // Faz o CAST para a entidade Usuario
            }
            
            if (u1 == null) {
                u1 = Usuario.builder()
                    .nome("Admin Coordenador")
                    .email("coordenador@naapi.com")
                    .papeis(Set.of(p1))
                    .build();
            }
            u1.setSenha(senhaCriptografada); // Garante que a senha é "123456"
            usuarioRepository.save(u1);

            if (usuarioRepository.findByEmail("membro@naapi.com") == null) {
                Usuario u2 = Usuario.builder()
                    .nome("Membro Tecnico")
                    .email("membro@naapi.com")
                    .senha(senhaCriptografada)
                    .papeis(Set.of(p2))
                    .build();
                usuarioRepository.save(u2);
            }

            if (usuarioRepository.findByEmail("estagiario@naapi.com") == null) {
                Usuario u3 = Usuario.builder()
                    .nome("Estagiario 1 (Assistente)")
                    .email("estagiario@naapi.com")
                    .senha(senhaCriptografada)
                    .papeis(Set.of(p3))
                    .build();
                usuarioRepository.save(u3);
            }

            // O restante (Cursos, Turmas, etc.) não é crítico para o login.
            // A lógica original de criá-los (sem verificar) é aceitável aqui 
            // ou falhará silenciosamente se as constraints únicas já existirem.

            return ResponseEntity.ok("SUCESSO: O banco de dados foi verificado e os usuários de teste foram garantidos.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("ERRO AO TENTAR POPULAR O BANCO: " + e.getMessage() +
                ". Os dados podem já existir, mas a senha pode estar incorreta.");
        }
    }
}
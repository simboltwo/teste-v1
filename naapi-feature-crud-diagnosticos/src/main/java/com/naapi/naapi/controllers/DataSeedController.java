package com.naapi.naapi.controllers;

import com.naapi.naapi.entities.*;
import com.naapi.naapi.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.userdetails.UserDetails; // Não precisamos mais deste
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
    private final CursoRepository cursoRepository; //
    private final TurmaRepository turmaRepository; //
    private final DiagnosticoRepository diagnosticoRepository; //
    private final TipoAtendimentoRepository tipoAtendimentoRepository; //
    private final AlunoRepository alunoRepository; //
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/seed-all")
    public ResponseEntity<String> seedDatabase() {
        
        try {
            // 1. Inserir Papéis (Idempotente)
            Papel p1 = papelRepository.findByAuthority("ROLE_COORDENADOR_NAAPI");
            if (p1 == null) p1 = papelRepository.save(Papel.builder().authority("ROLE_COORDENADOR_NAAPI").build());

            Papel p2 = papelRepository.findByAuthority("ROLE_MEMBRO_TECNICO");
            if (p2 == null) p2 = papelRepository.save(Papel.builder().authority("ROLE_MEMBRO_TECNICO").build());
            
            Papel p3 = papelRepository.findByAuthority("ROLE_ESTAGIARIO_NAAPI");
            if (p3 == null) p3 = papelRepository.save(Papel.builder().authority("ROLE_ESTAGIARIO_NAAPI").build());
            
            Papel p4 = papelRepository.findByAuthority("ROLE_COORDENADOR_CURSO"); //
            if (p4 == null) p4 = papelRepository.save(Papel.builder().authority("ROLE_COORDENADOR_CURSO").build());

            Papel p5 = papelRepository.findByAuthority("ROLE_PROFESSOR"); //
            if (p5 == null) p5 = papelRepository.save(Papel.builder().authority("ROLE_PROFESSOR").build());

            // 2. Inserir Usuários (Idempotente e com atualização de senha)
            String senhaCriptografada = passwordEncoder.encode("123456");
            
            // --- INÍCIO DA CORREÇÃO ---
            // Usamos o novo método 'findUsuarioByEmail' que retorna a entidade Usuario
            Usuario u1 = usuarioRepository.findUsuarioByEmail("coordenador@naapi.com");
            // --- FIM DA CORREÇÃO ---
            
            if (u1 == null) {
                u1 = Usuario.builder()
                    .nome("Admin Coordenador")
                    .email("coordenador@naapi.com")
                    .papeis(Set.of(p1))
                    .build();
            }
            u1.setSenha(senhaCriptografada); // Garante que a senha é "123456"
            usuarioRepository.save(u1);
            
            // Repetir para os outros usuários
            Usuario u2 = usuarioRepository.findUsuarioByEmail("membro@naapi.com");
            if (u2 == null) {
                 u2 = Usuario.builder()
                    .nome("Membro Tecnico")
                    .email("membro@naapi.com")
                    .papeis(Set.of(p2))
                    .build();
            }
            u2.setSenha(senhaCriptografada);
            usuarioRepository.save(u2);
            
            Usuario u3 = usuarioRepository.findUsuarioByEmail("estagiario@naapi.com");
            if (u3 == null) {
                 u3 = Usuario.builder()
                    .nome("Estagiario 1 (Assistente)")
                    .email("estagiario@naapi.com")
                    .papeis(Set.of(p3))
                    .build();
            }
            u3.setSenha(senhaCriptografada);
            usuarioRepository.save(u3);
            
            return ResponseEntity.ok("SUCESSO: O banco de dados foi verificado e as senhas dos usuários de teste foram atualizadas.");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("ERRO AO TENTAR POPULAR O BANCO: " + e.getMessage());
        }
    }
}
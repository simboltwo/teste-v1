package com.naapi.naapi.controllers;

import com.naapi.naapi.entities.*;
import com.naapi.naapi.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    // Mudei para GetMapping para ser fácil de rodar no navegador
    @GetMapping("/seed-all")
    public ResponseEntity<String> seedDatabase() {
        
        // Verifica se já foi populado
        if (usuarioRepository.count() > 0) {
            return ResponseEntity.ok("O banco de dados já estava populado. Nenhum dado foi alterado.");
        }

        try {
            // 1. Inserir Papéis (Roles)
            Papel p1 = papelRepository.save(Papel.builder().authority("ROLE_COORDENADOR_NAAPI").build());
            Papel p2 = papelRepository.save(Papel.builder().authority("ROLE_MEMBRO_TECNICO").build());
            Papel p3 = papelRepository.save(Papel.builder().authority("ROLE_ESTAGIARIO_NAAPI").build());
            Papel p4 = papelRepository.save(Papel.builder().authority("ROLE_COORDENADOR_CURSO").build());
            Papel p5 = papelRepository.save(Papel.builder().authority("ROLE_PROFESSOR").build());

            // 2. Inserir Usuários (Senha para todos é "123456")
            String senhaCriptografada = passwordEncoder.encode("123456");
            
            Usuario u1 = Usuario.builder()
                .nome("Admin Coordenador")
                .email("coordenador@naapi.com")
                .senha(senhaCriptografada)
                .papeis(Set.of(p1))
                .build();
            
            Usuario u2 = Usuario.builder()
                .nome("Membro Tecnico")
                .email("membro@naapi.com")
                .senha(senhaCriptografada)
                .papeis(Set.of(p2))
                .build();

            Usuario u3 = Usuario.builder()
                .nome("Estagiario 1 (Assistente)")
                .email("estagiario@naapi.com")
                .senha(senhaCriptografada)
                .papeis(Set.of(p3))
                .build();
            
            usuarioRepository.saveAll(List.of(u1, u2, u3));

            // 3. Inserir Cursos, Turmas, Diagnósticos, Tipos de Atendimento
            Curso c1 = cursoRepository.save(Curso.builder().nome("Engenharia de Computação").build());
            Curso c2 = cursoRepository.save(Curso.builder().nome("Arquitetura").build());

            Turma t1 = turmaRepository.save(Turma.builder().nome("COM-2023").build());
            Turma t2 = turmaRepository.save(Turma.builder().nome("ARQ-2022").build());

            Diagnostico d1 = diagnosticoRepository.save(Diagnostico.builder().cid("F84.0").nome("Autismo").sigla("TEA").build());
            diagnosticoRepository.save(Diagnostico.builder().cid("F90.0").nome("Hiperatividade").sigla("TDAH").build());

            tipoAtendimentoRepository.save(TipoAtendimento.builder().nome("Assistência").build());
            tipoAtendimentoRepository.save(TipoAtendimento.builder().nome("Orientação de Estudos").build());

            // 4. Inserir Alunos
            Aluno a1 = Aluno.builder()
                .nome("Maria Silva")
                .matricula("123456")
                .prioridade("Baixa")
                .curso(c1)
                .turma(t1)
                .diagnosticos(Set.of(d1)) 
                .build();
            
            Aluno a2 = Aluno.builder()
                .nome("João Souza")
                .nomeSocial("Joana")
                .matricula("789012")
                .prioridade("Alta")
                .curso(c2)
                .turma(t2)
                .build();

            Aluno a3 = Aluno.builder()
                .nome("Carlos Pereira")
                .matricula("654321")
                .prioridade("Média")
                .curso(c1)
                .turma(t1)
                .build();

            alunoRepository.saveAll(List.of(a1, a2, a3));

            return ResponseEntity.ok("SUCESSO: O banco de dados de produção foi populado com os dados de teste!");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("ERRO AO POPULAR BANCO: " + e.getMessage());
        }
    }
}
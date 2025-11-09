package com.naapi.naapi.dtos;

import com.naapi.naapi.entities.Aluno;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlunoDTO {

    private Long id;
    private String nome;
    private String nomeSocial;
    private String matricula;
    private String foto;
    private String prioridade;
    private Boolean ativo;

    private CursoDTO curso;
    private TurmaDTO turma;

    @Builder.Default
    private Set<DiagnosticoDTO> diagnosticos = new HashSet<>();

    public AlunoDTO(Aluno entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.nomeSocial = entity.getNomeSocial();
        this.matricula = entity.getMatricula();
        this.foto = entity.getFoto();
        this.prioridade = entity.getPrioridade();
        this.ativo = entity.getAtivo();

        if (entity.getCurso() != null) {
            this.curso = new CursoDTO(entity.getCurso());
        }
        if (entity.getTurma() != null) {
            this.turma = new TurmaDTO(entity.getTurma());
        }
        this.diagnosticos = entity.getDiagnosticos().stream()
                .map(DiagnosticoDTO::new)
                .collect(Collectors.toSet());
    }
}
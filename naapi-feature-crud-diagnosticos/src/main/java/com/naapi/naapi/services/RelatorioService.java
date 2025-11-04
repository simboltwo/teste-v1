package com.naapi.naapi.services;

import com.naapi.naapi.dtos.*;
import com.naapi.naapi.repositories.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final AlunoRepository alunoRepository;

    private final AlunoService alunoService;
    private final LaudoService laudoService;
    private final PeiService peiService;

    @Transactional(readOnly = true)
    public List<RelatorioAlunosPorCursoDTO> getRelatorioAlunosPorCurso() {
        return alunoRepository.countAlunosPorCurso();
    }

    @Transactional(readOnly = true)
    public List<RelatorioAlunosPorDiagnosticoDTO> getRelatorioAlunosPorDiagnostico() {
        return alunoRepository.countAlunosPorDiagnostico();
    }

    @Transactional(readOnly = true)
    public RelatorioHistoricoAlunoDTO getRelatorioHistoricoAluno(Long alunoId) {
        
        AlunoDTO aluno = alunoService.findById(alunoId);

        List<LaudoDTO> laudos = laudoService.findByAlunoId(alunoId);

        List<PeiDTO> peis = peiService.findByAlunoId(alunoId);

        return RelatorioHistoricoAlunoDTO.builder()
                .aluno(aluno)
                .laudos(laudos)
                .peis(peis)
                .build();
    }
}
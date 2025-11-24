package com.naapi.naapi.services;

import com.naapi.naapi.dtos.*;
import com.naapi.naapi.repositories.AlunoRepository;
import com.naapi.naapi.repositories.AtendimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final AlunoRepository alunoRepository;
    private final AtendimentoRepository atendimentoRepository; // <-- 2. INJETAR

    private final AlunoService alunoService;
    private final LaudoService laudoService;
    private final PeiService peiService;

    @Transactional(readOnly = true)
    public RelatorioKpiDTO getTotalAlunosAtivos() {
        long total = alunoRepository.count(); 
        return new RelatorioKpiDTO(total);
    }

    // --- 3. ADICIONAR NOVO MÉTODO ---
    @Transactional(readOnly = true)
    public RelatorioKpiDTO getTotalAtendimentosPorStatus(String status) {
        // O frontend envia 'REALIZADO' ou 'AGENDADO'
        long total = atendimentoRepository.countByStatus(status.toUpperCase()); 
        return new RelatorioKpiDTO(total);
    }
    // --- FIM DA MUDANÇA ---

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

    @Transactional(readOnly = true)
    public RelatorioKpiDTO getTotalAtendimentosPorData(LocalDate data, String status) {
        LocalDateTime inicioDoDia = data.atStartOfDay();
        LocalDateTime fimDoDia = data.atTime(LocalTime.MAX);
        
        long total = atendimentoRepository.countByDataHoraBetweenAndStatus(inicioDoDia, fimDoDia, status.toUpperCase());
        return new RelatorioKpiDTO(total);
    }
}
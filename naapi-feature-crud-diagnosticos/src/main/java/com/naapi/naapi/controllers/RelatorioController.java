package com.naapi.naapi.controllers;

import com.naapi.naapi.dtos.RelatorioAlunosPorCursoDTO;
import com.naapi.naapi.dtos.RelatorioAlunosPorDiagnosticoDTO;
import com.naapi.naapi.dtos.RelatorioHistoricoAlunoDTO;
import com.naapi.naapi.dtos.RelatorioKpiDTO; 
import com.naapi.naapi.services.ExportacaoService;
import com.naapi.naapi.services.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService relatorioService;
    private final ExportacaoService exportacaoService;


    @GetMapping("/total-alunos-ativos")
    public ResponseEntity<RelatorioKpiDTO> getTotalAlunosAtivos() {
        RelatorioKpiDTO dto = relatorioService.getTotalAlunosAtivos();
        return ResponseEntity.ok(dto);
    }

    // --- INÍCIO DA MUDANÇA ---
    @GetMapping("/total-atendimentos")
    public ResponseEntity<RelatorioKpiDTO> getTotalAtendimentosPorStatus(@RequestParam(value = "status") String status) {
        RelatorioKpiDTO dto = relatorioService.getTotalAtendimentosPorStatus(status);
        return ResponseEntity.ok(dto);
    }
    // --- FIM DA MUDANÇA ---

    @GetMapping("/alunos-por-curso")
    public ResponseEntity<List<RelatorioAlunosPorCursoDTO>> getAlunosPorCurso() {
        List<RelatorioAlunosPorCursoDTO> list = relatorioService.getRelatorioAlunosPorCurso();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/alunos-por-diagnostico")
    public ResponseEntity<List<RelatorioAlunosPorDiagnosticoDTO>> getAlunosPorDiagnostico() {
        List<RelatorioAlunosPorDiagnosticoDTO> list = relatorioService.getRelatorioAlunosPorDiagnostico();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/historico-aluno/{alunoId}")
    public ResponseEntity<RelatorioHistoricoAlunoDTO> getHistoricoAluno(@PathVariable Long alunoId) {
        RelatorioHistoricoAlunoDTO relatorio = relatorioService.getRelatorioHistoricoAluno(alunoId);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/alunos-por-curso/csv")
    public ResponseEntity<String> exportarAlunosPorCursoCSV() {
        String csvData = exportacaoService.exportarAlunosPorCursoCSV();
        String filename = "relatorio_alunos_por_curso_" + LocalDate.now() + ".csv";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + "; charset=utf-8");
        return ResponseEntity.ok().headers(headers).body(csvData);
    }

    @GetMapping("/alunos-por-diagnostico/csv")
    public ResponseEntity<String> exportarAlunosPorDiagnosticoCSV() {
        String csvData = exportacaoService.exportarAlunosPorDiagnosticoCSV();
        String filename = "relatorio_alunos_por_diagnostico_" + LocalDate.now() + ".csv";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + "; charset=utf-8");
        return ResponseEntity.ok().headers(headers).body(csvData);
    }
}
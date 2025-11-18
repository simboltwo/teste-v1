/*
 * Arquivo: simboltwo/teste-v1/teste-v1-ac4c03749fe5021245d97adeb7c4827ee1afde3f/naapi-feature-crud-diagnosticos/src/main/java/com/naapi/naapi/controllers/RelatorioController.java
 * Descrição: Corrigidas as chamadas de método para 'getRelatorioAlunosPorCurso' e 'getRelatorioAlunosPorDiagnostico'.
 */
package com.naapi.naapi.controllers;

import com.naapi.naapi.dtos.RelatorioAlunosPorCursoDTO;
import com.naapi.naapi.dtos.RelatorioAlunosPorDiagnosticoDTO;
import com.naapi.naapi.dtos.RelatorioHistoricoAlunoDTO;
import com.naapi.naapi.dtos.RelatorioKpiDTO; 
import com.naapi.naapi.services.ExportacaoService;
import com.naapi.naapi.services.RelatorioService;
import com.naapi.naapi.services.PdfService;
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
    private final PdfService pdfService;


    @GetMapping("/total-alunos-ativos")
    public ResponseEntity<RelatorioKpiDTO> getTotalAlunosAtivos() {
        RelatorioKpiDTO dto = relatorioService.getTotalAlunosAtivos();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/total-atendimentos")
    public ResponseEntity<RelatorioKpiDTO> getTotalAtendimentosPorStatus(@RequestParam(value = "status") String status) {
        RelatorioKpiDTO dto = relatorioService.getTotalAtendimentosPorStatus(status);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/alunos-por-curso")
    public ResponseEntity<List<RelatorioAlunosPorCursoDTO>> getAlunosPorCurso() {
        // --- INÍCIO DA CORREÇÃO ---
        List<RelatorioAlunosPorCursoDTO> list = relatorioService.getRelatorioAlunosPorCurso();
        // --- FIM DA CORREÇÃO ---
        return ResponseEntity.ok(list);
    }

    @GetMapping("/alunos-por-diagnostico")
    public ResponseEntity<List<RelatorioAlunosPorDiagnosticoDTO>> getAlunosPorDiagnostico() {
        // --- INÍCIO DA CORREÇÃO ---
        List<RelatorioAlunosPorDiagnosticoDTO> list = relatorioService.getRelatorioAlunosPorDiagnostico();
        // --- FIM DA CORREÇÃO ---
        return ResponseEntity.ok(list);
    }

    @GetMapping("/historico-aluno/{alunoId}")
    public ResponseEntity<RelatorioHistoricoAlunoDTO> getHistoricoAluno(@PathVariable Long alunoId) {
        RelatorioHistoricoAlunoDTO relatorio = relatorioService.getRelatorioHistoricoAluno(alunoId);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/historico-aluno/{alunoId}/pdf")
    public ResponseEntity<byte[]> getHistoricoAlunoPdf(@PathVariable Long alunoId) {
        RelatorioHistoricoAlunoDTO dto = relatorioService.getRelatorioHistoricoAluno(alunoId);
        
        byte[] pdfBytes = pdfService.generateHistoricoPdf(dto);

        String filename = "Relatorio-Historico-" + dto.getAluno().getMatricula() + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
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
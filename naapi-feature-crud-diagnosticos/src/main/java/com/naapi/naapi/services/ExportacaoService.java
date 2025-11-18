/*
 * Arquivo: simboltwo/teste-v1/teste-v1-ac4c03749fe5021245d97adeb7c4827ee1afde3f/naapi-feature-crud-diagnosticos/src/main/java/com/naapi/naapi/services/ExportacaoService.java
 * Descrição: Corrigidas as chamadas de método para 'getRelatorioAlunosPorCurso' e 'getRelatorioAlunosPorDiagnostico'.
 */
package com.naapi.naapi.services;

import com.naapi.naapi.dtos.RelatorioAlunosPorCursoDTO;
import com.naapi.naapi.dtos.RelatorioAlunosPorDiagnosticoDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportacaoService {

    private final RelatorioService relatorioService;

    public String exportarAlunosPorCursoCSV() {
        // --- INÍCIO DA CORREÇÃO ---
        List<RelatorioAlunosPorCursoDTO> dados = relatorioService.getRelatorioAlunosPorCurso();
        // --- FIM DA CORREÇÃO ---
        
        StringWriter stringWriter = new StringWriter();
        
        String[] cabecalhos = {"Curso", "Total de Alunos"};

        try (CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT.withHeader(cabecalhos))) {
            for (RelatorioAlunosPorCursoDTO dto : dados) {
                csvPrinter.printRecord(dto.getCursoNome(), dto.getTotalAlunos());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar o ficheiro CSV: " + e.getMessage());
        }

        return stringWriter.toString();
    }

    public String exportarAlunosPorDiagnosticoCSV() {
        // --- INÍCIO DA CORREÇÃO ---
        List<RelatorioAlunosPorDiagnosticoDTO> dados = relatorioService.getRelatorioAlunosPorDiagnostico();
        // --- FIM DA CORREÇÃO ---
        StringWriter stringWriter = new StringWriter();
        String[] cabecalhos = {"Diagnostico", "Total de Alunos"};

        try (CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT.withHeader(cabecalhos))) {
            for (RelatorioAlunosPorDiagnosticoDTO dto : dados) {
                csvPrinter.printRecord(dto.getDiagnosticoNome(), dto.getTotalAlunos());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar o ficheiro CSV: " + e.getMessage());
        }

        return stringWriter.toString();
    }
}
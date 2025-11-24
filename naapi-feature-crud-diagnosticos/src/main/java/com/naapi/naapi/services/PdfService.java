package com.naapi.naapi.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.naapi.naapi.dtos.AlunoDTO;
import com.naapi.naapi.dtos.DiagnosticoDTO;
import com.naapi.naapi.dtos.HistoricoAcademicoDTO;
import com.naapi.naapi.dtos.LaudoDTO;
import com.naapi.naapi.dtos.PeiDTO;
import com.naapi.naapi.dtos.RelatorioHistoricoAlunoDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    private static final Font FONT_TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
    private static final Font FONT_SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
    private static final Font FONT_NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 10);
    private static final Font FONT_LABEL = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] generateHistoricoPdf(RelatorioHistoricoAlunoDTO dto) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            // 1. Título
            Paragraph titulo = new Paragraph("Relatório Histórico do Aluno", FONT_TITULO);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20f);
            document.add(titulo);

            // 2. Dados do Aluno (Essenciais)
            addDadosAluno(document, dto.getAluno());
            
            // 3. Histórico Acadêmico (Trajetória Completa)
            addHistoricoAcademico(document, dto);

            // 4. Observações Internas NAAPI (Campos de texto do Aluno)
            addObservacoesInternas(document, dto.getAluno());

            // 5. Diagnósticos
            addDiagnosticos(document, dto.getAluno());

            // 6. Laudos
            addLaudos(document, dto);

            // 7. PEIs
            addPeis(document, dto);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            // Em um app real, teríamos um tratamento de exceção mais robusto
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }

    /**
     * MUDANÇA: Simplificada para mostrar apenas dados atuais de contato/identificação.
     */
    private void addDadosAluno(Document document, AlunoDTO aluno) throws DocumentException {
        document.add(new Paragraph("Dados do Aluno (Registro Atual)", FONT_SUBTITULO));
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        table.addCell(createLabelCell("Nome:"));
        table.addCell(createValueCell(aluno.getNome()));

        if (aluno.getNomeSocial() != null && !aluno.getNomeSocial().isBlank()) {
            table.addCell(createLabelCell("Nome Social:"));
            table.addCell(createValueCell(aluno.getNomeSocial()));
        }

        table.addCell(createLabelCell("Matrícula:"));
        table.addCell(createValueCell(aluno.getMatricula()));

        table.addCell(createLabelCell("CPF:"));
        table.addCell(createValueCell(aluno.getCpf()));
        
        table.addCell(createLabelCell("Prioridade Atual:"));
        table.addCell(createValueCell(aluno.getPrioridade()));

        document.add(table);
    }
    
    /**
     * MUDANÇA: Melhorada a formatação do Histórico Acadêmico.
     */
    private void addHistoricoAcademico(Document document, RelatorioHistoricoAlunoDTO dto) throws DocumentException {
        document.add(Chunk.NEWLINE);
        Paragraph subtitulo = new Paragraph("Histórico Acadêmico (Trajetória)", FONT_SUBTITULO);
        subtitulo.setSpacingBefore(10f);
        document.add(subtitulo);
        document.add(Chunk.NEWLINE);

        if (dto.getHistoricoAcademico() == null || dto.getHistoricoAcademico().isEmpty()) {
            document.add(new Paragraph("Nenhum registro de histórico acadêmico encontrado.", FONT_NORMAL));
            return;
        }

        for (HistoricoAcademicoDTO hist : dto.getHistoricoAcademico()) {
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.setSpacingAfter(10f);

            // Linha de Cabeçalho (Curso e Turma)
            String headerText = hist.getCurso().getNome() + " | " + (hist.getTurma() != null ? hist.getTurma().getNome() : "N/D");
            PdfPCell header = createLabelCell(headerText);
            header.setBackgroundColor(java.awt.Color.decode("#CCE5FF")); // Azul claro para destaque
            header.setBorder(PdfPCell.BOTTOM);
            header.setBorderColor(java.awt.Color.decode("#00376b"));
            header.setBorderWidthBottom(1f);
            table.addCell(header);
            
            // Linha de Detalhe (Período)
            String periodoStr = hist.getDataInicio().format(DATE_FORMAT);
            if (hist.getDataFim() != null) {
                 periodoStr += " até " + hist.getDataFim().format(DATE_FORMAT);
            } else {
                 periodoStr += " (Período Atual)";
            }
            table.addCell(createValueCell("Duração: " + periodoStr));
            
            document.add(table);
        }
    }
    
    /**
     * NOVO: Adiciona a seção para os campos de texto longos de uso interno.
     */
    private void addObservacoesInternas(Document document, AlunoDTO aluno) throws DocumentException {
        document.add(Chunk.NEWLINE);
        Paragraph subtitulo = new Paragraph("Observações Internas NAAPI", FONT_SUBTITULO);
        subtitulo.setSpacingBefore(10f);
        document.add(subtitulo);
        document.add(Chunk.NEWLINE);

        if (aluno.getAdaptacoesNecessarias() == null && aluno.getNecessidadesRelatoriosMedicos() == null && aluno.getAnotacoesNaapi() == null) {
            document.add(new Paragraph("Nenhuma observação interna registrada no cadastro do aluno.", FONT_NORMAL));
            return;
        }

        // 1. Adaptações Necessárias
        document.add(createLabelParagraph("Adaptações Necessárias:"));
        document.add(createValueParagraph(aluno.getAdaptacoesNecessarias()));

        // 2. Necessidades (Baseadas em Relatórios Médicos)
        document.add(createLabelParagraph("Necessidades (Relatórios Médicos):"));
        document.add(createValueParagraph(aluno.getNecessidadesRelatoriosMedicos()));

        // 3. Anotações Gerais (Uso Interno)
        document.add(createLabelParagraph("Anotações Gerais (Uso Interno):"));
        document.add(createValueParagraph(aluno.getAnotacoesNaapi()));
    }
    // --- FIM NOVO MÉTODO ---


    private void addDiagnosticos(Document document, AlunoDTO aluno) throws DocumentException {
        document.add(Chunk.NEWLINE);
        Paragraph subtitulo = new Paragraph("Diagnósticos Vinculados", FONT_SUBTITULO);
        subtitulo.setSpacingBefore(10f);
        document.add(subtitulo);
        document.add(Chunk.NEWLINE);

        if (aluno.getDiagnosticos() == null || aluno.getDiagnosticos().isEmpty()) {
            document.add(new Paragraph("Nenhum diagnóstico vinculado.", FONT_NORMAL));
            return;
        }

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.addCell(createHeaderCell("Nome"));
        table.addCell(createHeaderCell("Sigla"));
        table.addCell(createHeaderCell("CID"));

        for (DiagnosticoDTO diag : aluno.getDiagnosticos()) {
            table.addCell(createValueCell(diag.getNome()));
            table.addCell(createValueCell(diag.getSigla()));
            table.addCell(createValueCell(diag.getCid()));
        }
        document.add(table);
    }

    private void addLaudos(Document document, RelatorioHistoricoAlunoDTO dto) throws DocumentException {
        document.add(Chunk.NEWLINE);
        Paragraph subtitulo = new Paragraph("Laudos e Documentos", FONT_SUBTITULO);
        subtitulo.setSpacingBefore(10f);
        document.add(subtitulo);
        document.add(Chunk.NEWLINE);

        if (dto.getLaudos() == null || dto.getLaudos().isEmpty()) {
            document.add(new Paragraph("Nenhum laudo anexado.", FONT_NORMAL));
            return;
        }

        for (LaudoDTO laudo : dto.getLaudos()) {
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.setSpacingAfter(10f);

            String data = laudo.getDataEmissao() != null ? laudo.getDataEmissao().format(DATE_FORMAT) : "N/D";
            String desc = laudo.getDescricao() != null ? laudo.getDescricao() : "Sem descrição";

            table.addCell(createLabelCell("Descrição: " + desc + " (Data: " + data + ")"));
            
            document.add(table);
        }
    }

    private void addPeis(Document document, RelatorioHistoricoAlunoDTO dto) throws DocumentException {
        document.add(Chunk.NEWLINE);
        Paragraph subtitulo = new Paragraph("Planos Educacionais Individuais (PEI)", FONT_SUBTITULO);
        subtitulo.setSpacingBefore(10f);
        document.add(subtitulo);
        document.add(Chunk.NEWLINE);

        if (dto.getPeis() == null || dto.getPeis().isEmpty()) {
            document.add(new Paragraph("Nenhum PEI registrado.", FONT_NORMAL));
            return;
        }

        for (PeiDTO pei : dto.getPeis()) {
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.setSpacingAfter(15f);

            String dataInicio = pei.getDataInicio().format(DATE_FORMAT);
            PdfPCell header = createLabelCell("PEI iniciado em: " + dataInicio + " (Responsável: " + pei.getResponsavelNome() + ")");
            header.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
            table.addCell(header);

            table.addCell(createLabelCell("Metas:"));
            table.addCell(createValueCell(pei.getMetas()));
            
            table.addCell(createLabelCell("Estratégias:"));
            table.addCell(createValueCell(pei.getEstrategias()));

            if (pei.getAvaliacao() != null && !pei.getAvaliacao().isBlank()) {
                table.addCell(createLabelCell("Avaliação:"));
                table.addCell(createValueCell(pei.getAvaliacao()));
            }

            document.add(table);
        }
    }


    // --- Helpers de Célula e Parágrafo ---

    private Paragraph createLabelParagraph(String text) {
        Paragraph p = new Paragraph(text, FONT_LABEL);
        p.setSpacingBefore(8f);
        p.setSpacingAfter(2f);
        return p;
    }

    private Paragraph createValueParagraph(String text) {
        if (text == null || text.isBlank()) {
            text = "N/D";
        }
        Paragraph p = new Paragraph(text, FONT_NORMAL);
        p.setIndentationLeft(10f);
        p.setSpacingAfter(5f);
        return p;
    }

    private PdfPCell createLabelCell(String text) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, FONT_LABEL));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPadding(4);
        return cell;
    }

    private PdfPCell createValueCell(String text) {
        PdfPCell cell = new PdfPCell(new Paragraph(text != null ? text : "", FONT_NORMAL));
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPadding(4);
        return cell;
    }

    private PdfPCell createHeaderCell(String text) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, FONT_LABEL));
        cell.setBackgroundColor(java.awt.Color.decode("#EEEEEE"));
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }
}
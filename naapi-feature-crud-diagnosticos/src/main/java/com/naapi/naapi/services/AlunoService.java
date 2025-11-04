package com.naapi.naapi.services;

import com.naapi.naapi.dtos.AlunoDTO;
import com.naapi.naapi.dtos.AlunoInsertDTO;
import com.naapi.naapi.dtos.ResponsavelInsertDTO; // Importado
import com.naapi.naapi.entities.*; // Importado
import com.naapi.naapi.repositories.*; // Importado
import com.naapi.naapi.services.exceptions.BusinessException;
import com.naapi.naapi.services.specifications.AlunoSpecifications;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile; // Importado

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository repository;

    private final CursoRepository cursoRepository;
    private final TurmaRepository turmaRepository;
    private final DiagnosticoRepository diagnosticoRepository;
    
    // --- NOVAS DEPENDÊNCIAS INJETADAS ---
    private final FileStorageService fileStorageService;
    private final TipoAtendimentoRepository tipoAtendimentoRepository;
    private final UsuarioRepository usuarioRepository;

    private static final Set<String> PREPOSICOES_CURTAS = Set.of(
            "e", "de", "do", "da", "dos", "das", "em", "no", "na", "nos", "nas"
    );

    @Transactional(readOnly = true)
    public List<AlunoDTO> findAll(String nome, String matricula, Long cursoId, Long turmaId, Long diagnosticoId) {
        
        Specification<Aluno> spec = Specification.where(null);

        if (nome != null && !nome.isBlank()) {
            spec = spec.and(AlunoSpecifications.hasNome(nome));
        }
        if (matricula != null && !matricula.isBlank()) {
            spec = spec.and(AlunoSpecifications.hasMatricula(matricula));
        }
        if (cursoId != null) {
            spec = spec.and(AlunoSpecifications.hasCursoId(cursoId));
        }
        if (turmaId != null) {
            spec = spec.and(AlunoSpecifications.hasTurmaId(turmaId));
        }
        if (diagnosticoId != null) {
            spec = spec.and(AlunoSpecifications.hasDiagnosticoId(diagnosticoId));
        }

        List<Aluno> list = repository.findAll(spec);
        return list.stream().map(AlunoDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AlunoDTO findById(Long id) {
        Aluno entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));
        return new AlunoDTO(entity);
    }

    // --- ASSINATURA DO MÉTODO MODIFICADA ---
    @Transactional
    public AlunoDTO insert(AlunoInsertDTO dto, MultipartFile foto) {
        verificarUnicidade(dto, -1L); 
        
        Aluno entity = new Aluno();
        
        // 1. Salva a foto (se existir) e obtém a URL
        String fotoUrl = fileStorageService.saveFile(foto, "fotos");
        entity.setFoto(fotoUrl);
        
        // 2. Copia o resto dos dados
        copyDtoToEntity(dto, entity);
        entity.setAtivo(true);
        
        // 3. Salva (Cascade salvará os responsáveis)
        entity = repository.save(entity);
        return new AlunoDTO(entity);
    }

    // --- ASSINATURA DO MÉTODO MODIFICADA ---
    @Transactional
    public AlunoDTO update(Long id, AlunoInsertDTO dto, MultipartFile foto) {
        Aluno entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));

        verificarUnicidade(dto, id); 

        // 1. Salva a nova foto (se enviada)
        String fotoUrl = fileStorageService.saveFile(foto, "fotos");
        if (fotoUrl != null) {
            // TODO: Adicionar lógica para deletar a foto antiga do storage
            entity.setFoto(fotoUrl);
        }
        
        // 2. Copia o resto dos dados
        copyDtoToEntity(dto, entity);
        
        // 3. Salva (Cascade salvará/atualizará/removerá responsáveis)
        entity = repository.save(entity);
        return new AlunoDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        Aluno entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));
        
        entity.setAtivo(false);
        repository.save(entity);
    }

    private void verificarUnicidade(AlunoInsertDTO dto, Long id) {
        if (repository.existsByMatriculaAndIdNot(dto.getMatricula(), id)) {
            throw new BusinessException("A matrícula '" + dto.getMatricula() + "' já está cadastrada.");
        }
        
        if (dto.getCpf() != null && !dto.getCpf().isBlank()) {
            if (repository.existsByCpfAndIdNot(dto.getCpf(), id)) {
                throw new BusinessException("O CPF '" + dto.getCpf() + "' já está cadastrado.");
            }
        }
        
        if (dto.getProcessoSipac() != null && !dto.getProcessoSipac().isBlank()) {
            if (repository.existsByProcessoSipacAndIdNot(dto.getProcessoSipac(), id)) {
                throw new BusinessException("O Processo Sipac '" + dto.getProcessoSipac() + "' já está cadastrado.");
            }
        }
    }

    // --- MÉTODO copyDtoToEntity TOTALMENTE REFATORADO ---
    private void copyDtoToEntity(AlunoInsertDTO dto, Aluno entity) {
        
        // Bloco Pessoal (Simples)
        entity.setNome(dto.getNome());
        entity.setNomeSocial(dto.getNomeSocial());
        entity.setMatricula(dto.getMatricula());
        entity.setCpf(dto.getCpf());
        entity.setDataNascimento(dto.getDataNascimento());
        entity.setSerie(dto.getSerie());
        entity.setPrioridade(dto.getPrioridade());
        entity.setNomeProtegido(gerarNomeProtegido(dto.getNome())); 
        entity.setTelefoneEstudante(dto.getTelefoneEstudante());

        // Bloco Acadêmico (Relacionamentos)
        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com ID: ".concat(dto.getCursoId().toString())));
        entity.setCurso(curso);

        Turma turma = turmaRepository.findById(dto.getTurmaId())
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com ID: ".concat(dto.getTurmaId().toString())));
        entity.setTurma(turma);

        // Bloco NAAPI (Simples)
        entity.setProvaOutroEspaco(dto.getProvaOutroEspaco());
        entity.setPossuiPEI(dto.getPossuiPEI());
        entity.setProcessoSipac(dto.getProcessoSipac());
        entity.setAnotacoesNaapi(dto.getAnotacoesNaapi());
        entity.setAdaptacoesNecessarias(dto.getAdaptacoesNecessarias()); // Campo Corrigido
        entity.setNecessidadesRelatoriosMedicos(dto.getNecessidadesRelatoriosMedicos());
        entity.setDataUltimoLaudo(dto.getDataUltimoLaudo());

        // Bloco NAAPI (Relacionamentos por ID)
        if (dto.getTipoAtendimentoPrincipalId() != null) {
            TipoAtendimento ta = tipoAtendimentoRepository.findById(dto.getTipoAtendimentoPrincipalId())
                    .orElseThrow(() -> new EntityNotFoundException("Tipo de Atendimento não encontrado: " + dto.getTipoAtendimentoPrincipalId()));
            entity.setTipoAtendimentoPrincipal(ta);
        } else {
            entity.setTipoAtendimentoPrincipal(null);
        }

        if (dto.getAssistenteReferenciaId() != null) {
            Usuario u = usuarioRepository.findById(dto.getAssistenteReferenciaId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuário (Assistente) não encontrado: " + dto.getAssistenteReferenciaId()));
            entity.setAssistenteReferencia(u);
        } else {
            entity.setAssistenteReferencia(null);
        }

        if (dto.getMembroNaapiReferenciaId() != null) {
            Usuario u = usuarioRepository.findById(dto.getMembroNaapiReferenciaId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuário (Membro NAAPI) não encontrado: " + dto.getMembroNaapiReferenciaId()));
            entity.setMembroNaapiReferencia(u);
        } else {
            entity.setMembroNaapiReferencia(null);
        }

        // Bloco Diagnósticos (ManyToMany)
        entity.getDiagnosticos().clear();
        if (dto.getDiagnosticosId() != null) {
            for (Long diagId : dto.getDiagnosticosId()) {
                Diagnostico diag = diagnosticoRepository.findById(diagId)
                        .orElseThrow(() -> new EntityNotFoundException("Diagnóstico não encontrado com ID: ".concat(diagId.toString())));
                entity.getDiagnosticos().add(diag);
            }
        }
        
        // --- NOVO BLOCO: Responsáveis (OneToMany com Cascade) ---
        entity.getResponsaveis().clear(); // Limpa a lista antiga (orphanRemoval=true)
        if (dto.getResponsaveis() != null) {
            for (ResponsavelInsertDTO respDto : dto.getResponsaveis()) {
                Responsavel r = Responsavel.builder()
                        .nome(respDto.getNome())
                        .parentesco(respDto.getParentesco())
                        .telefone(respDto.getTelefone())
                        .autorizadoBuscar(respDto.getAutorizadoBuscar())
                        .aluno(entity) // Associa o responsável ao aluno
                        .build();
                entity.getResponsaveis().add(r);
            }
        }
    }
    
    /**
     * Gera um nome protegido (ex: "Adriano de Oliveira Carvalho" -> "Adr**** de Oli***** Car*****")
     * Preserva preposições curtas.
     */
    private String gerarNomeProtegido(String nomeCompleto) {
        if (nomeCompleto == null || nomeCompleto.isBlank()) {
            return null;
        }

        String[] palavras = nomeCompleto.split("\\s+");
        StringBuilder nomeProtegido = new StringBuilder();

        for (String palavra : palavras) {
            if (palavra.isBlank()) continue;

            // Se for uma preposição curta, mantém
            if (PREPOSICOES_CURTAS.contains(palavra.toLowerCase())) {
                nomeProtegido.append(palavra).append(" ");
            } 
            // Se for uma palavra maior, censura
            else {
                int tamanho = palavra.length();
                int mostrar = Math.min(tamanho, 3); // Mostra até 3 letras

                nomeProtegido.append(palavra.substring(0, mostrar));
                for (int i = mostrar; i < tamanho; i++) {
                    nomeProtegido.append("*");
                }
                nomeProtegido.append(" ");
            }
        }

        return nomeProtegido.toString().trim(); // Remove o último espaço
    }
}
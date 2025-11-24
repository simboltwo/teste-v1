package com.naapi.naapi.dtos;

import com.naapi.naapi.entities.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    private String nome;

    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "Formato de email inválido.")
    private String email;

    @Builder.Default
    private Set<PapelDTO> papeis = new HashSet<>();

    @Builder.Default
    private Set<TurmaDTO> turmasLecionadas = new HashSet<>();

    public UsuarioDTO(Usuario entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.email = entity.getEmail();
        this.papeis = entity.getPapeis().stream().map(PapelDTO::new).collect(Collectors.toSet());

        if (entity.getTurmasLecionadas() != null) {
            this.turmasLecionadas = entity.getTurmasLecionadas().stream().map(TurmaDTO::new).collect(Collectors.toSet());
        }
    }
}
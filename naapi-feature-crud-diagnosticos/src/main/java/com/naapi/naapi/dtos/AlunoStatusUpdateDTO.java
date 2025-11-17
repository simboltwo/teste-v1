package com.naapi.naapi.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

// DTO leve apenas para atualizações de status inline
@Data
@NoArgsConstructor
public class AlunoStatusUpdateDTO {
    // Usamos Wrappers (Boolean, String) para que possam ser nulos
    // Assim, o frontend pode enviar só o campo que mudou.
    private String prioridade;
    private Boolean provaOutroEspaco;
}
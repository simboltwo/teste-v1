package com.naapi.naapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioKpiDTO {
    
    // O 'long' (primitivo) garante que nunca será nulo
    private long total; 
}
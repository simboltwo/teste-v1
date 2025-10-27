package com.naapi.naapi.dtos;

import com.naapi.naapi.entities.Papel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PapelDTO {

    private Long id;
    private String authority;

    public PapelDTO(Papel entity) {
        this.id = entity.getId();
        this.authority = entity.getAuthority();
    }
}
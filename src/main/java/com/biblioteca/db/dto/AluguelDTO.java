package com.biblioteca.db.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class AluguelDTO {

    public Long id;
    public Date dataRetirada;
    public Date dataDevolucao;
    public Long locatarioId; // Referência ao ID do locatário
    public Set<Long> livrosIds;
}

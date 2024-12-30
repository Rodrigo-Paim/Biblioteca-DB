package com.biblioteca.db.dto;

import com.biblioteca.db.model.Livro;
import com.biblioteca.db.model.Locatario;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AluguelDTO {

    @NotNull
    public Long id;

    public Date dataRetirada;
    public Date dataDevolucao;
    public Long locatarioId;
    public Set<Long> livrosIds;

    public AluguelDTO(Long id, Date dataRetirada, Date dataDevolucao, Locatario locatario, Set<Livro> livros) {
    }
}

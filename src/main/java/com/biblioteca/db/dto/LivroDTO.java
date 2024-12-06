package com.biblioteca.db.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class LivroDTO {

    public Long id;
    public String nome;
    public String isbn;
    public Date dataPublicacao;
    public Set<Long> autoresIds;
}

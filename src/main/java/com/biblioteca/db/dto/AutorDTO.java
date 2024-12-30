package com.biblioteca.db.dto;

import com.biblioteca.db.model.Livro;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AutorDTO {

    public Long id;
    public String nome;
    public String sexo;
    public Integer anoNascimento;
    public String cpf;
}

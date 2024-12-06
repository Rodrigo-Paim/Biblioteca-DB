package com.biblioteca.db.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LocatarioDTO {

    public Long id;
    public String nome;
    public String sexo;
    public String telefone;
    public String email;
    public Date dataNascimento;
    public String cpf;

}

package com.biblioteca.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@Entity
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String nome;

    public String sexo;

    @Column(nullable = false)
    public Integer anoNascimento;

    @Column(unique = true, nullable = false)
    public String cpf;

    @ManyToMany(mappedBy = "autores")
    public Set<Livro> livros;

}

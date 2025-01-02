package com.biblioteca.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
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

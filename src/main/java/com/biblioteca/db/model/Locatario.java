package com.biblioteca.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Locatario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String nome;

    public String sexo;

    @Column(nullable = false)
    public String telefone;

    @Column(unique = true, nullable = false)
    public String email;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    public Date dataNascimento;

    @Column(unique = true, nullable = false)
    public String cpf;

    public Set<Livro> livros;

}

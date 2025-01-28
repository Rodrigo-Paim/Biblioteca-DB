package com.biblioteca.db.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String nome;

    @Column(unique = true, nullable = false)
    public String isbn;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    public Date dataPublicacao;

    @Column(nullable = false)
    public boolean isAlugado;

    @ManyToMany
    @JoinTable(
            name = "autor_livro",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    public Set<Autor> autores = new HashSet<>();

}

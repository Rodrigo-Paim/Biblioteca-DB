package com.biblioteca.db.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Setter
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date dataRetirada;

    @Temporal(TemporalType.DATE)
    private Date dataDevolucao;

    @ManyToOne
    @JoinColumn(name = "locatario_id", nullable = false)
    private Locatario locatario;

    @ManyToMany
    @JoinTable(
            name = "aluguel_livro",
            joinColumns = @JoinColumn(name = "aluguel_id"),
            inverseJoinColumns = @JoinColumn(name = "livro_id")
    )
    private Set<Livro> livros;

    public Aluguel(Long locatarioId, Date dataRetirada, Date dataDevolucao, Long locatarioId1, Set<Long> livrosIds) {
    }
}

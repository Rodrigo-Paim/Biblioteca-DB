package com.biblioteca.db.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    public Date dataRetirada;

    @Temporal(TemporalType.DATE)
    public Date dataDevolucao;

    @ManyToOne
    @JoinColumn(name = "locatario_id", nullable = false)
    public Locatario locatario;

    @ManyToMany
    @JoinTable(
            name = "aluguel_livro",
            joinColumns = @JoinColumn(name = "aluguel_id"),
            inverseJoinColumns = @JoinColumn(name = "livro_id")
    )
    public Set<Livro> livros;
}

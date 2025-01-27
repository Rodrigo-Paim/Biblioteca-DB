package com.biblioteca.db.repository;

import com.biblioteca.db.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByAutores_Id(Long autorId);

    List<Livro> findByIdNotIn(List<Long> alugadosIds);

    boolean existsByIsbn(String isbn);

    List<Livro> findByAlugadoFalse();

    List<Livro> findByAlugadoTrue();

    List<Livro> findByLocatarioId(Long locatarioId);

    List<Livro> findByAutorNome(String nome);
}

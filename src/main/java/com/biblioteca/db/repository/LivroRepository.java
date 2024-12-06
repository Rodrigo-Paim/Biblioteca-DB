package com.biblioteca.db.repository;

import com.biblioteca.db.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByAutores_Id(Long autorId);

    // Busca livros que não estão associados a nenhum aluguel
    List<Livro> findByIdNotIn(List<Long> alugadosIds);

    // Verifica se já existe um livro com o ISBN
    boolean existsByIsbn(String isbn);
}

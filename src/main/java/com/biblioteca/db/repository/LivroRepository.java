package com.biblioteca.db.repository;

import com.biblioteca.db.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    
    List<Livro> findByAutoresNome(String nomeAutor);

    List<Livro> findByAlugueisIsNull();
}
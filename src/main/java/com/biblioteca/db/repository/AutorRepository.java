package com.biblioteca.db.repository;


import com.biblioteca.db.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    
	Optional<Autor> findByNome(String nome);
}


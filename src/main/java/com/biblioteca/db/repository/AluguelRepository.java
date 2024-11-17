package com.biblioteca.db.repository;

import com.biblioteca.db.model.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
    
    List<Aluguel> findByLocatarioId(Long locatarioId);
}


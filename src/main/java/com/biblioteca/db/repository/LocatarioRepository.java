package com.biblioteca.db.repository;

import com.biblioteca.db.model.Locatario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocatarioRepository extends JpaRepository<Locatario, Long> {

    boolean existsByCpf(String cpf);

    // Verifica se já existe um locatário com o email
    boolean existsByEmail(String email);
}


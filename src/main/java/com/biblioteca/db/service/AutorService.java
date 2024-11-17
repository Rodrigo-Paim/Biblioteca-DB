package com.biblioteca.db.service;


import com.biblioteca.db.model.Autor;
import com.biblioteca.db.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public Autor save(Autor autor) {
        return autorRepository.save(autor);
    }

    public Optional<Autor> findById(Long id) {
        return autorRepository.findById(id);
    }

    public List<Autor> findAll() {
        return autorRepository.findAll();
    }

    public Optional<Autor> findByNome(String nome) {
        return autorRepository.findByNome(nome);
    }

    public void delete(Long id) {
        Optional<Autor> autor = autorRepository.findById(id);
        if (autor.isPresent() && autor.get().getLivros().isEmpty()) {
            autorRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Autor possui livros associados e não pode ser excluído.");
        }
    }
}


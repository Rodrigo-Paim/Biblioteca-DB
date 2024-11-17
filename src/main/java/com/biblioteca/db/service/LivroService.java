package com.biblioteca.db.service;

import com.biblioteca.db.model.Livro;
import com.biblioteca.db.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    public Livro save(Livro livro) {
        return livroRepository.save(livro);
    }

    public Optional<Livro> findById(Long id) {
        return livroRepository.findById(id);
    }

    public List<Livro> findAll() {
        return livroRepository.findAll();
    }

    public List<Livro> findByAutorNome(String nomeAutor) {
        return livroRepository.findByAutoresNome(nomeAutor);
    }

    public List<Livro> findAvailable() {
        return livroRepository.findByAlugueisIsNull();
    }

    public void delete(Long id) {
        Optional<Livro> livro = livroRepository.findById(id);
        if (livro.isPresent() && (livro.get().getAlugueis() == null || livro.get().getAlugueis().isEmpty())) {
            livroRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Livro possui aluguéis associados e não pode ser excluído.");
        }
    }
}


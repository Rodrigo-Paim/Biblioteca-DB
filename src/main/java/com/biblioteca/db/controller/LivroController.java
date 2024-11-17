package com.biblioteca.db.controller;

import com.biblioteca.db.model.Livro;
import com.biblioteca.db.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    @PostMapping
    public Livro createLivro(@RequestBody Livro livro) {
        return livroService.save(livro);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> getLivroById(@PathVariable Long id) {
        return livroService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Livro> getAllLivros() {
        return livroService.findAll();
    }

    @GetMapping("/disponiveis")
    public List<Livro> getLivrosDisponiveis() {
        return livroService.findAvailable();
    }

    @GetMapping("/buscar")
    public List<Livro> getLivrosByAutorNome(@RequestParam String nomeAutor) {
        return livroService.findByAutorNome(nomeAutor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivro(@PathVariable Long id) {
        try {
            livroService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}


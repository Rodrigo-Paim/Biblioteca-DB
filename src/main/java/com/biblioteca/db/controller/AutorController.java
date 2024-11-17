package com.biblioteca.db.controller;

import com.biblioteca.db.model.Autor;
import com.biblioteca.db.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @PostMapping
    public Autor createAutor(@RequestBody Autor autor) {
        return autorService.save(autor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autor> getAutorById(@PathVariable Long id) {
        return autorService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Autor> getAllAutores() {
        return autorService.findAll();
    }

    @GetMapping("/buscar")
    public ResponseEntity<Autor> getAutorByNome(@RequestParam String nome) {
        return autorService.findByNome(nome)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAutor(@PathVariable Long id) {
        try {
            autorService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}


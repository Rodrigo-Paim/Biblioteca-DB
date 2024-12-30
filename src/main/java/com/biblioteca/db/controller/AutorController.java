package com.biblioteca.db.controller;

import com.biblioteca.db.dto.AutorDTO;
import com.biblioteca.db.model.Autor;
import com.biblioteca.db.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/autores")
@Validated
public class AutorController {

    @Autowired
    private AutorService autorService;

    @PostMapping
    public ResponseEntity<AutorDTO> criarAutor(@RequestBody @Valid AutorDTO autorDTO) {
        AutorDTO criado = autorService.criarAutor(autorDTO);
        return ResponseEntity.status(201).body(criado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorDTO> buscarPorId(@PathVariable Long id) {
        AutorDTO autor = autorService.buscarPorId(id);
        return ResponseEntity.ok(autor);
    }

    @GetMapping
    public ResponseEntity<List<AutorDTO>> listarTodos() {
        List<AutorDTO> autores = autorService.listarTodos();
        return ResponseEntity.ok(autores);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAutor(@PathVariable Long id) {
        autorService.excluirAutor(id);
        return ResponseEntity.noContent().build();
    }
}


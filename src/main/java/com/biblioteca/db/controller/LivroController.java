package com.biblioteca.db.controller;

import com.biblioteca.db.dto.LivroDTO;
import com.biblioteca.db.model.Livro;
import com.biblioteca.db.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("v1/api/livros")
@Validated
public class LivroController {

    @Autowired
    private LivroService livroService;

    @PostMapping
    public ResponseEntity<LivroDTO> criarLivro(@RequestBody @Valid LivroDTO livroDTO) {
        LivroDTO criado = livroService.criarLivro(livroDTO);
        return ResponseEntity.status(201).body(criado);
    }

    @GetMapping("findBook/{id}")
    public ResponseEntity<LivroDTO> buscarLivroPorId(@PathVariable Long id) {
        LivroDTO livro = livroService.buscarPorId(id);
        return ResponseEntity.ok(livro);
    }

    @GetMapping
    public ResponseEntity<List<LivroDTO>> listarTodosOsLivros() {
        List<LivroDTO> livros = livroService.listarTodos();
        return ResponseEntity.ok(livros);
    }

    @DeleteMapping("excludeBook/{id}")
    public ResponseEntity<Void> excluirLivro(@PathVariable Long id) {
        livroService.excluirLivro(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("updateBook/{id}")
    public ResponseEntity<LivroDTO> atualizarLivro(@PathVariable Long id, @RequestBody LivroDTO livroAtualizado) {
        LivroDTO livro = livroService.atualizarLivro(id, livroAtualizado);
        return ResponseEntity.ok(livro);
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<LivroDTO>> listarLivrosDisponiveis() {
        List<LivroDTO> livrosDisponiveis = livroService.listarLivrosDisponiveis();
        return ResponseEntity.ok(livrosDisponiveis);
    }

    @GetMapping("/alugados")
    public ResponseEntity<List<LivroDTO>> listarLivrosAlugados() {
        List<LivroDTO> livrosAlugados = livroService.listarLivrosAlugados();
        return ResponseEntity.ok(livrosAlugados);
    }

    @GetMapping("/autor")
    public ResponseEntity<List<LivroDTO>> listarLivrosPorAutor(@RequestParam String nomeAutor) {
        List<LivroDTO> livrosPorAutor = livroService.listarLivrosPorAutor(nomeAutor);
        return ResponseEntity.ok(livrosPorAutor);
    }

    @GetMapping("/locatario/{locatarioId}")
    public ResponseEntity<List<LivroDTO>> listarLivrosPorLocatario(@PathVariable Long locatarioId) {
        List<LivroDTO> livrosPorLocatario = livroService.listarLivrosPorLocatario(locatarioId);
        return ResponseEntity.ok(livrosPorLocatario);
    }
}


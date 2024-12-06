package com.biblioteca.db.controller;

import com.biblioteca.db.dto.AluguelDTO;
import com.biblioteca.db.model.Aluguel;
import com.biblioteca.db.service.AluguelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alugueis")
@Validated
public class AluguelController {

    @Autowired
    private AluguelService aluguelService;

    @PostMapping
    public ResponseEntity<AluguelDTO> criarAluguel(@RequestBody @Valid AluguelDTO aluguelDTO) {
        AluguelDTO criado = aluguelService.criarAluguel(aluguelDTO);
        return ResponseEntity.status(201).body(criado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AluguelDTO> buscarPorId(@PathVariable Long id) {
        AluguelDTO aluguel = aluguelService.buscarPorId(id);
        return ResponseEntity.ok(aluguel);
    }

    @GetMapping
    public ResponseEntity<List<AluguelDTO>> listarTodos() {
        List<AluguelDTO> alugueis = aluguelService.listarTodos();
        return ResponseEntity.ok(alugueis);
    }
}


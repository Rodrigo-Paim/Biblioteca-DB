package com.biblioteca.db.controller;

import com.biblioteca.db.dto.LocatarioDTO;
import com.biblioteca.db.model.Locatario;
import com.biblioteca.db.service.LocatarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locatarios")
@Validated
public class LocatarioController {

    @Autowired
    private LocatarioService locatarioService;

    @PostMapping
    public ResponseEntity<LocatarioDTO> criarLocatario(@RequestBody @Valid LocatarioDTO locatarioDTO) {
        LocatarioDTO criado = locatarioService.criarLocatario(locatarioDTO);
        return ResponseEntity.status(201).body(criado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocatarioDTO> buscarPorId(@PathVariable Long id) {
        LocatarioDTO locatario = locatarioService.buscarPorId(id);
        return ResponseEntity.ok(locatario);
    }

    @GetMapping
    public ResponseEntity<List<LocatarioDTO>> listarTodos() {
        List<LocatarioDTO> locatarios = locatarioService.listarTodos();
        return ResponseEntity.ok(locatarios);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirLocatario(@PathVariable Long id) {
        locatarioService.excluirLocatario(id);
        return ResponseEntity.noContent().build();
    }
}


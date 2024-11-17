package com.biblioteca.db.controller;

import com.biblioteca.db.model.Aluguel;
import com.biblioteca.db.service.AluguelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alugueis")
public class AluguelController {

    @Autowired
    private AluguelService aluguelService;

    @PostMapping
    public Aluguel createAluguel(@RequestBody Aluguel aluguel) {
        return aluguelService.save(aluguel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluguel> getAluguelById(@PathVariable Long id) {
        return aluguelService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Aluguel> getAllAlugueis() {
        return aluguelService.findAll();
    }

    @GetMapping("/locatario/{locatarioId}")
    public List<Aluguel> getAlugueisByLocatarioId(@PathVariable Long locatarioId) {
        return aluguelService.findByLocatarioId(locatarioId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAluguel(@PathVariable Long id) {
        aluguelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


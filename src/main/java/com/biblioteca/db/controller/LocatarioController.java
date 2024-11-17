package com.biblioteca.db.controller;

import com.biblioteca.db.model.Locatario;
import com.biblioteca.db.service.LocatarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/locatarios")
public class LocatarioController {

    @Autowired
    private LocatarioService locatarioService;

    @PostMapping
    public Locatario createLocatario(@RequestBody Locatario locatario) {
        return locatarioService.save(locatario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Locatario> getLocatarioById(@PathVariable Long id) {
        return locatarioService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Locatario> getAllLocatarios() {
        return locatarioService.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocatario(@PathVariable Long id) {
        try {
            locatarioService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}


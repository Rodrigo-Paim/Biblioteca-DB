package com.biblioteca.db.service;


import com.biblioteca.db.model.Locatario;
import com.biblioteca.db.repository.LocatarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocatarioService {

    @Autowired
    private LocatarioRepository locatarioRepository;

    public Locatario save(Locatario locatario) {
        return locatarioRepository.save(locatario);
    }

    public Optional<Locatario> findById(Long id) {
        return locatarioRepository.findById(id);
    }

    public List<Locatario> findAll() {
        return locatarioRepository.findAll();
    }

    public void delete(Long id) {
        Optional<Locatario> locatario = locatarioRepository.findById(id);
        if (locatario.isPresent() && (locatario.get().getAlugueis() == null || locatario.get().getAlugueis().isEmpty())) {
            locatarioRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Locatário possui aluguéis pendentes e não pode ser excluído.");
        }
    }
}


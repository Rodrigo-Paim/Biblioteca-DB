package com.biblioteca.db.service;

import com.biblioteca.db.model.Aluguel;
import com.biblioteca.db.model.Locatario;
import com.biblioteca.db.repository.AluguelRepository;
import com.biblioteca.db.repository.LocatarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private LocatarioRepository locatarioRepository;

    public Aluguel save(Aluguel aluguel) {
        aluguel.setDataRetirada(LocalDate.now());
        aluguel.setDataDevolucao(LocalDate.now().plusDays(2));
        return aluguelRepository.save(aluguel);
    }

    public Optional<Aluguel> findById(Long id) {
        return aluguelRepository.findById(id);
    }

    public List<Aluguel> findAll() {
        return aluguelRepository.findAll();
    }

    public List<Aluguel> findByLocatarioId(Long locatarioId) {
        return aluguelRepository.findByLocatarioId(locatarioId);
    }

    public void delete(Long id) {
        aluguelRepository.deleteById(id);
    }
}


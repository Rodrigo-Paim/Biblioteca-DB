package com.biblioteca.db.service;

import com.biblioteca.db.dto.AluguelDTO;
import com.biblioteca.db.model.Aluguel;
import com.biblioteca.db.model.Livro;
import com.biblioteca.db.model.Locatario;
import com.biblioteca.db.repository.AluguelRepository;
import com.biblioteca.db.repository.LivroRepository;
import com.biblioteca.db.repository.LocatarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private LocatarioRepository locatarioRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Transactional
    public AluguelDTO criarAluguel(AluguelDTO aluguelDTO) {
        Locatario locatario = locatarioRepository.findById(aluguelDTO.getLocatarioId())
                .orElseThrow(() -> new RuntimeException("Locatário não encontrado"));

        Set<Livro> livros = aluguelDTO.getLivrosIds().stream()
                .map(id -> livroRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Livro não encontrado: " + id)))
                .collect(Collectors.toSet());

        Aluguel aluguel = toEntity(aluguelDTO);
        aluguel.setLocatario(locatario);
        aluguel.setLivros(livros);
        aluguel = aluguelRepository.save(aluguel);
        return toDTO(aluguel);
    }

    public AluguelDTO buscarPorId(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado"));
        return toDTO(aluguel);
    }

    public List<AluguelDTO> listarTodos() {
        return aluguelRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private AluguelDTO toDTO(Aluguel aluguel) {
        AluguelDTO dto = new AluguelDTO();
        dto.setId(aluguel.getId());
        dto.setDataRetirada(aluguel.getDataRetirada());
        dto.setDataDevolucao(aluguel.getDataDevolucao());
        dto.setLocatarioId(aluguel.getLocatario().getId());
        dto.setLivrosIds(aluguel.getLivros().stream()
                .map(Livro::getId)
                .collect(Collectors.toSet()));
        return dto;
    }

    private Aluguel toEntity(AluguelDTO dto) {
        Aluguel aluguel = new Aluguel();
        aluguel.setDataRetirada(dto.getDataRetirada());
        aluguel.setDataDevolucao(dto.getDataDevolucao());
        return aluguel;
    }
}


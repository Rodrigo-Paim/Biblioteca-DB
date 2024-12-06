package com.biblioteca.db.service;


import com.biblioteca.db.dto.AutorDTO;
import com.biblioteca.db.model.Autor;
import com.biblioteca.db.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    @Transactional
    public AutorDTO criarAutor(AutorDTO autorDTO) {

        if (!autorRepository.findByNome(autorDTO.nome).isEmpty()) {
            throw new RuntimeException("Autor já cadastrado com este nome");
        }

        if (autorRepository.findById(autorDTO.id).isPresent()) {
            throw new RuntimeException("Autor já existe com o ID fornecido");
        }

        Autor autor = toEntity(autorDTO);
        autor = autorRepository.save(autor);
        return toDTO(autor);
    }

    public AutorDTO buscarPorId(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));
        return toDTO(autor);
    }

    public List<AutorDTO> listarTodos() {
        return autorRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void excluirAutor(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));

        if (!autor.livros.isEmpty()) {
            throw new RuntimeException("Autor não pode ser excluído, pois possui livros associados");
        }

        autorRepository.delete(autor);
    }

    private AutorDTO toDTO(Autor autor) {
        AutorDTO dto = new AutorDTO();
        dto.setId(autor.getId());
        dto.setNome(autor.getNome());
        dto.setSexo(autor.getSexo());
        dto.setAnoNascimento(autor.getAnoNascimento());
        dto.setCpf(autor.getCpf());
        return dto;
    }

    private Autor toEntity(AutorDTO dto) {
        Autor autor = new Autor();
        autor.setNome(dto.getNome());
        autor.setSexo(dto.getSexo());
        autor.setAnoNascimento(dto.getAnoNascimento());
        autor.setCpf(dto.getCpf());
        return autor;
    }
}


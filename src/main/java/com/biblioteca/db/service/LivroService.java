package com.biblioteca.db.service;

import com.biblioteca.db.dto.LivroDTO;
import com.biblioteca.db.model.Autor;
import com.biblioteca.db.model.Livro;
import com.biblioteca.db.repository.AutorRepository;
import com.biblioteca.db.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository;

    @Transactional
    public LivroDTO criarLivro(LivroDTO livroDTO) {
        // Validação de duplicidade
        if (livroRepository.findAll().stream().anyMatch(l -> l.getIsbn().equals(livroDTO.getIsbn()))) {
            throw new RuntimeException("Livro já cadastrado com o ISBN fornecido");
        }

        Livro livro = toEntity(livroDTO);
        livro = livroRepository.save(livro);
        return toDTO(livro);
    }

    public LivroDTO buscarPorId(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        return toDTO(livro);
    }

    public List<LivroDTO> listarTodos() {
        return livroRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void excluirLivro(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        if (!livro.getAutores().isEmpty()) {
            throw new RuntimeException("Livro não pode ser excluído, pois está associado a autores");
        }

        livroRepository.delete(livro); // Exclusão lógica recomendada aqui
    }

    private LivroDTO toDTO(Livro livro) {
        LivroDTO dto = new LivroDTO();
        dto.setId(livro.getId());
        dto.setNome(livro.getNome());
        dto.setIsbn(livro.getIsbn());
        dto.setDataPublicacao(livro.getDataPublicacao());
        dto.setAutoresIds(livro.getAutores().stream()
                .map(Autor::getId)
                .collect(Collectors.toSet()));
        return dto;
    }

    private Livro toEntity(LivroDTO dto) {
        Livro livro = new Livro();
        livro.setNome(dto.getNome());
        livro.setIsbn(dto.getIsbn());
        livro.setDataPublicacao(dto.getDataPublicacao());
        Set<Autor> autores = dto.getAutoresIds().stream()
                .map(id -> autorRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Autor não encontrado: " + id)))
                .collect(Collectors.toSet());
        livro.setAutores(autores);
        return livro;
    }
}


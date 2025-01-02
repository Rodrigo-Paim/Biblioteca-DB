package com.biblioteca.db.service;

import com.biblioteca.db.Utils.Exception.EntidadeNaoEncontradaException;
import com.biblioteca.db.dto.LivroDTO;
import com.biblioteca.db.model.Autor;
import com.biblioteca.db.model.Livro;
import com.biblioteca.db.repository.AutorRepository;
import com.biblioteca.db.repository.LivroRepository;
import com.biblioteca.db.service.external.BookInfoApiService;
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

    @Autowired
    private BookInfoApiService bookInfoApiService;

    @Transactional
    public LivroDTO criarLivro(LivroDTO livroDTO) {
        if (livroRepository.findAll().stream().anyMatch(l -> l.getIsbn().equals(livroDTO.getIsbn()))) {
            throw new RuntimeException("Livro já cadastrado com o ISBN fornecido");
        }

        if (livroDTO.getAutoresIds() == null || livroDTO.getAutoresIds().isEmpty()) {
            throw new RuntimeException("Um livro deve conter pelo menos 1 autor.");
        }

        bookInfoApiService.validarIsbnGoogle(livroDTO.getIsbn());
        bookInfoApiService.validarIsbnOpenLibrary(livroDTO.getIsbn());

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

        livroRepository.delete(livro);
    }

    @Transactional
    public LivroDTO atualizarLivro(Long id, LivroDTO dto) {
        Livro livroExistente = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com ID: " + id));

        if (dto.getAutoresIds() == null || dto.getAutoresIds().isEmpty()) {
            throw new RuntimeException("Um livro deve conter pelo menos 1 autor.");
        }

        if (!livroExistente.getIsbn().equals(dto.getIsbn())) {
            if (livroRepository.existsByIsbn(dto.getIsbn())) {
                throw new RuntimeException("Já existe um livro cadastrado com esse ISBN.");
            }
            bookInfoApiService.validarIsbnGoogle(dto.getIsbn());
        }

        livroExistente.nome = dto.getNome();
        livroExistente.isbn = dto.getIsbn();
        livroExistente.dataPublicacao = dto.getDataPublicacao();

        Set<Autor> novosAutores = dto.getAutoresIds().stream()
                .map(autorId -> autorRepository.findById(autorId)
                        .orElseThrow(() -> new RuntimeException("Autor não encontrado: " + autorId)))
                .collect(Collectors.toSet());
        livroExistente.autores = novosAutores;

        livroExistente = livroRepository.save(livroExistente);
        return toDTO(livroExistente);
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
        livro.nome = dto.getNome();
        livro.isbn = dto.getIsbn();
        livro.dataPublicacao = dto.getDataPublicacao();
        Set<Autor> autores = dto.getAutoresIds().stream()
                .map(id -> autorRepository.findById(id)
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("Autor não encontrado: " + id)))
                .collect(Collectors.toSet());
        livro.autores = autores;
        return livro;
    }


}


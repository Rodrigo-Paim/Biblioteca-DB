package com.biblioteca.db.service;

import com.biblioteca.db.Utils.Exception.EntidadeNaoEncontradaException;
import com.biblioteca.db.dto.LivroDTO;
import com.biblioteca.db.mappers.LivroMapper;
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

    private LivroMapper livroMapper;

    @Transactional
    public LivroDTO criarLivro(LivroDTO livroDTO) {
        livroJaCadastradoIsbn(livroDTO);

        livroPossuiAutor(livroDTO);

        bookInfoApiService.validarIsbnGoogle(livroDTO.getIsbn());
        bookInfoApiService.validarIsbnOpenLibrary(livroDTO.getIsbn());

        Livro livro = livroMapper.livroDtoToEntity(livroDTO);
        livro = livroRepository.save(livro);
        return livroMapper.livroToDto(livro);
    }

    public LivroDTO buscarPorId(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        return livroMapper.livroToDto(livro);
    }

    public List<LivroDTO> listarTodos() {
        return livroRepository.findAll().stream()
                .map(livro -> livroMapper.livroToDto(livro))
                .collect(Collectors.toList());
    }

    public List<LivroDTO> listarLivrosDisponiveis() {
        return livroRepository.findByAlugadoFalse().stream()
                .map(livro -> livroMapper.livroToDto(livro))
                .collect(Collectors.toList());
    }

    public List<LivroDTO> listarLivrosAlugados() {
        return livroRepository.findByAlugadoTrue().stream()
                .map(livro -> livroMapper.livroToDto(livro))
                .collect(Collectors.toList());
    }

    public List<LivroDTO> listarLivrosPorLocatario(Long locatarioId) {
        return livroRepository.findByLocatarioId(locatarioId).stream()
                .map(livro -> livroMapper.livroToDto(livro))
                .collect(Collectors.toList());
    }

    public List<LivroDTO> listarLivrosPorAutor(String nomeAutor) {
        return livroRepository.findByAutorNome(nomeAutor).stream()
                .map(livro -> livroMapper.livroToDto(livro))
                .collect(Collectors.toList());
    }

    @Transactional
    public void excluirLivro(Long id) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        possuiAutores(livro);

        livroEstaAlugado(livro);

        livroRepository.delete(livro);
    }

    @Transactional
    public LivroDTO atualizarLivro(Long id, LivroDTO dto) {
        Livro livroExistente = livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com ID: " + id));

        livroPossuiAutor(dto);

        possuiIsbn(dto, livroExistente);

        livroExistente.nome = dto.getNome();
        livroExistente.isbn = dto.getIsbn();
        livroExistente.dataPublicacao = dto.getDataPublicacao();

        Set<Autor> novosAutores = dto.getAutoresIds().stream()
                .map(autorId -> autorRepository.findById(autorId)
                        .orElseThrow(() -> new RuntimeException("Autor não encontrado: " + autorId)))
                .collect(Collectors.toSet());
        livroExistente.autores = novosAutores;

        livroExistente = livroRepository.save(livroExistente);
        return livroMapper.livroToDto(livroExistente);
    }

    private void livroJaCadastradoIsbn(LivroDTO livroDTO) {
        if (livroRepository.findAll().stream().anyMatch(l -> l.getIsbn().equals(livroDTO.getIsbn()))) {
            throw new RuntimeException("Livro já cadastrado com o ISBN fornecido");
        }
    }

    private static void livroPossuiAutor(LivroDTO livroDTO) {
        if (livroDTO.getAutoresIds() == null || livroDTO.getAutoresIds().isEmpty()) {
            throw new RuntimeException("Um livro deve conter pelo menos 1 autor.");
        }
    }

    private static void livroEstaAlugado(Livro livro) {
        if (livro.isAlugado()) {
            throw new RuntimeException("Livro não pode ser excluído, pois está alugado");
        }
    }

    private static void possuiAutores(Livro livro) {
        if (!livro.getAutores().isEmpty()) {
            throw new RuntimeException("Livro não pode ser excluído, pois está associado a autores");
        }
    }

    private void possuiIsbn(LivroDTO dto, Livro livroExistente) {
        if (!livroExistente.getIsbn().equals(dto.getIsbn())) {
            if (livroRepository.existsByIsbn(dto.getIsbn())) {
                throw new RuntimeException("Já existe um livro cadastrado com esse ISBN.");
            }
            bookInfoApiService.validarIsbnGoogle(dto.getIsbn());
        }
    }
}
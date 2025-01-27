package com.biblioteca.db.service;


import com.biblioteca.db.dto.AutorDTO;
import com.biblioteca.db.mappers.AutorMapper;
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

    private AutorMapper autorMapper;

    public AutorDTO criarAutor(AutorDTO autorDTO) {

        validarAutorPeloNome(autorDTO);

        verificaSeAutorJaExiste(autorDTO);

        Autor autor = autorMapper.autorDtoToEntity(autorDTO);
        autor = autorRepository.save(autor);
        return autorMapper.autorToDto(autor);
    }

    public AutorDTO buscarPorId(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));
        return autorMapper.autorToDto(autor);
    }

    public List<AutorDTO> listarTodos() {
        return autorRepository.findAll().stream()
                .map(autor -> autorMapper.autorToDto(autor))
                .collect(Collectors.toList());
    }

    public void excluirAutor(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));

        validarSeAutorTemLivro(autor);

        autorRepository.delete(autor);
    }

    public List<AutorDTO> buscarPorNome(String nome) {
        List<Autor> autores = autorRepository.findByNome(nome);

        return autores.stream()
                .map(autorDto -> autorMapper.autorToDto(autorDto))
                .collect(Collectors.toList());
    }

    private void verificaSeAutorJaExiste(AutorDTO autorDTO) {
        if (autorRepository.findById(autorDTO.id).isPresent()) {
            throw new RuntimeException("Autor já existe com o ID fornecido");
        }
    }

    private void validarAutorPeloNome(AutorDTO autorDTO) {
        if (!autorRepository.findByNome(autorDTO.nome).isEmpty()) {
            throw new RuntimeException("Autor já cadastrado com este nome");
        }
    }

    private static void validarSeAutorTemLivro(Autor autor) {
        if (!autor.livros.isEmpty()) {
            throw new RuntimeException("Autor não pode ser excluído, pois possui livros associados");
        }
    }
}


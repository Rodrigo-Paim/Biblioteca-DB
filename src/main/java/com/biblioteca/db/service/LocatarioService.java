package com.biblioteca.db.service;


import com.biblioteca.db.dto.LocatarioDTO;
import com.biblioteca.db.mappers.LocatarioMapper;
import com.biblioteca.db.model.Locatario;
import com.biblioteca.db.repository.LocatarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocatarioService {

    @Autowired
    private LocatarioRepository locatarioRepository;

    private LocatarioMapper locatarioMapper;

    @Transactional
    public LocatarioDTO criarLocatario(LocatarioDTO locatarioDTO) {

        PossuiLocatario(locatarioDTO);

        Locatario locatario = locatarioMapper.locatarioDtoToEntity(locatarioDTO);
        locatario = locatarioRepository.save(locatario);
        return locatarioMapper.locatarioToDto(locatario);
    }

    public LocatarioDTO buscarPorId(Long id) {
        Locatario locatario = locatarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Locatário não encontrado"));
        return locatarioMapper.locatarioToDto(locatario);
    }

    public List<LocatarioDTO> listarTodos() {
        return locatarioRepository.findAll().stream()
                .map(locatario -> locatarioMapper.locatarioToDto(locatario))
                .collect(Collectors.toList());
    }

    @Transactional
    public void excluirLocatario(Long id) {
        Locatario locatario = locatarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Locatário não encontrado"));

        Possuilivros(locatario);

        locatarioRepository.delete(locatario);
    }

    private static void Possuilivros(Locatario locatario) {
        if (!locatario.getLivros().isEmpty()) {
            throw new RuntimeException("Locatário não pode ser excluído, pois possui livros para devolução");
        }
    }

    private void PossuiLocatario(LocatarioDTO locatarioDTO) {
        if (locatarioRepository.findAll().stream().anyMatch(l -> l.getCpf().equals(locatarioDTO.getCpf()))) {
            throw new RuntimeException("Locatário já cadastrado com o CPF fornecido");
        }
    }

}


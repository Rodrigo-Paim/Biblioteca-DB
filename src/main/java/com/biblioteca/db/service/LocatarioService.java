package com.biblioteca.db.service;


import com.biblioteca.db.dto.LocatarioDTO;
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

    @Transactional
    public LocatarioDTO criarLocatario(LocatarioDTO locatarioDTO) {
        if (locatarioRepository.findAll().stream().anyMatch(l -> l.getCpf().equals(locatarioDTO.getCpf()))) {
            throw new RuntimeException("Locatário já cadastrado com o CPF fornecido");
        }

        Locatario locatario = toEntity(locatarioDTO);
        locatario = locatarioRepository.save(locatario);
        return toDTO(locatario);
    }

    public LocatarioDTO buscarPorId(Long id) {
        Locatario locatario = locatarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Locatário não encontrado"));
        return toDTO(locatario);
    }

    public List<LocatarioDTO> listarTodos() {
        return locatarioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void excluirLocatario(Long id) {
        Locatario locatario = locatarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Locatário não encontrado"));

        locatarioRepository.delete(locatario);
    }

    private LocatarioDTO toDTO(Locatario locatario) {
        LocatarioDTO dto = new LocatarioDTO();
        dto.setId(locatario.getId());
        dto.setNome(locatario.getNome());
        dto.setSexo(locatario.getSexo());
        dto.setTelefone(locatario.getTelefone());
        dto.setEmail(locatario.getEmail());
        dto.setDataNascimento(locatario.getDataNascimento());
        dto.setCpf(locatario.getCpf());
        return dto;
    }

    private Locatario toEntity(LocatarioDTO dto) {
        Locatario locatario = new Locatario();
        locatario.nome = dto.getNome();
        locatario.sexo = dto.getSexo();
        locatario.telefone = dto.getTelefone();
        locatario.email = dto.getEmail();
        locatario.dataNascimento = dto.getDataNascimento();
        locatario.cpf = dto.getCpf();
        return locatario;
    }
}


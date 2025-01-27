package com.biblioteca.db.service;

import com.biblioteca.db.Utils.Exception.EntidadeNaoEncontradaException;
import com.biblioteca.db.dto.AluguelDTO;
import com.biblioteca.db.mappers.AluguelMapper;
import com.biblioteca.db.model.Aluguel;
import com.biblioteca.db.model.Livro;
import com.biblioteca.db.model.Locatario;
import com.biblioteca.db.repository.AluguelRepository;
import com.biblioteca.db.repository.LivroRepository;
import com.biblioteca.db.repository.LocatarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
    private AluguelMapper aluguelMapper;

    @Autowired
    private LivroRepository livroRepository;

    @Transactional
    public AluguelDTO criarAluguel(AluguelDTO aluguelDTO) {
        Locatario locatario = locatarioRepository.findById(aluguelDTO.getLocatarioId())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Locatário não encontrado"));

        Set<Livro> livros = aluguelDTO.getLivrosIds().stream()
                .map(id -> livroRepository.findById(id)
                        .orElseThrow(() -> new EntidadeNaoEncontradaException("Livro não encontrado: " + id)))
                .collect(Collectors.toSet());

        Aluguel aluguel = aluguelMapper.aluguelDtoToEntity(aluguelDTO);
        aluguel = aluguelRepository.save(aluguel);
        return aluguelMapper.aluguelToDto(aluguel);
    }

    public AluguelDTO buscarPorId(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Aluguel não encontrado"));
        return toDTO(aluguel);
    }

    public List<AluguelDTO> listarTodos() {
        return aluguelRepository.findAll().stream()
                .map(aluguel -> aluguelMapper.aluguelToDto(aluguel))
                .collect(Collectors.toList());
    }

    private AluguelDTO toDTO(Aluguel aluguel) {
        return aluguelMapper.aluguelToDto(aluguel);
    }

    private Aluguel toEntity(AluguelDTO dto) {
        return aluguelMapper.aluguelDtoToEntity(dto);
    }

    public BigDecimal calcularValorAluguel(Long aluguelId) {
        Aluguel aluguel = aluguelRepository.findById(aluguelId)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado: " + aluguelId));

        LocalDateTime retirada = converterParaLocalDateTime(aluguel.getDataRetirada());
        LocalDateTime devolucao = converterParaLocalDateTime(aluguel.getDataDevolucao());

        long diasLocados = ChronoUnit.DAYS.between(retirada.toLocalDate(), devolucao.toLocalDate());
        if (diasLocados < 0) diasLocados = 0;

        BigDecimal valorDias = BigDecimal.valueOf(diasLocados * 2.0);

        BigDecimal multaAtraso = BigDecimal.ZERO;

        if (devolucao.getHour() > 22) {
            long diasAtraso = ChronoUnit.DAYS.between(retirada.toLocalDate(), devolucao.toLocalDate());
            multaAtraso = BigDecimal.valueOf(diasAtraso * 1.0);
        }

        return valorDias.add(multaAtraso);
    }

    private LocalDateTime converterParaLocalDateTime(java.util.Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}


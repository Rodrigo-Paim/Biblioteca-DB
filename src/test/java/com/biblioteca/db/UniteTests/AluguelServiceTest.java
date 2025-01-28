package com.biblioteca.db.UniteTests;

import com.biblioteca.db.Utils.Exception.EntidadeNaoEncontradaException;
import com.biblioteca.db.dto.AluguelDTO;
import com.biblioteca.db.mappers.AluguelMapper;
import com.biblioteca.db.model.Aluguel;
import com.biblioteca.db.model.Livro;
import com.biblioteca.db.model.Locatario;
import com.biblioteca.db.repository.AluguelRepository;
import com.biblioteca.db.repository.LivroRepository;
import com.biblioteca.db.repository.LocatarioRepository;
import com.biblioteca.db.service.AluguelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class AluguelServiceTest {

    @InjectMocks
    private AluguelService aluguelService;

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private LocatarioRepository locatarioRepository;

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private AluguelMapper aluguelMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarAluguel_Sucesso() {
        // Dados de entrada
        AluguelDTO aluguelDTO = new AluguelDTO();
        aluguelDTO.setLocatarioId(1L);
        aluguelDTO.setLivrosIds(Set.of(1L, 2L));

        Locatario locatario = new Locatario();
        locatario.setId(1L);

        Livro livro1 = new Livro();
        livro1.setId(1L);

        Livro livro2 = new Livro();
        livro2.setId(2L);

        Aluguel aluguel = new Aluguel();
        aluguel.setId(1L);

        // Mock dos métodos
        when(locatarioRepository.findById(1L)).thenReturn(Optional.of(locatario));
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro1));
        when(livroRepository.findById(2L)).thenReturn(Optional.of(livro2));
        when(aluguelMapper.aluguelDtoToEntity(aluguelDTO)).thenReturn(aluguel);
        when(aluguelRepository.save(aluguel)).thenReturn(aluguel);
        when(aluguelMapper.aluguelToDto(aluguel)).thenReturn(aluguelDTO);

        // Execução
        AluguelDTO resultado = aluguelService.criarAluguel(aluguelDTO);

        // Verificações
        assertNotNull(resultado);
        verify(locatarioRepository, times(1)).findById(1L);
        verify(livroRepository, times(2)).findById(anyLong());
        verify(aluguelRepository, times(1)).save(aluguel);
    }

    @Test
    void testCriarAluguel_LocatarioNaoEncontrado() {
        // Dados de entrada
        AluguelDTO aluguelDTO = new AluguelDTO();
        aluguelDTO.setLocatarioId(1L);

        when(locatarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Execução e Verificação
        EntidadeNaoEncontradaException ex = assertThrows(EntidadeNaoEncontradaException.class, () -> aluguelService.criarAluguel(aluguelDTO));
        assertEquals("Locatário não encontrado", ex.getMessage());
        verify(locatarioRepository, times(1)).findById(1L);
        verifyNoInteractions(livroRepository);
    }

    @Test
    void testCalcularValorAluguel_Sucesso() {
        // Dados de entrada
        Aluguel aluguel = new Aluguel();
        aluguel.setDataRetirada(new Date());
        aluguel.setDataDevolucao(new Date());

        when(aluguelRepository.findById(1L)).thenReturn(Optional.of(aluguel));

        // Execução
        BigDecimal valor = aluguelService.calcularValorAluguel(1L);

        // Verificações
        assertNotNull(valor);
        verify(aluguelRepository, times(1)).findById(1L);
    }

    @Test
    void testCalcularValorAluguel_AluguelNaoEncontrado() {
        when(aluguelRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> aluguelService.calcularValorAluguel(1L));
        assertEquals("Aluguel não encontrado: 1", ex.getMessage());
        verify(aluguelRepository, times(1)).findById(1L);
    }
}

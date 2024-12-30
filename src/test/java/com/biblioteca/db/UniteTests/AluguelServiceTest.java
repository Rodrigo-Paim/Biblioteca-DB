package com.biblioteca.db.UniteTests;

import com.biblioteca.db.dto.AluguelDTO;
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
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class AluguelServiceTest {

    @Mock
    private AluguelRepository aluguelRepository;
    @Mock
    private LocatarioRepository locatarioRepository;
    @Mock
    private LivroRepository livroRepository;

    @InjectMocks
    private AluguelService aluguelService;

    private AluguelDTO aluguelDTO;
    private Aluguel aluguel;
    private Locatario locatario;
    private Livro livro;

    @BeforeEach
    void setUp() {
        locatario = new Locatario();
        locatario.setId(1L);
        locatario.setNome("Loc Teste");

        livro = new Livro();
        livro.setId(10L);
        livro.setNome("Livro Alugado");

        aluguel = new Aluguel();
        aluguel.setId(100L);
        aluguel.setLocatario(locatario);
        aluguel.setLivros(Set.of(livro));
        aluguel.setDataRetirada(new Date());

        aluguelDTO = new AluguelDTO();
        aluguelDTO.setLocatarioId(1L);
        aluguelDTO.setLivrosIds(Set.of(10L));
        aluguelDTO.setDataRetirada(new Date());
    }

    @Test
    void criarAluguel_deveCriarComSucesso() {
        when(locatarioRepository.findById(1L)).thenReturn(Optional.of(locatario));
        when(livroRepository.findById(10L)).thenReturn(Optional.of(livro));
        when(aluguelRepository.save(any(Aluguel.class))).thenReturn(aluguel);

        AluguelDTO criado = aluguelService.criarAluguel(aluguelDTO);
        assertNotNull(criado.getId());
        assertTrue(criado.getLivrosIds().contains(10L));
        verify(aluguelRepository, times(1)).save(any(Aluguel.class));
    }

    @Test
    void criarAluguel_deveFalharSeNaoHouverLocatarioId() {
        aluguelDTO.setLocatarioId(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> aluguelService.criarAluguel(aluguelDTO));
        assertTrue(ex.getMessage().contains("Locatário é obrigatório"));
        verifyNoInteractions(aluguelRepository);
    }

    @Test
    void criarAluguel_deveFalharSeNaoHouverLivros() {
        aluguelDTO.setLivrosIds(Collections.emptySet());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> aluguelService.criarAluguel(aluguelDTO));
        assertTrue(ex.getMessage().contains("Deve haver ao menos 1 livro"));
        verifyNoInteractions(aluguelRepository);
    }

    @Test
    void buscarPorId_deveRetornarAluguel() {
        when(aluguelRepository.findById(100L)).thenReturn(Optional.of(aluguel));

        AluguelDTO encontrado = aluguelService.buscarPorId(100L);
        assertNotNull(encontrado);
        assertEquals(1L, encontrado.getLocatarioId());
    }

    @Test
    void listarTodos_deveRetornarLista() {
        when(aluguelRepository.findAll()).thenReturn(Collections.singletonList(aluguel));

        var lista = aluguelService.listarTodos();
        assertEquals(1, lista.size());
    }
}

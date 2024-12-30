package com.biblioteca.db.UniteTests;

import com.biblioteca.db.dto.AutorDTO;
import com.biblioteca.db.model.Autor;
import com.biblioteca.db.repository.AutorRepository;
import com.biblioteca.db.service.AutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutorServiceTest {

    @Mock
    private AutorRepository autorRepository;

    @InjectMocks
    private AutorService autorService;

    private Autor autor;
    private AutorDTO autorDTO;

    @BeforeEach
    void setUp() {
        autor = new Autor();
        autor.setId(1L);
        autor.setNome("João da Silva");
        autor.setCpf("12345678901");
        autor.setAnoNascimento(1980);

        autorDTO = new AutorDTO();
        autorDTO.setNome("João da Silva");
        autorDTO.setCpf("12345678901");
        autorDTO.setAnoNascimento(1980);
    }

    @Test
    void criarAutor_deveCriarAutorComSucesso() {
        when(autorRepository.save(any(Autor.class))).thenReturn(autor);

        AutorDTO criado = autorService.criarAutor(autorDTO);

        assertNotNull(criado.getId());
        assertEquals("João da Silva", criado.getNome());
        verify(autorRepository, times(1)).save(any(Autor.class));
    }

    @Test
    void buscarPorId_deveRetornarAutor() {
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));

        AutorDTO resultado = autorService.buscarPorId(1L);
        assertNotNull(resultado);
        assertEquals("João da Silva", resultado.getNome());
        verify(autorRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_deveLancarExcecaoSeNaoEncontrar() {
        when(autorRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> autorService.buscarPorId(2L));
        assertTrue(ex.getMessage().contains("não encontrado"));
    }

    @Test
    void listarTodos_deveRetornarListaAutores() {
        when(autorRepository.findAll()).thenReturn(Collections.singletonList(autor));

        List<AutorDTO> lista = autorService.listarTodos();
        assertEquals(1, lista.size());
        assertEquals("João da Silva", lista.get(0).getNome());
    }

    @Test
    void excluirAutor_deveExcluirAutorSemLivros() {
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        // autor sem livros associados
        autor.setLivros(Collections.emptySet());

        autorService.excluirAutor(1L);
        verify(autorRepository, times(1)).delete(autor);
    }
}

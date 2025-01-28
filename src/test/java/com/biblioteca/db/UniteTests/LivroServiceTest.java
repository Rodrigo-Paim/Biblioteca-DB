package com.biblioteca.db.UniteTests;

import com.biblioteca.db.dto.LivroDTO;
import com.biblioteca.db.mappers.LivroMapper;
import com.biblioteca.db.model.Autor;
import com.biblioteca.db.model.Livro;
import com.biblioteca.db.repository.AutorRepository;
import com.biblioteca.db.repository.LivroRepository;
import com.biblioteca.db.service.LivroService;
import com.biblioteca.db.service.external.BookInfoApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LivroServiceTest {

    @InjectMocks
    private LivroService livroService;

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private LivroMapper livroMapper;

    @Mock
    private BookInfoApiService bookInfoApiService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarLivro_Sucesso() {
        LivroDTO livroDTO = new LivroDTO();
        livroDTO.setNome("Livro Teste");
        livroDTO.setIsbn("123456789");
        livroDTO.setAutoresIds(Set.of(1L));

        Livro livro = new Livro();
        livro.setId(1L);
        livro.setNome("Livro Teste");

        when(livroRepository.findAll()).thenReturn(Collections.emptyList());
        when(livroMapper.livroDtoToEntity(livroDTO)).thenReturn(livro);
        when(livroRepository.save(livro)).thenReturn(livro);
        when(livroMapper.livroToDto(livro)).thenReturn(livroDTO);

        LivroDTO resultado = livroService.criarLivro(livroDTO);

        assertNotNull(resultado);
        assertEquals("Livro Teste", resultado.getNome());
        verify(bookInfoApiService, times(1)).validarIsbnGoogle("123456789");
        verify(bookInfoApiService, times(1)).validarIsbnOpenLibrary("123456789");
        verify(livroRepository, times(1)).save(livro);
    }

    @Test
    void testCriarLivro_Falha_IsbnDuplicado() {
        LivroDTO livroDTO = new LivroDTO();
        livroDTO.setIsbn("123456789");

        Livro livroExistente = new Livro();
        livroExistente.setIsbn("123456789");

        when(livroRepository.findAll()).thenReturn(List.of(livroExistente));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> livroService.criarLivro(livroDTO));
        assertEquals("Livro já cadastrado com o ISBN fornecido", ex.getMessage());
        verify(bookInfoApiService, never()).validarIsbnGoogle(anyString());
    }

    @Test
    void testExcluirLivro_ComAutores_Falha() {
        Livro livro = new Livro();
        livro.setId(1L);
        livro.setAutores(Set.of(new Autor()));

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> livroService.excluirLivro(1L));
        assertEquals("Livro não pode ser excluído, pois está associado a autores", ex.getMessage());
        verify(livroRepository, never()).delete(livro);
    }

    @Test
    void testExcluirLivro_Sucesso() {
        Livro livro = new Livro();
        livro.setId(1L);
        livro.setAutores(Collections.emptySet());
        livro.setAlugado(false);

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        livroService.excluirLivro(1L);

        verify(livroRepository, times(1)).delete(livro);
    }

    @Test
    void testBuscarPorId_Sucesso() {
        Livro livro = new Livro();
        livro.setId(1L);
        livro.setNome("Livro Teste");

        LivroDTO livroDTO = new LivroDTO();
        livroDTO.setId(1L);
        livroDTO.setNome("Livro Teste");

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(livroMapper.livroToDto(livro)).thenReturn(livroDTO);

        LivroDTO resultado = livroService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Livro Teste", resultado.getNome());
        verify(livroRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorId_Falha() {
        when(livroRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> livroService.buscarPorId(1L));
        assertEquals("Livro não encontrado", ex.getMessage());
        verify(livroRepository, times(1)).findById(1L);
    }

    @Test
    void testListarTodos_Sucesso() {
        Livro livro = new Livro();
        livro.setId(1L);
        livro.setNome("Livro Teste");

        LivroDTO livroDTO = new LivroDTO();
        livroDTO.setId(1L);
        livroDTO.setNome("Livro Teste");

        when(livroRepository.findAll()).thenReturn(List.of(livro));
        when(livroMapper.livroToDto(livro)).thenReturn(livroDTO);

        List<LivroDTO> resultado = livroService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Livro Teste", resultado.get(0).getNome());
        verify(livroRepository, times(1)).findAll();
    }
}
package com.biblioteca.db.UniteTests;

import com.biblioteca.db.dto.LivroDTO;
import com.biblioteca.db.model.Autor;
import com.biblioteca.db.model.Livro;
import com.biblioteca.db.repository.AutorRepository;
import com.biblioteca.db.repository.LivroRepository;
import com.biblioteca.db.service.LivroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private AutorRepository autorRepository;

    @InjectMocks
    private LivroService livroService;

    private Livro livro;
    private LivroDTO livroDTO;
    private Autor autor;

    @BeforeEach
    void setUp() {
        autor = new Autor();
        autor.setId(1L);
        autor.setNome("Autor Teste");

        livro = new Livro();
        livro.setId(1L);
        livro.setNome("Livro Teste");
        livro.setIsbn("999-888");
        livro.setAutores(Set.of(autor));

        livroDTO = new LivroDTO();
        livroDTO.setNome("Livro Teste");
        livroDTO.setIsbn("999-888");
        livroDTO.setAutoresIds(Set.of(1L));
    }

    @Test
    void criarLivro_deveCriarLivroComAutores() {
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);

        LivroDTO criado = livroService.criarLivro(livroDTO);
        assertNotNull(criado.getId());
        assertEquals("Livro Teste", criado.getNome());
        verify(livroRepository, times(1)).save(any(Livro.class));
    }

    @Test
    void criarLivro_deveLancarExcecaoSeNaoHouverAutores() {
        livroDTO.setAutoresIds(Collections.emptySet());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> livroService.criarLivro(livroDTO));
        assertTrue(ex.getMessage().contains("pelo menos 1 autor"));
        verifyNoInteractions(livroRepository);
    }

    @Test
    void buscarPorId_deveRetornarLivro() {
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));

        LivroDTO encontrado = livroService.buscarPorId(1L);
        assertNotNull(encontrado);
        assertEquals("Livro Teste", encontrado.getNome());
    }

    @Test
    void listarTodos_deveRetornarLista() {
        when(livroRepository.findAll()).thenReturn(Collections.singletonList(livro));

        var lista = livroService.listarTodos();
        assertEquals(1, lista.size());
    }

    @Test
    void excluirLivro_deveExcluirComSucesso() {
        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        // Deixamos a condição para exclusão (ex.: sem aluguéis) simulada ou customizada
        // Aqui só testamos a chamada
        livro.setAutores(Set.of()); // se houver regra que exige sem autores
        livroService.excluirLivro(1L);
        verify(livroRepository, times(1)).delete(livro);
    }
}

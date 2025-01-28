package com.biblioteca.db.UniteTests;

import com.biblioteca.db.dto.AutorDTO;
import com.biblioteca.db.mappers.AutorMapper;
import com.biblioteca.db.model.Autor;
import com.biblioteca.db.model.Livro;
import com.biblioteca.db.repository.AutorRepository;
import com.biblioteca.db.service.AutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AutorServiceTest {

    @InjectMocks
    private AutorService autorService;

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private AutorMapper autorMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarAutor_Sucesso() {
        AutorDTO autorDTO = new AutorDTO();
        autorDTO.setId(1L);
        autorDTO.setNome("Autor Teste");

        Autor autor = new Autor();
        autor.setId(1L);
        autor.setNome("Autor Teste");

        when(autorRepository.findByNome("Autor Teste")).thenReturn(Collections.emptyList());
        when(autorRepository.findById(1L)).thenReturn(Optional.empty());
        when(autorMapper.autorDtoToEntity(autorDTO)).thenReturn(autor);
        when(autorRepository.save(autor)).thenReturn(autor);
        when(autorMapper.autorToDto(autor)).thenReturn(autorDTO);

        AutorDTO resultado = autorService.criarAutor(autorDTO);

        assertNotNull(resultado);
        assertEquals("Autor Teste", resultado.getNome());
        verify(autorRepository, times(1)).findByNome("Autor Teste");
        verify(autorRepository, times(1)).findById(1L);
        verify(autorRepository, times(1)).save(autor);
    }

    @Test
    void testCriarAutor_Falha_NomeJaCadastrado() {
        AutorDTO autorDTO = new AutorDTO();
        autorDTO.setNome("Autor Duplicado");

        when(autorRepository.findByNome("Autor Duplicado")).thenReturn(List.of(new Autor()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> autorService.criarAutor(autorDTO));
        assertEquals("Autor já cadastrado com este nome", ex.getMessage());
        verify(autorRepository, times(1)).findByNome("Autor Duplicado");
    }

    @Test
    void testExcluirAutor_ComLivros_Falha() {
        Autor autor = new Autor();
        autor.setId(1L);
        autor.setNome("Autor Teste");
        autor.setLivros(Set.of(new Livro()));

        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> autorService.excluirAutor(1L));
        assertEquals("Autor não pode ser excluído, pois possui livros associados", ex.getMessage());
        verify(autorRepository, times(1)).findById(1L);
        verify(autorRepository, never()).delete(autor);
    }

    @Test
    void testBuscarPorId_Sucesso() {
        Autor autor = new Autor();
        autor.setId(1L);
        autor.setNome("Autor Teste");

        AutorDTO autorDTO = new AutorDTO();
        autorDTO.setId(1L);
        autorDTO.setNome("Autor Teste");

        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        when(autorMapper.autorToDto(autor)).thenReturn(autorDTO);

        AutorDTO resultado = autorService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Autor Teste", resultado.getNome());
        verify(autorRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorId_Falha_NaoEncontrado() {
        when(autorRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> autorService.buscarPorId(1L));
        assertEquals("Autor não encontrado", ex.getMessage());
        verify(autorRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorNome_Sucesso() {
        Autor autor = new Autor();
        autor.setId(1L);
        autor.setNome("Autor Teste");

        AutorDTO autorDTO = new AutorDTO();
        autorDTO.setId(1L);
        autorDTO.setNome("Autor Teste");

        when(autorRepository.findByNome("Autor Teste")).thenReturn(List.of(autor));
        when(autorMapper.autorToDto(autor)).thenReturn(autorDTO);

        List<AutorDTO> resultado = autorService.buscarPorNome("Autor Teste");

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Autor Teste", resultado.get(0).getNome());
        verify(autorRepository, times(1)).findByNome("Autor Teste");
    }
}
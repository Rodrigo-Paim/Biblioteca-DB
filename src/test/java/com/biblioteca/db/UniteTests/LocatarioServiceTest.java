package com.biblioteca.db.UniteTests;

import com.biblioteca.db.dto.LocatarioDTO;
import com.biblioteca.db.mappers.LocatarioMapper;
import com.biblioteca.db.model.Livro;
import com.biblioteca.db.model.Locatario;
import com.biblioteca.db.repository.LocatarioRepository;
import com.biblioteca.db.service.LocatarioService;
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
import static org.mockito.internal.verification.VerificationModeFactory.times;

class LocatarioServiceTest {

    @InjectMocks
    private LocatarioService locatarioService;

    @Mock
    private LocatarioRepository locatarioRepository;

    @Mock
    private LocatarioMapper locatarioMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarLocatario_Sucesso() {
        LocatarioDTO locatarioDTO = new LocatarioDTO();
        locatarioDTO.setNome("Locatário Teste");
        locatarioDTO.setCpf("12345678901");

        Locatario locatario = new Locatario();
        locatario.setId(1L);
        locatario.setNome("Locatário Teste");

        when(locatarioRepository.findAll()).thenReturn(Collections.emptyList());
        when(locatarioMapper.locatarioDtoToEntity(locatarioDTO)).thenReturn(locatario);
        when(locatarioRepository.save(locatario)).thenReturn(locatario);
        when(locatarioMapper.locatarioToDto(locatario)).thenReturn(locatarioDTO);

        LocatarioDTO resultado = locatarioService.criarLocatario(locatarioDTO);

        assertNotNull(resultado);
        assertEquals("Locatário Teste", resultado.getNome());
        verify(locatarioRepository, times(1)).findAll();
        verify(locatarioRepository, times(1)).save(locatario);
    }

    @Test
    void testCriarLocatario_Falha_CpfDuplicado() {
        LocatarioDTO locatarioDTO = new LocatarioDTO();
        locatarioDTO.setCpf("12345678901");

        Locatario locatarioExistente = new Locatario();
        locatarioExistente.setCpf("12345678901");

        when(locatarioRepository.findAll()).thenReturn(List.of(locatarioExistente));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> locatarioService.criarLocatario(locatarioDTO));
        assertEquals("Locatário já cadastrado com o CPF fornecido", ex.getMessage());
        verify(locatarioRepository, times(1)).findAll();
        verify(locatarioRepository, never()).save(any());
    }

    @Test
    void testExcluirLocatario_Sucesso() {
        Locatario locatario = new Locatario();
        locatario.setId(1L);
        locatario.setLivros(Collections.emptySet());

        when(locatarioRepository.findById(1L)).thenReturn(Optional.of(locatario));

        locatarioService.excluirLocatario(1L);

        verify(locatarioRepository, times(1)).delete(locatario);
    }

    @Test
    void testExcluirLocatario_Falha_PossuiLivros() {
        Locatario locatario = new Locatario();
        locatario.setId(1L);
        locatario.setLivros(Set.of(new Livro()));

        when(locatarioRepository.findById(1L)).thenReturn(Optional.of(locatario));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> locatarioService.excluirLocatario(1L));
        assertEquals("Locatário não pode ser excluído, pois possui livros para devolução", ex.getMessage());
        verify(locatarioRepository, never()).delete(locatario);
    }

    @Test
    void testBuscarPorId_Sucesso() {
        Locatario locatario = new Locatario();
        locatario.setId(1L);
        locatario.setNome("Locatário Teste");

        LocatarioDTO locatarioDTO = new LocatarioDTO();
        locatarioDTO.setId(1L);
        locatarioDTO.setNome("Locatário Teste");

        when(locatarioRepository.findById(1L)).thenReturn(Optional.of(locatario));
        when(locatarioMapper.locatarioToDto(locatario)).thenReturn(locatarioDTO);

        LocatarioDTO resultado = locatarioService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Locatário Teste", resultado.getNome());
        verify(locatarioRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarPorId_Falha() {
        when(locatarioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> locatarioService.buscarPorId(1L));
        assertEquals("Locatário não encontrado", ex.getMessage());
        verify(locatarioRepository, times(1)).findById(1L);
    }

    @Test
    void testListarTodos_Sucesso() {
        Locatario locatario = new Locatario();
        locatario.setId(1L);
        locatario.setNome("Locatário Teste");

        LocatarioDTO locatarioDTO = new LocatarioDTO();
        locatarioDTO.setId(1L);
        locatarioDTO.setNome("Locatário Teste");

        when(locatarioRepository.findAll()).thenReturn(List.of(locatario));
        when(locatarioMapper.locatarioToDto(locatario)).thenReturn(locatarioDTO);

        List<LocatarioDTO> resultado = locatarioService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Locatário Teste", resultado.get(0).getNome());
        verify(locatarioRepository, times(1)).findAll();
    }
}
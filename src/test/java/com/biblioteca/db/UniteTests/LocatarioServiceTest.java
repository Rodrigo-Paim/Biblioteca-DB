package com.biblioteca.db.UniteTests;

import com.biblioteca.db.dto.LocatarioDTO;
import com.biblioteca.db.model.Locatario;
import com.biblioteca.db.repository.LocatarioRepository;
import com.biblioteca.db.service.LocatarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class LocatarioServiceTest {

    @Mock
    private LocatarioRepository locatarioRepository;

    @InjectMocks
    private LocatarioService locatarioService;

    private Locatario locatario;
    private LocatarioDTO locatarioDTO;

    @BeforeEach
    void setUp() {
        locatario = new Locatario();
        locatario.setId(1L);
        locatario.setNome("Maria Silva");
        locatario.setEmail("maria@test.com");
        locatario.setCpf("98765432100");
        locatario.setTelefone("11999999999");

        locatarioDTO = new LocatarioDTO();
        locatarioDTO.setNome("Maria Silva");
        locatarioDTO.setEmail("maria@test.com");
        locatarioDTO.setCpf("98765432100");
        locatarioDTO.setTelefone("11999999999");
    }

    @Test
    void criarLocatario_deveCriarComSucesso() {
        when(locatarioRepository.save(any(Locatario.class))).thenReturn(locatario);

        LocatarioDTO criado = locatarioService.criarLocatario(locatarioDTO);
        assertNotNull(criado.getId());
        verify(locatarioRepository, times(1)).save(any(Locatario.class));
    }

    @Test
    void buscarPorId_deveRetornarLocatario() {
        when(locatarioRepository.findById(1L)).thenReturn(Optional.of(locatario));

        LocatarioDTO resultado = locatarioService.buscarPorId(1L);
        assertNotNull(resultado);
        assertEquals("Maria Silva", resultado.getNome());
    }

    @Test
    void buscarPorId_deveLancarExcecaoSeNaoEncontrar() {
        when(locatarioRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> locatarioService.buscarPorId(2L));
        assertTrue(ex.getMessage().contains("não encontrado"));
    }

    @Test
    void listarTodos_deveRetornarLista() {
        when(locatarioRepository.findAll()).thenReturn(Collections.singletonList(locatario));

        var lista = locatarioService.listarTodos();
        assertEquals(1, lista.size());
        assertEquals("Maria Silva", lista.get(0).getNome());
    }

    @Test
    void excluirLocatario_deveExcluirComSucesso() {
        when(locatarioRepository.findById(1L)).thenReturn(Optional.of(locatario));

        locatarioService.excluirLocatario(1L);
        verify(locatarioRepository, times(1)).delete(locatario);
    }
}

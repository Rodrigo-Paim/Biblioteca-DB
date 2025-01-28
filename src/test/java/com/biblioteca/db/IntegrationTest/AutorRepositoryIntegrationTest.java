package com.biblioteca.db.IntegrationTest;

import com.biblioteca.db.dto.AutorDTO;
import com.biblioteca.db.model.Autor;
import com.biblioteca.db.repository.AutorRepository;
import com.biblioteca.db.service.AutorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AutorRepositoryIntegrationTest {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private AutorService autorService;


    @Test
    void deveSalvarERecuperarAutor() {
        Autor autor = new Autor();
        autor.nome = "Autor1";
        autor.cpf = "98765432101";
        autor.anoNascimento = 1975;

        Autor salvo = autorRepository.save(autor);
        assertNotNull(salvo.getId());

        Optional<Autor> encontrado = autorRepository.findById(salvo.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Autor1", encontrado.get().getNome());
    }

    @Test
    void testCriarEListarAutor() {
        AutorDTO autorDTO = new AutorDTO();
        autorDTO.setNome("Autor Teste");
        autorDTO.setCpf("12345678901");
        autorDTO.setAnoNascimento(1985);

        AutorDTO autorCriado = autorService.criarAutor(autorDTO);
        assertNotNull(autorCriado);
        assertEquals("Autor Teste", autorCriado.getNome());

        List<AutorDTO> autores = autorService.listarTodos();
        assertEquals(1, autores.size());
        assertEquals("Autor Teste", autores.get(0).getNome());
    }
}

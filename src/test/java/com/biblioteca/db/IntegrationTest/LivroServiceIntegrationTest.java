package com.biblioteca.db.IntegrationTest;

import com.biblioteca.db.dto.LivroDTO;
import com.biblioteca.db.model.Autor;
import com.biblioteca.db.repository.AutorRepository;
import com.biblioteca.db.repository.LivroRepository;
import com.biblioteca.db.service.LivroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class LivroServiceIntegrationTest {

    @Autowired
    private LivroService livroService;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    private Autor autor;

    @BeforeEach
    void setUp() {
        autor = new Autor();
        autor.nome = "Autor1";
        autor.cpf = "12312312399";
        autor.anoNascimento = 1980;
        autorRepository.save(autor);
    }

    @Test
    void criarLivro_devePersistirNoBancoH2() {
        LivroDTO livroDTO = new LivroDTO();
        livroDTO.setNome("Livro1");
        livroDTO.setIsbn("ISBN-999");
        livroDTO.setAutoresIds(Set.of(autor.getId()));

        LivroDTO criado = livroService.criarLivro(livroDTO);
        assertNotNull(criado.getId());
        assertTrue(livroRepository.existsById(criado.getId()));
        assertEquals("Livro1", criado.getNome());
    }
}

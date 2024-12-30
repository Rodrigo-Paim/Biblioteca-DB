package com.biblioteca.db.IntegrationTest;

import com.biblioteca.db.model.Autor;
import com.biblioteca.db.repository.AutorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    void deveSalvarERecuperarAutor() {
        Autor autor = new Autor();
        autor.setNome("Autor1");
        autor.setCpf("98765432101");
        autor.setAnoNascimento(1975);

        Autor salvo = autorRepository.save(autor);
        assertNotNull(salvo.getId());

        Optional<Autor> encontrado = autorRepository.findById(salvo.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Autor1", encontrado.get().getNome());
    }
}

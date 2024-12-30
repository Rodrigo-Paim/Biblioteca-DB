package com.biblioteca.db.service.external;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class BookInfoApiService {

    /**
     * @param isbn ISBN do livro
     * @throws RuntimeException caso o ISBN não seja encontrado na API
     */
    public void validarIsbnGoogle(String isbn) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null) {
            throw new RuntimeException("Falha ao consultar Google Books API (resposta nula).");
        }

        // "totalItems" é um campo retornado pela Google Books API
        Integer totalItems = (Integer) response.get("totalItems");
        if (totalItems == null || totalItems == 0) {
            throw new RuntimeException("ISBN não encontrado na Google Books API: " + isbn);
        }

        System.out.println("ISBN encontrado! totalItems = " + totalItems);
    }

    /**
     * Exemplo de método para consultar a OpenLibrary API.
     * @param isbn ISBN do livro
     */
    public void validarIsbnOpenLibrary(String isbn) {
        String url = "https://openlibrary.org/isbn/" + isbn + ".json";

        RestTemplate restTemplate = new RestTemplate();
        var response = restTemplate.getForObject(url, Map.class);

        if (response == null || response.isEmpty()) {
            throw new RuntimeException("ISBN não encontrado na OpenLibrary API: " + isbn);
        }

        System.out.println("OpenLibrary => Dados encontrados: " + response);
    }
}

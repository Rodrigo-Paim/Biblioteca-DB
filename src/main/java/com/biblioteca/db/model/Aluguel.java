package com.biblioteca.db.model;

import java.time.LocalDate;
import java.util.List;

public class Aluguel {

    private Long id;
    private Locatario locatario;
    private List<Livro> livros;
    private LocalDate dataRetirada;
    private LocalDate dataDevolucao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Locatario getLocatario() {
        return locatario;
    }

    public void setLocatario(Locatario locatario) {
        this.locatario = locatario;
    }

    public List<Livro> getLivros() {
        return livros;
    }

    public void setLivros(List<Livro> livros) {
        this.livros = livros;
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(LocalDate dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }
}

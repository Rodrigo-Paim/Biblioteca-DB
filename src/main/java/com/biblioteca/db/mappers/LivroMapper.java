package com.biblioteca.db.mappers;

import com.biblioteca.db.dto.LivroDTO;
import com.biblioteca.db.model.Livro;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LivroMapper {

    public Livro livroDtoToEntity(LivroDTO livroDTO);

    public LivroDTO livroToDto(Livro livro);
}

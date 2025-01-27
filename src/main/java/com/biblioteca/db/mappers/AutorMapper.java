package com.biblioteca.db.mappers;

import com.biblioteca.db.dto.AluguelDTO;
import com.biblioteca.db.dto.AutorDTO;
import com.biblioteca.db.model.Aluguel;
import com.biblioteca.db.model.Autor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutorMapper {

    public Autor autorDtoToEntity(AutorDTO autorDTO);

    public AutorDTO autorToDto(Autor autor);
}

package com.biblioteca.db.mappers;

import com.biblioteca.db.dto.AluguelDTO;
import com.biblioteca.db.model.Aluguel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AluguelMapper {

    public Aluguel aluguelDtoToEntity(AluguelDTO aluguelDTO);

    public AluguelDTO aluguelToDto(Aluguel aluguel);
}

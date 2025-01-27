package com.biblioteca.db.mappers;

import com.biblioteca.db.dto.LocatarioDTO;
import com.biblioteca.db.model.Locatario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocatarioMapper {

    public Locatario locatarioDtoToEntity(LocatarioDTO locatarioDTO);

    public LocatarioDTO locatarioToDto(Locatario locatario);
}

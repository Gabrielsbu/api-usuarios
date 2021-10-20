package com.dextra.makemagic.utils;

import com.dextra.makemagic.domain.dto.CharacterDTO;
import com.dextra.makemagic.domain.models.Character;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CharacterConverter {

    CharacterDTO toDTO(Character character);

    Character toModel(CharacterDTO character);

    List<CharacterDTO> toCollectionDTO(List<Character> characters);
}

package com.dextra.makemagic.domain.services;

import com.dextra.makemagic.domain.dto.CharacterDTO;
import com.dextra.makemagic.domain.dto.CreateCharacterDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CharacterService {

    List<CharacterDTO> findAllCharacters();

    CharacterDTO findCharacterById(Long characterId);

    CharacterDTO saveCharacter(CreateCharacterDTO createCharacter);

    CharacterDTO updateCharacter(Long characterId);

    ResponseEntity<Void> deleteCharacter(Long characterId);
}

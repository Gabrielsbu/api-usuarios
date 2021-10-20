package com.dextra.makemagic.domain.services.Impl;

import com.dextra.makemagic.domain.models.Character;
import com.dextra.makemagic.domain.repositories.CharacterRepository;
import com.dextra.makemagic.domain.dto.CharacterDTO;
import com.dextra.makemagic.domain.dto.CreateCharacterDTO;
import com.dextra.makemagic.domain.services.CharacterService;
import com.dextra.makemagic.exceptions.AllException;
import com.dextra.makemagic.utils.CharacterConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;
    private final CharacterConverter characterConverter;

    @Override
    public List<CharacterDTO> findAllCharacters() {
        return characterConverter.toCollectionDTO(characterRepository.findAll());
    }

    @Override
    public CharacterDTO findCharacterById(Long characterId) {
        return characterConverter.toDTO(characterRepository.findById(characterId)
                .orElseThrow(() -> new AllException("Character not found", HttpStatus.NOT_FOUND)));
    }

    @Override
    public CharacterDTO saveCharacter(CreateCharacterDTO createCharacter) {
        return null;
    }

    @Override
    public CharacterDTO updateCharacter(Long characterId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteCharacter(Long characterId) {
        Optional<Character> character = characterRepository.findById(characterId);

        if(character.isEmpty()) {
            throw new AllException("Character not found", HttpStatus.NOT_FOUND);
        }

        characterRepository.deleteById(characterId);

        return ResponseEntity.noContent().build();

    }
}

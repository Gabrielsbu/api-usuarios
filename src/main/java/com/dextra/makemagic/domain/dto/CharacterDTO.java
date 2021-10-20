package com.dextra.makemagic.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CharacterDTO {

    private UUID characterId;
    private String school;
    private String email;
    private String house;
    private String patronus;

    private LocalDateTime createCharacterAt;
}

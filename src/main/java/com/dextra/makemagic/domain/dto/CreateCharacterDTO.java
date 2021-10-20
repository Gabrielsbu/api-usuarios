package com.dextra.makemagic.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCharacterDTO {

    private String school;
    private String email;
    private String house;
    private String patronus;
}

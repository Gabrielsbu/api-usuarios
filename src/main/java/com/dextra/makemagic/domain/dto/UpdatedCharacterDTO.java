package com.dextra.makemagic.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedCharacterDTO {

    private String name;
    private String role;
    private String school;
    private String house;
    private String patronus;

    private LocalDateTime updatedCharacterAt;
}

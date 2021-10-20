package com.dextra.makemagic.domain.repositories;

import com.dextra.makemagic.domain.models.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
}
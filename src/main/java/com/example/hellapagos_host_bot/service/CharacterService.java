package com.example.hellapagos_host_bot.service;

import com.example.hellapagos_host_bot.model.GameCharacter;
import com.example.hellapagos_host_bot.repository.CharacterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;

    public CharacterService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    public List<GameCharacter> getRandomCharacters(int size) {
        return characterRepository.findRandomCharacters(size);
    }
}

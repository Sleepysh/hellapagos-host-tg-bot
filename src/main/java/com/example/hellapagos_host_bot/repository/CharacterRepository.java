package com.example.hellapagos_host_bot.repository;

import com.example.hellapagos_host_bot.model.GameCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<GameCharacter, Long> {
    @Query(value = "SELECT * FROM character ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<GameCharacter> findRandomCharacters(@Param("count") int count);
}

package com.example.hellapagos_host_bot.repository;

import com.example.hellapagos_host_bot.model.GameCharacter;
import com.example.hellapagos_host_bot.model.GameEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventsRepository extends JpaRepository<GameEvent, Long> {
    @Query(value = "SELECT * FROM event ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    GameEvent findRandomEvents();
}

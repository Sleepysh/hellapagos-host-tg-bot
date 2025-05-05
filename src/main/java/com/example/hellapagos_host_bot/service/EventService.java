package com.example.hellapagos_host_bot.service;

import com.example.hellapagos_host_bot.model.GameEvent;
import com.example.hellapagos_host_bot.repository.EventsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventsRepository eventsRepository;

    public EventService(EventsRepository eventsRepository) {
        this.eventsRepository = eventsRepository;
    }

    public GameEvent getRandomEvents() {
        return eventsRepository.findRandomEvents();
    }
}

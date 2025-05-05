package com.example.hellapagos_host_bot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity()
@Table(name = "event")
public class GameEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "quote")
    private String quote;
    @Column(name = "event_description")
    private String eventDescription;
    @Column(name = "duration")
    private String duration;

    public GameEvent() {
    }

    public GameEvent(Long id, String name, String quote, String eventDescription, String duration) {
        this.id = id;
        this.name = name;
        this.quote = quote;
        this.eventDescription = eventDescription;
        this.duration = duration;
    }

    public GameEvent(String name, String quote, String eventDescription, String duration) {
        this.name = name;
        this.quote = quote;
        this.eventDescription = eventDescription;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}

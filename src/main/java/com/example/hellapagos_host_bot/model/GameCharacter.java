package com.example.hellapagos_host_bot.model;

import jakarta.persistence.*;
import lombok.*;

@Entity()
@Table(name = "character")
public class GameCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "quote")
    private String quote;
    @Column(name = "special_conditions")
    private String specialConditions;

    public GameCharacter() {
    }

    public GameCharacter(String name, String quote, String specialConditions) {
        this.name = name;
        this.quote = quote;
        this.specialConditions = specialConditions;
    }

    public GameCharacter(Long id, String name, String quote, String specialConditions) {
        this.id = id;
        this.name = name;
        this.quote = quote;
        this.specialConditions = specialConditions;
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

    public String getSpecialConditions() {
        return specialConditions;
    }

    public void setSpecialConditions(String specialConditions) {
        this.specialConditions = specialConditions;
    }
}

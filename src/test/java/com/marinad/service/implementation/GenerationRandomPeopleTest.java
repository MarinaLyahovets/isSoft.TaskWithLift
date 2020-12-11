package com.marinad.service.implementation;

import com.marinad.entity.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenerationRandomPeopleTest {

    @Test
    void generatePerson_successfully() {
        GenerationRandomPeople generation = new GenerationRandomPeople();

        Person randomPerson = generation.generatePerson(10);

        assertNotNull(randomPerson);
    }

    @Test
    void generatePerson_invalidNumberOfFloorsInBuilding() {
        GenerationRandomPeople generation = new GenerationRandomPeople();

        assertThrows(IllegalArgumentException.class, ()->{
            generation.generatePerson(0);
        });

    }
}
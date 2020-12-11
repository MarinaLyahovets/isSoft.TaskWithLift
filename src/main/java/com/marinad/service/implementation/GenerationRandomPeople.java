package com.marinad.service.implementation;

import com.google.common.base.Preconditions;
import com.marinad.entity.Person;
import com.marinad.service.PersonService;

import java.util.Random;

public class GenerationRandomPeople implements PersonService {


    private final Random random = new Random();

    @Override
    public  Person generatePerson ( int numberOfFloorsInBuilding){
        Preconditions.checkArgument(numberOfFloorsInBuilding > 0, "Number of floors in building less than or equals 0!");

        int floorNumber = random.nextInt(numberOfFloorsInBuilding) + 1;
        int weight = random.nextInt(Person.MAX_WEIGHT) + 1;

        return Person.of(weight, floorNumber);
    }
}

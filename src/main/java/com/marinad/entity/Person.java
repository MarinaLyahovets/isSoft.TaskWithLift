package com.marinad.entity;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

@Getter
public class Person {

    public static final int MAX_WEIGHT = 150;

    private final UUID id;
    private final int weight;
    private final int floorNumber;

    private Person(int weight, int floorNumber) {
        this.id = UUID.randomUUID();
        this.weight = weight;
        this.floorNumber = floorNumber;
    }

    public static Person of(int weight, int floorNumber){
        checkArgument(weight > 0 && weight <= MAX_WEIGHT, "Incorrect weight!");
        checkArgument(floorNumber > 0, "floor number less than 0!");

        return new Person(weight, floorNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new StringBuilder("Person{")
                .append("weight: ").append(weight).append(", ")
                .append("floor number: ").append(floorNumber)
                .append("}").toString();
    }
}

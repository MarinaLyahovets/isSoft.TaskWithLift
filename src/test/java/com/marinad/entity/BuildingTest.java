package com.marinad.entity;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class BuildingTest {

    @Test
    void of_successfully() {
        Building building = Building.of(12, 5);

        assertThat(building.getNumberOfFloors(), is(12));
        assertThat(building.getNumberOfElevators(), is(5));
    }

    @Test
    void of_invalidNumberOfElevators_equal_to_NumberOfFloors() {
        assertThrows(IllegalArgumentException.class, ()->{
            Building building = Building.of(1, 1);
        });
    }

    @Test
    void of_invalidNumberOfElevators() {
        assertThrows(IllegalArgumentException.class, ()->{
            Building building = Building.of(12, 0);
        });
    }

    @Test
    void getFloors() {
        Building building = Building.of(3, 2);

        assertThat(building.getFloors().size(), is(3));
    }

    @Test
    void getElevators() {
        Building building = Building.of(3, 2);

        assertThat(building.getElevators().size(), is(2));

    }
}
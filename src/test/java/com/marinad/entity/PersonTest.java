package com.marinad.entity;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void of_successfully() {
        Person person= Person.of(40, 2);

        assertThat(person.getWeight(), is(40));
        assertThat(person.getFloorNumber(), is(2));
    }

    @Test
    void of_invalidWeight() {
        assertThrows(IllegalArgumentException.class, ()->{
            Person.of(0, 2);
        });
    }

    @Test
    void of_invalidFloorNumber() {
        assertThrows(IllegalArgumentException.class, ()->{
            Person.of(40, 0);
        });
    }


}
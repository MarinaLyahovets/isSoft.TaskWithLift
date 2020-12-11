package com.marinad.entity;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

class CallTest {

    @Test
    void of_successfully() {
        Call call = Call.of(2, Direction.DOWN);

        assertThat(call.getNumber(), is(2));
        assertThat(call.getDirection(), is(Direction.DOWN));
    }

    @Test
    void of_invalidDirection() {
        assertThrows(NullPointerException.class, ()->{
            Call.of(2, null);
        });

    }

    @Test
    void of_invalidFloorNumber() {
        assertThrows(IllegalArgumentException.class, ()->{
            Call.of(0, Direction.DOWN);
        });
    }


}
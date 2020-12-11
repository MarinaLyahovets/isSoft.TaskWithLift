package com.marinad.statistics;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

class ServiceStatisticsTest {

    @Test
    void of_invalidNumberFloorFrom() {
        assertThrows(IllegalArgumentException.class, ()->{
            ServiceStatistics.of(0, 2);
        });
    }

    @Test
    void of_invalidNumberFloorTo() {
        assertThrows(IllegalArgumentException.class, ()->{
            ServiceStatistics.of(2, 0);
        });
    }

    @Test
    void of_successfully() {
        ServiceStatistics serviceStatistics = ServiceStatistics.of(1,5);

        assertThat(serviceStatistics.getNumberFloorTo(), is(5));
        assertThat(serviceStatistics.getNumberFloorFrom(), is(1));
    }
}
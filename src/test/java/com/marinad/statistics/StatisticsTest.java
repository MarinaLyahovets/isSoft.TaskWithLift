package com.marinad.statistics;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Map;


class StatisticsTest {

    @Test
    void addStatistics_successfully() {
        Statistics statistics = new Statistics();
        Map<ServiceStatistics, Integer> serviceMap = new HashMap<>();
        serviceMap.put( ServiceStatistics.of(1, 2), 1);
        statistics.addStatistics(serviceMap);

        assertThat(statistics.getStatisticsMap().size(), is(1));
    }

    @Test
    void addStatistics_invalidNull() {
        Statistics statistics = new Statistics();

        assertThrows(NullPointerException.class, ()->{
            statistics.addStatistics(null);
        });
    }
}
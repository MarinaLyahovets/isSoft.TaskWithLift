package com.marinad.statistics;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class Statistics {
    private final Map<ServiceStatistics, Integer> statisticsMap = new HashMap<>();

    public void addStatistics(Map<ServiceStatistics, Integer> serviceMap) {
        checkNotNull(serviceMap, "Service map is null");

        serviceMap.forEach((key, value) -> {
            createEntryIfNotExists(key);

            statisticsMap.put(key, statisticsMap.get(key) + value);
            log.info("Statistics: {} = {}", key, statisticsMap.get(key));
        });
    }

    private boolean createEntryIfNotExists(ServiceStatistics service) {
        checkNotNull(service, "Service is null");

        if (!statisticsMap.containsKey(service)) {
            statisticsMap.put(service, 0);
            return true;
        }
        return false;
    }

    public Map<ServiceStatistics, Integer> getStatisticsMap() {
        return Map.copyOf(statisticsMap);
    }
}

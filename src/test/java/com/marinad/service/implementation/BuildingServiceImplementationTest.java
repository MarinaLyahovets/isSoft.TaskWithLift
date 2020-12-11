package com.marinad.service.implementation;

import com.marinad.entity.Elevator;
import com.marinad.entity.Floor;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BuildingServiceImplementationTest {

    BuildingServiceImplementation buildingService = new BuildingServiceImplementation();


    @Test
    void generationFloor_successfully() {
        List<Floor> floorList = buildingService.generationFloor(3);

        assertThat(floorList.size(), is(3));
    }

    @Test
    void generationFloor_invalidNumberOfFloors() {

        assertThrows(IllegalArgumentException.class, ()->{
            buildingService.generationFloor(0);
        });
    }

    @Test
    void generationListElevator_successfully() {
        List<Elevator> elevatorList = buildingService.generationListElevator(3);

        assertThat(elevatorList.size(), is(3));

    }

    @Test
    void generationListElevator_invalidNumberOfElevators() {
        assertThrows(IllegalArgumentException.class, ()->{
            buildingService.generationListElevator(0);
        });
    }
}
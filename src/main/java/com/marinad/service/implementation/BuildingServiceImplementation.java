package com.marinad.service.implementation;

import com.marinad.entity.Elevator;
import com.marinad.entity.Floor;
import com.marinad.entity.KindFloor;
import com.marinad.service.BuildingService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class BuildingServiceImplementation implements BuildingService {

    @Override
    public List<Floor> generationFloor(int numberOfFloors) {

        checkArgument(numberOfFloors > 0, "Number of floors less than 0 or equals 0!");

        List<Floor> floorList = new ArrayList<>();
        int i = 1;
        while (i <= numberOfFloors){
            KindFloor kindFloor;
            if(i == 1) {
                kindFloor = KindFloor.FIRST;
            }
            else if (i == numberOfFloors){
                kindFloor = KindFloor.LAST;
            }
            else {
                kindFloor = KindFloor.OTHER;
            }
            Floor floor = Floor.of( i, kindFloor);
            floor.setName("Floor: " + i);
            floorList.add(floor);
            i++;
        }
        return floorList;
    }

    @Override
    public List<Elevator> generationListElevator(int numberOfElevator){
        checkArgument(numberOfElevator > 0, "Number of elevators less than 0 or equals 0!");

        List<Elevator> elevatorList = new ArrayList<>();
        int i = 1;
        while (i <= numberOfElevator){
            Elevator elevator = new Elevator();
            elevator.setName("Elevator" + i);
            elevatorList.add(elevator);
            i++;
        }

        return elevatorList;
    }
}

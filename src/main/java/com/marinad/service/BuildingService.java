package com.marinad.service;

import com.marinad.entity.Elevator;
import com.marinad.entity.Floor;

import java.util.List;

public interface BuildingService {

    List<Floor> generationFloor(int numberOfFloors);
    List<Elevator> generationListElevator (int numberOfElevators);

}

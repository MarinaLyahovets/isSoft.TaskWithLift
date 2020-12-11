package com.marinad.entity;

import com.marinad.service.BuildingService;
import com.marinad.service.CallService;
import com.marinad.service.FloorService;
import com.marinad.service.implementation.BuildingServiceImplementation;
import com.marinad.service.implementation.GenerationRandomPeople;
import com.marinad.service.implementation.ServiceCall;
import com.marinad.service.implementation.ServiceFloor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Getter
public class Building {


    private final int numberOfFloors;
    private final int numberOfElevators;
    private final List<Floor> floors;
    private final BlockingQueue<Call> callQueue = new LinkedBlockingQueue<>();
    private final List<Elevator> elevators;



    @SneakyThrows
    private Building(int numberOfFloors, int numberOfElevators) {

        log.info("Building is creating");

        BuildingService buildingService = new BuildingServiceImplementation();

        this.numberOfFloors = numberOfFloors;
        this.numberOfElevators = numberOfElevators;
        this.floors = buildingService.generationFloor(numberOfFloors);
        this.elevators = buildingService.generationListElevator(numberOfElevators);

        log.info("Building created");

    }

    public static Building of(int numberOfFloors, int numberOfElevators){
        checkArgument(numberOfFloors > 1 && numberOfFloors <= 20, "Incorrect number of floors");
        checkArgument(numberOfElevators < numberOfFloors && numberOfElevators > 0, "Incorrect number of elevators");

        return new Building(numberOfFloors, numberOfElevators);
    }

    public void start(){
        CallService serviceCall = ServiceCall.of(callQueue);
        FloorService serviceFloor = ServiceFloor.of(floors);
        GenerationRandomPeople generationRandomPeople = new GenerationRandomPeople();

        floors.forEach(floor -> {
            floor.setCallService(serviceCall);
            floor.setServiceFloor(serviceFloor);
            floor.setGenerationRandomPeople(generationRandomPeople);
            floor.start();
        });

        elevators.forEach(elevator -> {
            elevator.setCallService(serviceCall);
            elevator.setFloorService(serviceFloor);
            elevator.start();
        });
    }

    public List<Floor> getFloors() {
        return List.copyOf(floors);
    }

    public List<Elevator> getElevators() {
        return List.copyOf(elevators);
    }

    public List<Call> getCallQueue() {
        return List.copyOf(callQueue);
    }
}

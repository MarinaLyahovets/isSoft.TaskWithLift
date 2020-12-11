package com.marinad.entity;

import com.marinad.service.CallService;
import com.marinad.service.FloorService;
import com.marinad.statistics.ServiceStatistics;
import com.marinad.statistics.Statistics;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class Elevator extends Thread {

    public static final int LIFTING_CAPACITY = 400;
    public static final int DOOR_SPEED = 1;
    public static final int LIFT_SPEED = 1;
    public static final int STANDING_TIME = 2;
    public static final int MAX_NUMBER_OF_FLOORS = 1000;
    public static final int MIN_NUMBER_OF_FLOORS = 0;

    private final UUID id;

    private Direction direction;
    private Floor floor;
    private int weight;
    private final List<Person> personList = new ArrayList<>();
    private boolean isDoorOpened;

    private boolean stop;

    private FloorService serviceFloor;
    private CallService serviceCall;

    private final Statistics statistics = new Statistics();
    private final Map<ServiceStatistics, Integer> serviceStatisticsMap = new HashMap<>();

    public Elevator() {
        this.id = UUID.randomUUID();
        this.direction = null;
        this.weight = 0;
        this.stop = false;
        this.isDoorOpened = false;
    }

    @SneakyThrows
    @Override
    public void run() {

        checkNotNull(serviceCall, "Service Call is null!");
        checkNotNull(serviceFloor, "Service floor is null!");

        floor = serviceFloor.getFloor(1);

        while (!isStop()) {
            Call call = getNextCall();
            this.direction = call.getDirection();
            int minFloor = MAX_NUMBER_OF_FLOORS;
            int maxFloor = MIN_NUMBER_OF_FLOORS;

            floor = serviceFloor.moveToFloorOnWhichCall(floor.getFloorNumber(), call.getNumber(), direction);

            while (floor.getFloorNumber() != maxFloor && floor.getFloorNumber() != minFloor) {

                landingPeople();
                if (direction == Direction.UP) {
                    int maxRequiredFloorNumber = addAllPersonUp();

                    maxFloor = Math.max(maxFloor, maxRequiredFloorNumber);

                    if(floor.getFloorNumber() == call.getNumber() && personList.isEmpty()){
                        break;
                    }

                    needCloseDoor();
                    TimeUnit.SECONDS.sleep(LIFT_SPEED);

                    floor = serviceFloor.nextFloor(floor, direction);

                    if(floor.getFloorNumber() == maxFloor)
                    {
                        landingPeople();
                        needCloseDoor();
                    }
                }

                else if (direction == Direction.DOWN) {
                    int minRequiredFloorNumber = addAllPersonDown();
                    minFloor = Math.min(minFloor, minRequiredFloorNumber);

                    if(floor.getFloorNumber() == call.getNumber() && personList.isEmpty()){
                        break;
                    }

                    needCloseDoor();
                    TimeUnit.SECONDS.sleep(LIFT_SPEED);

                    floor = serviceFloor.nextFloor(floor, direction);

                    if(floor.getFloorNumber() == minFloor)
                    {
                        landingPeople();
                        needCloseDoor();
                    }
                }
            }
        }
    }

    public void setWeight(int weight) {
        checkArgument(weight >= 0 && weight <= LIFTING_CAPACITY, "Weight less than 0 or more than lifting capacity");

        this.weight = weight;
    }


    public void setCallService(CallService serviceCall) {
        checkNotNull(serviceCall, "Service call is null!");
        this.serviceCall = serviceCall;
    }

    public void setFloorService(FloorService serviceFloor) {
        checkNotNull(serviceFloor, "Service floor is null!");
        this.serviceFloor = serviceFloor;
    }

    public void setFloor(Floor floor){
        checkNotNull(floor, "Floor is null!");

        this.floor = floor;
    }

    public boolean isStop() {
        return stop;
    }

    public void stopTheThread(){
        stop = true;
    }

    public int addPerson(Person person){
        checkNotNull(person, "Person is null!");
        checkNotNull(floor, "Floor is null!");

        personList.add(person);
        weight += person.getWeight();
        log.info("Floor: {}, Add {}", floor.getFloorNumber(), person);
        log.info("Load elevator = {}", weight);
        addServiceStatistics(person);
        return person.getFloorNumber();
    }

    private void addServiceStatistics(Person person) {
        checkNotNull(person, "Person is null!");

        ServiceStatistics serviceStatistics = ServiceStatistics.of(floor.getFloorNumber(), person.getFloorNumber());

        if(!serviceStatisticsMap.containsKey(serviceStatistics)) {
            serviceStatisticsMap.put(serviceStatistics, 0);
        }

        serviceStatisticsMap.put(serviceStatistics, serviceStatisticsMap.get(serviceStatistics) + 1);
    }

    private int addPersonDown(){
        floor.offClickedDown();
        serviceCall.removeCall(floor.getFloorNumber(), direction);

        Person personAdd = floor.takePersonFromQueueDown(LIFTING_CAPACITY - this.weight);
        if(personAdd != null){
            return addPerson(personAdd);
        }

        return 0 ;
    }

    @SneakyThrows
    private void needCloseDoor(){
        if(isDoorOpened()){
            TimeUnit.SECONDS.sleep(STANDING_TIME);
            closeTheDoor();
        }
    }

    private int addAllPersonDown(){
        int minRequired = 100;
        while (!floor.isEmptyQueueDown()){
              int requiredFloorNumber = addPersonDown();
            if(requiredFloorNumber == 0){
                break;
            }
              minRequired = Math.min(minRequired, requiredFloorNumber);
        }
        needAddCallDown(floor);

        return minRequired;
    }

    private int addPersonUp(){
        floor.offClickedUp();
        serviceCall.removeCall(floor.getFloorNumber(), direction);

        Person personAdd = floor.takePersonFromQueueUp(LIFTING_CAPACITY - this.weight);

        if(personAdd != null){
            return addPerson(personAdd);
        }

        return 0 ;
    }

    @SneakyThrows
    private int addAllPersonUp(){
        int maxRequired = 0;
        while (!floor.isEmptyQueueUp()){
            if(!isDoorOpened()) {
                openTheDoor();
            }

            int requiredFloorNumber = addPersonUp();
            if(requiredFloorNumber == 0){
                break;
            }
            maxRequired = Math.max(maxRequired, requiredFloorNumber);
        }

        needAddCallUp(floor);

        return maxRequired;
    }


    private Call getNextCall(){
        if(!floor.isEmptyQueueUp()) {
            return serviceCall.removeCall(floor.getFloorNumber(), Direction.UP);
        }
        else if(!floor.isEmptyQueueDown()){
            return serviceCall.removeCall(floor.getFloorNumber(), Direction.DOWN);
        }
        else {
            return serviceCall.takeCall();
        }

    }

    private void needAddCallDown(Floor floor){
        checkNotNull(floor, "Floor is null!");

        if (floor.needToPressTheButtonDown()) {
            floor.onClickedDown();
            serviceCall.addCall(floor.getFloorNumber(), direction);
        }

    }

    private void needAddCallUp(Floor floor){
        checkNotNull(floor, "Floor is null!");

        if (floor.needToPressTheButtonUp()) {
            floor.onClickedUp();
            serviceCall.addCall(floor.getFloorNumber(), direction);
        }

    }

    public boolean isDoorOpened() {
        return isDoorOpened;
    }

    @SneakyThrows
    public void closeTheDoor(){
        TimeUnit.SECONDS.sleep(DOOR_SPEED);
        isDoorOpened = false;
    }
    @SneakyThrows
    public void openTheDoor(){
        TimeUnit.SECONDS.sleep(DOOR_SPEED);
        isDoorOpened = true;
    }

    public void landingPeople(){
        List<Person> people = personList.stream().filter(person -> person.getFloorNumber() == floor.getFloorNumber())
                .collect(Collectors.toList());

        if (!people.isEmpty()) {
            if(!isDoorOpened())
            {
                openTheDoor();
            }

            personList.removeAll(people);
            int weightPeopleRemoved = people.stream().mapToInt(Person::getWeight).sum();
            this.weight -= weightPeopleRemoved;

            log.info("Floor: {},remove: {}", floor.getFloorNumber(), people);
            addToTotalStatistics();
        }
    }

    private void addToTotalStatistics() {
        Map<ServiceStatistics, Integer> serviceToAddMap = serviceStatisticsMap.entrySet().stream()
                .filter(entry -> entry.getKey().getNumberFloorTo() == floor.getFloorNumber())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        statistics.addStatistics(serviceToAddMap);
        serviceToAddMap.keySet().forEach(serviceStatisticsMap::remove);
    }

    public List<Person> getPersonList() {
        return List.copyOf(personList);
    }

    public Map<ServiceStatistics, Integer> getServiceStatisticsMap() {
        return Map.copyOf(serviceStatisticsMap);
    }

    public Direction getDirection() {
        return direction;
    }

    public Floor getFloor() {
        return floor;
    }

    public int getWeight() {
        return weight;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elevator elevator = (Elevator) o;
        return Objects.equals(id, elevator.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


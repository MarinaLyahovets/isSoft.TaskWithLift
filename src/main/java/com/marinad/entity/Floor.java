package com.marinad.entity;

import com.marinad.service.CallService;
import com.marinad.service.FloorService;
import com.marinad.service.implementation.GenerationRandomPeople;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.*;

@Slf4j
public class Floor extends Thread {

    @Getter
    private boolean stopped;
    @Getter
    private final int floorNumber;
    @Getter
    private final KindFloor kindFloor;
    @Getter
    private boolean isClickedDown;
    @Getter
    private boolean isClickedUp;

    private CallService serviceCall;
    private FloorService serviceFloor;
    @Getter
    private GenerationRandomPeople generationRandomPeople;

    private final Queue<Person>  queueDown;
    private final Queue<Person>  queueUp;

    private final Lock lock = new ReentrantLock(true);

    private Floor(int floorNumber, KindFloor kindFloor){
        log.debug("Floor is creating");
        this.floorNumber = floorNumber;
        this.kindFloor = kindFloor;
        this.queueDown = new ConcurrentLinkedQueue<>();
        this.queueUp = new ConcurrentLinkedQueue<>();
        log.debug("Floor is created");
    }

    public static Floor of(int floorNumber, KindFloor kindFloor){
        checkArgument(floorNumber > 0, "Floor number is less or equals 0");
        checkNotNull(kindFloor, "Kind floor is null!");

        return new Floor(floorNumber, kindFloor);
    }

    public void addPersonUp (Person person){
        checkNotNull(person, "Person is null");
        checkArgument(floorNumber < person.getFloorNumber(), "The person has the wrong floor");

        queueUp.add(person);
    }


    public void addPersonDown (Person person){
        checkNotNull(person, "Person is null");
        checkArgument(floorNumber > person.getFloorNumber(), "The person has the wrong floor");

        queueDown.add(person);
    }

    public List<Person> getQueueDown() {
        return List.copyOf(queueDown);
    }

    public List<Person> getQueueUp() {
        return List.copyOf(queueUp);
    }

    @SneakyThrows
    @Override
    public void run() {

        checkNotNull(serviceCall, "Service call is null");
        checkNotNull(serviceFloor, "Service floor is null");
        checkNotNull(generationRandomPeople, "Generation people is null");

        while (!isStopped()) {

            Person person = generationRandomPeople.generatePerson(serviceFloor.getNumberOfFloors());

            if (floorNumber > person.getFloorNumber()) {
                addPersonDown(person);
                serviceCall.addCall(floorNumber, Direction.DOWN);

            } else if (floorNumber < person.getFloorNumber()) {
                addPersonUp(person);
                serviceCall.addCall(floorNumber, Direction.UP);
            }
            log.info("Up: {}, Down: {}", queueUp, queueDown);
            TimeUnit.SECONDS.sleep(9);
        }
    }


    public void setCallService(CallService serviceCall) {
        checkNotNull(serviceCall, "Service call is null");

        this.serviceCall = serviceCall;
    }

    public void setServiceFloor(FloorService serviceFloor) {
        checkNotNull(serviceFloor, "Service floor is null");


        this.serviceFloor = serviceFloor;
    }

    public void setGenerationRandomPeople(GenerationRandomPeople generationRandomPeople) {
        checkNotNull(generationRandomPeople, "Generation people is null");

        this.generationRandomPeople = generationRandomPeople;
    }


    public void stoppedThread(){
        stopped = true;
    }

    public void offClickedDown(){
         isClickedDown = false;
    }

    public void onClickedDown(){
        isClickedDown = true;
    }

    public void offClickedUp(){
        isClickedUp = false;
    }

    public void onClickedUp(){
        isClickedUp = true;
    }



    public Person takePersonFromQueueUp(int freeWeightInElevator){
        checkArgument(freeWeightInElevator >= 0, "free lifting capacity less than 0!");

        lock.lock();

        Person person = null;
        Person firstPerson = this.queueUp.peek();

        if( firstPerson != null && firstPerson.getWeight() <= freeWeightInElevator){
            person = this.queueUp.poll();
            log.debug("Taken a person from the queue up: {}", firstPerson);
        }

        lock.unlock();

        return person;
    }

    public Person takePersonFromQueueDown(int freeWeightInElevator){
        checkArgument(freeWeightInElevator >= 0, "free lifting capacity less than 0!");

        lock.lock();

        Person person = null;
        Person firstPerson = this.queueDown.peek();

        if( firstPerson != null && firstPerson.getWeight() <= freeWeightInElevator){
            person = this.queueDown.poll();
            log.debug("Taken a person from the queue down: {}", person);
        }

        lock.unlock();

        return person;
    }

    public boolean isEmptyQueueUp(){
        return this.queueUp.isEmpty();
    }

    public boolean isEmptyQueueDown(){
        return this.queueDown.isEmpty();
    }

    public boolean needToPressTheButtonDown(){
        if(isEmptyQueueDown()){
            return false;
        }

        return true;
    }

    public boolean needToPressTheButtonUp(){
        if(isEmptyQueueUp()){
            return false;
        }

        return true;
    }

}

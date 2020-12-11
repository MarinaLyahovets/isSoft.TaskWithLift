package com.marinad.entity;

import com.marinad.service.CallService;
import com.marinad.service.FloorService;
import com.marinad.service.implementation.GenerationRandomPeople;
import com.marinad.service.implementation.ServiceCall;
import com.marinad.service.implementation.ServiceFloor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class FloorTest {

    private Floor floor;

    @BeforeEach
    void addFloor(){
        floor = Floor.of(5, KindFloor.OTHER);
    }

    @Test
    void of_successfully() {
        Floor floorAdd = Floor.of(10, KindFloor.LAST);

        assertThat(floorAdd.getFloorNumber(), is(10));
        assertThat(floorAdd.getKindFloor(), is(KindFloor.LAST));
    }

    @Test
    void of_invalidNullKindFloor() {
        assertThrows(NullPointerException.class, ()->{
            Floor.of(2, null);
        });
    }

    @Test
    void of_invalidFloorNumber() {
        assertThrows(IllegalArgumentException.class, ()->{
            Floor.of(0, KindFloor.FIRST);
        });
    }

    @Test
    void getQueueDown_successfully() {
        Person person1 = Person.of(40, 2);
        Person person2 = Person.of(50, 3);

        floor.addPersonDown(person1);
        floor.addPersonDown(person2);
    }

    @Test
    void getQueueDown_invalid() {
        Person person1 = Person.of(40, 2);
        Person person2 = Person.of(50, 3);

        Queue<Person> queuePerson = new ConcurrentLinkedQueue<>();
        queuePerson.add(person1);

        floor.addPersonDown(person1);
        floor.addPersonDown(person2);

        assertNotEquals(floor.getQueueDown(), queuePerson);
    }

    @Test
    void getQueueUp_successfully() {
        Person person1 = Person.of(40, 8);
        Person person2 = Person.of(50, 6);

        floor.addPersonUp(person1);
        floor.addPersonUp(person2);
    }

    @Test
    void getQueueUp_invalid() {
        Person person1 = Person.of(40, 8);
        Person person2 = Person.of(50, 6);

        Queue<Person> queuePerson = new ConcurrentLinkedQueue<>();
        queuePerson.add(person1);

        floor.addPersonUp(person1);
        floor.addPersonUp(person2);

        assertNotEquals(floor.getQueueUp(), queuePerson);
    }

    @Test
    void setCallService_invalidNullFloor() {

        assertThrows(NullPointerException.class, ()->{
            floor.setCallService(null);
        });
    }

    @Test
    void setCallService_successfully() {
        CallService callService = ServiceCall.of(new LinkedBlockingQueue<>());
        floor.setCallService(callService);
    }


    @Test
    void setServiceFloor_successfully() {
        FloorService floorService = ServiceFloor.of(new ArrayList<Floor>());
        floor.setServiceFloor(floorService);
    }

    @Test
    void setServiceFloor_invalidNullFloor() {
        assertThrows(NullPointerException.class, ()->{
            floor.setServiceFloor(null);
        });
    }


    @Test
    void setGenerationRandomPeople_successfully() {
        GenerationRandomPeople generationRandomPeople = new GenerationRandomPeople();
        floor.setGenerationRandomPeople(generationRandomPeople);

        assertThat(floor.getGenerationRandomPeople(), is(generationRandomPeople));
    }

    @Test
    void setGenerationRandomPeople_invalidNull() {
        assertThrows(NullPointerException.class, ()->{
            floor.setGenerationRandomPeople(null);
        });
    }

    @Test
    void takePersonFromQueueUp_successfully() {
       Person person = floor.takePersonFromQueueUp(200);

       assertNull(person);
    }

    @Test
    void takePersonFromQueueUp_invalidFreeWeight() {
        assertThrows(IllegalArgumentException.class, ()->{
            floor.takePersonFromQueueUp(-12);
        });
    }

    @Test
    void takePersonFromQueueDown_successfully() {
        Person person = floor.takePersonFromQueueDown(200);

        assertNull(person);
    }

    @Test
    void takePersonFromQueueDown_invalidFreeWeight() {
        assertThrows(IllegalArgumentException.class, ()->{
            floor.takePersonFromQueueDown(-12);
        });
    }

    @Test
    void run_invalidNullServiceFloor() {
        GenerationRandomPeople generationRandomPeople = new GenerationRandomPeople();
        floor.setGenerationRandomPeople(generationRandomPeople);
        CallService callService = ServiceCall.of(new LinkedBlockingQueue<>());
        floor.setCallService(callService);

       assertThrows(NullPointerException.class, ()->{
           floor.run();
       });
    }

    @Test
    void run_invalidNullServiceCall() {
        FloorService floorService = ServiceFloor.of(new ArrayList<Floor>());
        floor.setServiceFloor(floorService);
        GenerationRandomPeople generationRandomPeople = new GenerationRandomPeople();
        floor.setGenerationRandomPeople(generationRandomPeople);

        assertThrows(NullPointerException.class, ()->{
            floor.run();
        });
    }

    @Test
    void run_invalidNullGenerationPeople() {
        FloorService floorService = ServiceFloor.of(new ArrayList<Floor>());
        floor.setServiceFloor(floorService);
        CallService callService = ServiceCall.of(new LinkedBlockingQueue<>());
        floor.setCallService(callService);

        assertThrows(NullPointerException.class, ()->{
            floor.run();
        });
    }

    @Test
    void run_successfully() {
        FloorService floorService = ServiceFloor.of(new ArrayList<Floor>());
        floor.setServiceFloor(floorService);
        CallService callService = ServiceCall.of(new LinkedBlockingQueue<>());
        floor.setCallService(callService);
        GenerationRandomPeople generationRandomPeople = new GenerationRandomPeople();
        floor.setGenerationRandomPeople(generationRandomPeople);

        floor.stoppedThread();
        floor.start();

        assertThat(floor.getGenerationRandomPeople(), is(generationRandomPeople));

    }

    @Test
    void addPersonUp_successfully() {
        Person person = Person.of(40, 8);
        floor.addPersonUp(person);

        assertThat(floor.getQueueUp().size(), is(1));
    }

    @Test
    void addPersonUp_invalidNullPerson() {
        assertThrows(NullPointerException.class, ()->{
            floor.addPersonUp(null);
        });

        assertThrows(IllegalArgumentException.class, ()->{
            floor.addPersonUp(Person.of(40, 2));
        });
    }

    @Test
    void addPersonDown_successfully() {
        Person person = Person.of(40, 3);
        floor.addPersonDown(person);

        assertThat(floor.getQueueDown().size(), is(1));
    }

    @Test
    void addPersonDown_invalidNullPerson() {
        assertThrows(NullPointerException.class, ()->{
            floor.addPersonDown(null);
        });

        assertThrows(IllegalArgumentException.class, ()->{
            floor.addPersonDown(Person.of(40, 7));
        });
    }
}
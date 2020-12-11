package com.marinad.entity;

import com.marinad.service.CallService;
import com.marinad.service.FloorService;
import com.marinad.service.implementation.ServiceCall;
import com.marinad.service.implementation.ServiceFloor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ElevatorTest {

    @Test
    void run_invalidServiceCall() {
        Elevator elevator = new Elevator();
        FloorService service = ServiceFloor.of(new ArrayList<Floor>());
        elevator.setFloorService(service);

        assertThrows(NullPointerException.class, ()->{
            elevator.run();
        });
    }

    @Test
    void run_invalidServiceFloor() {
        Elevator elevator = new Elevator();
        CallService service = ServiceCall.of(new LinkedBlockingQueue<Call>());
        elevator.setCallService(service);

        assertThrows(NullPointerException.class, ()->{
            elevator.run();
        });
    }

    @Test
    void run_successfully() {
        Elevator elevator = new Elevator();
        BlockingQueue<Call> queueCall = new LinkedBlockingQueue<>();
        CallService serviceCall = ServiceCall.of(queueCall);
        serviceCall.addCall(1, Direction.UP);
        elevator.setCallService(serviceCall);
        List<Floor> listFloor = new ArrayList<>();
        listFloor.add(Floor.of(1, KindFloor.FIRST));
        FloorService serviceFloor = ServiceFloor.of(listFloor);
        elevator.setFloorService(serviceFloor);

        elevator.stopTheThread();
        elevator.start();

    }

    @Test
    void setWeight_invalid() {
        Elevator  elevator = new Elevator();

        assertThrows(IllegalArgumentException.class, ()->{
            elevator.setWeight(-12);
        });

        assertThrows(IllegalArgumentException.class, ()->{
            elevator.setWeight(500);
        });
    }

    @Test
    void setWeight_successfully() {
        Elevator elevator = new Elevator();
        elevator.setWeight(40);

        assertThat(elevator.getWeight(), is(40));
    }

    @Test
    void setCallService_invalidNull() {
        Elevator elevator = new Elevator();

        assertThrows(NullPointerException.class, ()->{
            elevator.setCallService(null);
        });

    }

    @Test
    void setCallService_successfully() {
        Elevator elevator = new Elevator();
        BlockingQueue<Call> queueCall = new LinkedBlockingQueue<>();
        CallService serviceCall = ServiceCall.of(queueCall);
        serviceCall.addCall(1, Direction.UP);
        elevator.setCallService(serviceCall);

    }

    @Test
    void setFloorService_invalidNull() {
        Elevator elevator = new Elevator();

        assertThrows(NullPointerException.class, ()->{
            elevator.setFloorService(null);
        });

    }

    @Test
    void setFloorService_successfully() {
        Elevator elevator = new Elevator();
        List<Floor> listFloor = new ArrayList<>();
        listFloor.add(Floor.of(1, KindFloor.FIRST));
        FloorService serviceFloor = ServiceFloor.of(listFloor);
        elevator.setFloorService(serviceFloor);
    }

    @Test
    void addPerson_invalidNullPerson() {
        Elevator elevator = new Elevator();

        assertThrows(NullPointerException.class, ()->{
            elevator.addPerson(null);
        });
    }

    @Test
    void addPerson_invalidNullFloor() {
        Elevator elevator = new Elevator();



        assertThrows(NullPointerException.class, ()->{
            elevator.addPerson(Person.of(40, 1));
        });
    }

    @Test
    void addPerson_successfully() {
        Elevator elevator = new Elevator();
        elevator.setFloor(Floor.of(1, KindFloor.FIRST));
        elevator.addPerson(Person.of(40, 5));

        assertThat(elevator.getPersonList().size(), is(1));
    }

    @Test
    void setFloor_invalid() {
        Elevator elevator = new Elevator();

        assertThrows(NullPointerException.class, ()->{
            elevator.setFloor(null);
        });
    }

    @Test
    void setFloor_successfully() {
        Elevator elevator = new Elevator();
        Floor floor = Floor.of(1, KindFloor.FIRST);
        elevator.setFloor(floor);

        assertThat(elevator.getFloor(),  is(floor));
    }
}
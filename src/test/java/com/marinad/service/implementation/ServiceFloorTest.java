package com.marinad.service.implementation;

import com.marinad.entity.Direction;
import com.marinad.entity.Floor;
import com.marinad.entity.KindFloor;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServiceFloorTest {

    private ServiceFloor serviceFloor;
    private List<Floor> floorList;

    void initialize(){
        floorList = new ArrayList<>();
        floorList.add(Floor.of(1, KindFloor.FIRST));
        floorList.add(Floor.of(2, KindFloor.OTHER));
        floorList.add(Floor.of(3, KindFloor.LAST));
        serviceFloor = ServiceFloor.of(floorList);
    }

    @Test
    void of_successfully() {
        initialize();

        assertEquals(serviceFloor.getFloorList(), floorList);
    }

    @Test
    void of_invalidNullFloorList() {
        assertThrows(NullPointerException.class, ()->{
            ServiceFloor.of(null);
        });
    }

    @Test
    void getFloor_successfully() {
        initialize();
        Floor floor = serviceFloor.getFloor(1);

        assertEquals(floor, floorList.get(0));
    }

    @Test
    void getFloor_invalidNumberFloor() {
       initialize();

        assertThrows(IllegalArgumentException.class, ()->{
             serviceFloor.getFloor(0);
        });
    }

    @Test
    void nextFloor_successfullyUp() {
        initialize();
        Floor floor = serviceFloor.nextFloor(Floor.of(1, KindFloor.FIRST), Direction.UP);

        assertEquals(serviceFloor.getFloor(2), floor);
    }

    @Test
    void nextFloor_successfullyDown() {
        initialize();
        Floor floor = serviceFloor.nextFloor(Floor.of(2, KindFloor.LAST), Direction.DOWN);

        assertEquals(serviceFloor.getFloor(1), floor);
    }

    @Test
    void nextFloor_invalidNullFloor() {
        initialize();

        assertThrows(NullPointerException.class, ()->{
            serviceFloor.nextFloor(Floor.of(1, KindFloor.FIRST), null);
        });
    }

    @Test
    void nextFloor_invalidNullDirection() {
        initialize();

        assertThrows(NullPointerException.class, ()->{
            serviceFloor.nextFloor(null, Direction.UP);
        });
    }

    @Test
    void moveToFloorOnWhichCall_successfully() {
        initialize();
        Floor floor = serviceFloor.moveToFloorOnWhichCall(1, 3, Direction.DOWN);

        assertEquals(floor, serviceFloor.getFloor(3));

        floor = serviceFloor.moveToFloorOnWhichCall(1, 3, Direction.UP);

        assertEquals(floor, serviceFloor.getFloor(1));


    }

    @Test
    void moveToFloorOnWhichCall_invalidFloorNumber() {
        initialize();

        assertThrows(IllegalArgumentException.class, ()->{
            serviceFloor.moveToFloorOnWhichCall(0, 2, Direction.DOWN);
        });

        assertThrows(IllegalArgumentException.class, ()->{
            serviceFloor.moveToFloorOnWhichCall(1, 0, Direction.DOWN);
        });
    }

    @Test
    void moveToFloorOnWhichCall_invalidNullDirection() {
        initialize();

        assertThrows(NullPointerException.class, ()->{
            serviceFloor.moveToFloorOnWhichCall(1, 2, null);
        });
    }

    @Test
    void getNumberOfFloors_successfully() {
        initialize();

        assertThat( serviceFloor.getNumberOfFloors(), is(3));
    }
}
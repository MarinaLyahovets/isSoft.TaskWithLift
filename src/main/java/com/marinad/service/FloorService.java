package com.marinad.service;

import com.marinad.entity.Floor;
import com.marinad.entity.Direction;

public interface FloorService {

    Floor getFloor(int numberFloor);
    Floor nextFloor(Floor floor, Direction direction);
    Floor moveToFloorOnWhichCall(int floorNumberNow, int floorNumberWhichCall, Direction direction);
    int getNumberOfFloors();
}

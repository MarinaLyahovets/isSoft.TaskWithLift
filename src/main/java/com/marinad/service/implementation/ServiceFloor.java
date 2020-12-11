package com.marinad.service.implementation;

import com.google.common.base.Preconditions;
import com.marinad.entity.Elevator;
import com.marinad.entity.Floor;
import com.marinad.entity.KindFloor;
import com.marinad.entity.Direction;
import com.marinad.service.FloorService;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
@Getter
public class ServiceFloor implements FloorService {

    private List<Floor> floorList;

    private ServiceFloor(List<Floor> floorList) {
        log.debug("Service floor is creating");
        this.floorList = floorList;
        log.debug("Service floor created");
    }

    public static ServiceFloor of(List<Floor> floorList){
        checkNotNull(floorList, "Floor list is null!");

        return new ServiceFloor(floorList);
    }

    @Override
    public Floor getFloor(int numberFloor) {
        checkArgument(numberFloor > 0, "Number floor is less than jr equals 0!");

        return floorList.get(numberFloor - 1);
    }

    @Override
    public Floor nextFloor(Floor floor, Direction direction) {
        checkNotNull(floor, "Floor is null!");
        checkNotNull(direction, "Direction is null");

        if (direction == Direction.UP) {
            if (floor.getKindFloor() != KindFloor.LAST) {
                int numberNextFloor = floor.getFloorNumber() + 1;
                log.info("{} now on the {} floor", Thread.currentThread().getName(), floor.getFloorNumber());
                floor = getFloor(numberNextFloor);
                log.info("{} moved to the {} floor",Thread.currentThread().getName(), numberNextFloor);
            }
        } else {
            if (floor.getKindFloor() != KindFloor.FIRST) {
                int numberPreviewFloor = floor.getFloorNumber() - 1;
                log.info("{} now in the {} floor", Thread.currentThread().getName(), floor.getFloorNumber());
                floor = getFloor(numberPreviewFloor);
                log.info("{} moved to the {} floor", Thread.currentThread().getName(),numberPreviewFloor);

            }
        }
        return floor;
    }

    @SneakyThrows
    @Override
    public Floor moveToFloorOnWhichCall(int floorNumberNow, int floorNumberWhichCall, Direction direction){
        checkNotNull(direction, "Direction is null");
        checkArgument(floorNumberNow > 0 && floorNumberWhichCall > 0, "Floor number is less than or equals 0!");

        if((floorNumberNow - floorNumberWhichCall < 0 && direction == Direction.DOWN) ||
                (floorNumberWhichCall - floorNumberNow < 0 && direction == Direction.UP)){

            log.info("{} now on the {} floor", Thread.currentThread().getName(), floorNumberNow);

            TimeUnit.SECONDS.sleep(Math.abs(floorNumberNow - floorNumberWhichCall) * Elevator.LIFT_SPEED);

            log.info("{} moved to {} floor", Thread.currentThread().getName(), floorNumberWhichCall);

            return getFloor(floorNumberWhichCall);
        }

        return getFloor(floorNumberNow);
    }

    @Override
    public int getNumberOfFloors(){
        return floorList.size();
    }

    public List<Floor> getFloorList() {
        return List.copyOf(floorList);
    }
}

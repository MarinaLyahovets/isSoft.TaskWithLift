package com.marinad.service;

import com.marinad.entity.Call;
import com.marinad.entity.Direction;

public interface CallService {

    Call removeCall(int floorNumber, Direction direction);
    void addCall(int floorNumber, Direction direction);
    Call takeCall();

}

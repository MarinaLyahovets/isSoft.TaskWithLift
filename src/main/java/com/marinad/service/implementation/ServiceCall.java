package com.marinad.service.implementation;

import com.marinad.entity.Call;
import com.marinad.entity.Direction;
import com.marinad.service.CallService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class ServiceCall implements CallService {

    private final BlockingQueue<Call> queueCall;
    private final Lock lock = new ReentrantLock(true);


    private ServiceCall(BlockingQueue<Call> queueCall) {
        log.debug("Service call is creating");
        this.queueCall = queueCall;
        log.debug("Service call is created");
    }

    public static ServiceCall of(BlockingQueue<Call> queueCall){
        checkNotNull(queueCall, "Queue call is null!");

        return new ServiceCall(queueCall);
    }

    @Override
    public Call removeCall(int floorNumber, Direction direction){
        checkNotNull(direction, "Direction is null!");
        checkArgument(floorNumber > 0, "Floor number less than 0 or equals 0");

        lock.lock();

        Call call = Call.of(floorNumber, direction);
        boolean isRemoved = queueCall.remove(call);

        lock.unlock();
        if(!isRemoved){
            return null;
        }
        log.info("Remove call: {}", call);

        return call;
    }

    @SneakyThrows
    @Override
    public void addCall(int floorNumber, Direction direction){
        checkArgument( floorNumber >= 0 && floorNumber < 50, "Incorrect floor number!");
        checkNotNull(direction, "state is null!");

        lock.lock();

        Call call = Call.of(floorNumber, direction);

        if(!queueCall.contains(call)) {
            queueCall.put(call);

            log.info("Add call: {}", call);
        }
            lock.unlock();
    }

    @SneakyThrows
    @Override
    public Call takeCall(){

        Call call = queueCall.take();

        log.info("Take call: {}", call);

        return call;
    }


}


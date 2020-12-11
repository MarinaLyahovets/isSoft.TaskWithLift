package com.marinad.service.implementation;

import com.marinad.entity.Call;
import com.marinad.entity.Direction;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

class ServiceCallTest {



    @Test
    void of_successfully() {
        BlockingQueue<Call> blockingQueue = new LinkedBlockingQueue<>();
        ServiceCall.of(blockingQueue);
    }

    @Test
    void of_invalid() {
        assertThrows(NullPointerException.class, ()->{
            ServiceCall.of(null);
        });
    }

    @Test
    void removeCall_successfully() {
        Call call = Call.of(2, Direction.DOWN);
        BlockingQueue<Call> blockingQueue = new LinkedBlockingQueue<>();
        ServiceCall serviceCall = ServiceCall.of(blockingQueue);

        serviceCall.addCall(2, Direction.DOWN);
        serviceCall.addCall(3, Direction.DOWN);
        serviceCall.addCall(3, Direction.UP);


        Call callRemoved = serviceCall.removeCall(2, Direction.DOWN);

        assertEquals(callRemoved, call);
        assertThat(blockingQueue.size(), is(2));
    }

    @Test
    void removeCall_invalidFloorNumber() {
        BlockingQueue<Call> blockingQueue = new LinkedBlockingQueue<>();
        ServiceCall serviceCall = ServiceCall.of(blockingQueue);

        assertThrows(IllegalArgumentException.class, ()->{
            serviceCall.removeCall(0, Direction.DOWN);
        });
    }

    @Test
    void removeCall_invalidDirection() {
        BlockingQueue<Call> blockingQueue = new LinkedBlockingQueue<>();
        ServiceCall serviceCall = ServiceCall.of(blockingQueue);

        assertThrows(NullPointerException.class, ()->{
            serviceCall.removeCall(2, null);
        });
    }

    @Test
    void addCall_successfully() {
        BlockingQueue<Call> queueCall = new LinkedBlockingQueue<>();
        ServiceCall serviceCall = ServiceCall.of(queueCall);

        serviceCall.addCall(2, Direction.DOWN);
        serviceCall.addCall(3, Direction.DOWN);

        assertThat(queueCall.size(), is(2));
    }

    @Test
    void addCall_invalidFloorNumber() {
        BlockingQueue<Call> queueCall = new LinkedBlockingQueue<>();
        ServiceCall serviceCall = ServiceCall.of(queueCall);

        assertThrows(IllegalArgumentException.class, ()->{
            serviceCall.addCall(0, Direction.DOWN);
        });

    }

    @Test
    void addCall_invalidNullDirection() {
        BlockingQueue<Call> queueCall = new LinkedBlockingQueue<>();
        ServiceCall serviceCall = ServiceCall.of(queueCall);

        assertThrows(NullPointerException.class, ()->{
            serviceCall.addCall(2, null);
        });
    }

    @Test
    void takeCall() {
        Call call = Call.of(2, Direction.DOWN);
        BlockingQueue<Call> queueCall = new LinkedBlockingQueue<>();
        ServiceCall serviceCall = ServiceCall.of(queueCall);

        serviceCall.addCall(2, Direction.DOWN);
        serviceCall.addCall(3, Direction.DOWN);

        assertEquals(serviceCall.takeCall(), call);
    }

}
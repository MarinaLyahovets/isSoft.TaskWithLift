package com.marinad.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
@Getter
public class Call {
    private final int number;
    private final Direction direction;

    private Call(int number, Direction direction) {
        log.debug("Call is creating");

        this.number = number;
        this.direction = direction;

        log.debug("Call created");
    }

    public static Call of(int number, Direction direction){
        checkArgument(number > 0, "Number is less than or equals 0!");
        checkNotNull(direction, "Direction is null!");

        return new Call(number, direction);
    }

    @Override
    public String toString() {
        return new StringBuilder("Call {").
                append("number: ").append(number).append(", ")
                .append("state: ").append(direction)
                .append("}").toString();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Call call = (Call) o;
        return number == call.number &&
                direction == call.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, direction);
    }
}

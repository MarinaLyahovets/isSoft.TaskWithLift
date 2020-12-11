package com.marinad.statistics;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.html.HTMLDocument;
import java.util.Objects;

@Slf4j
@Getter
public class ServiceStatistics {

    private final int numberFloorTo;
    private final int numberFloorFrom;

    private ServiceStatistics(int numberFloorFrom, int numberFloorTo) {

        this.numberFloorFrom = numberFloorFrom;
        this.numberFloorTo = numberFloorTo;
    }

    public static ServiceStatistics of(int numberFloorFrom, int numberFloorTo){
        Preconditions.checkArgument(numberFloorFrom > 0, "Number floor from less than or equals 0");
        Preconditions.checkArgument(numberFloorTo > 0, "Number floor to less than or equals 0");

        return new ServiceStatistics(numberFloorFrom, numberFloorTo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceStatistics that = (ServiceStatistics) o;
        return numberFloorTo == that.numberFloorTo &&
                numberFloorFrom == that.numberFloorFrom;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberFloorTo, numberFloorFrom);
    }

    @Override
    public String toString() {
        return new StringBuilder("ServiceStatistics {")
                .append("number floor from: ").append(numberFloorFrom).append(",  ")
                .append("number floor to: ").append(numberFloorTo)
                .append("}").toString();

    }
}

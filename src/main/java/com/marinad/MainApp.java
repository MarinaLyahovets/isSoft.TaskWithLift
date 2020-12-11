package com.marinad;

import com.marinad.entity.Building;
import lombok.SneakyThrows;

public class MainApp {

    @SneakyThrows
    public static void main(String[] args) {

        Building building = Building.of( 7, 3);

        building.start();
    }

}

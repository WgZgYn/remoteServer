package org.scu301.remoteserver.dto;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.scu301.remoteserver.entity.House;

public record HouseInfo(Integer house_id, String house_name) {
    @Contract("_ -> new")
    public static @NotNull HouseInfo of(House house) {
        return new HouseInfo(house.getId(), house.getHouseName());
    }
}

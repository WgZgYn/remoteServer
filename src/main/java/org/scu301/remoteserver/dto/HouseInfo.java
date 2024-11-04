package org.scu301.remoteserver.dto;

import org.scu301.remoteserver.entity.House;

public record HouseInfo(Integer house_id, String house_name) {
    public static HouseInfo of(House house) {
        return new HouseInfo(house.getId(), house.getHouseName());
    }
}

package org.scu301.remoteserver.dto;

import org.scu301.remoteserver.entity.House;

import java.util.List;

public record HouseDevices(int house_id,
                           String house_name,
                           List<AreaDevices> areas_devices) {
    public static HouseDevices of(House house) {
        return new HouseDevices(
                house.getId(),
                house.getHouseName(),
                house.getAreas()
                        .stream()
                        .map(AreaDevices::of)
                        .toList());
    }
}
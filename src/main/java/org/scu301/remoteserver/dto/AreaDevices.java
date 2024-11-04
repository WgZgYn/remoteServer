package org.scu301.remoteserver.dto;

import java.util.List;

import org.scu301.remoteserver.entity.Area;

public record AreaDevices(int area_id, String area_name, List<DeviceInfo> devices) {
    public static AreaDevices of(Area area) {
        return new AreaDevices(
                area.getId(),
                area.getAreaName(),
                area.getDevices()
                        .stream()
                        .map(DeviceInfo::of)
                        .toList()
        );
    }
}
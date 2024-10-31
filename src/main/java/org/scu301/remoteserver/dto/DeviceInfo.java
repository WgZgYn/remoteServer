package org.scu301.remoteserver.dto;

import org.scu301.remoteserver.entity.Device;
import org.scu301.remoteserver.entity.DeviceControl;
import org.scu301.remoteserver.entity.DeviceType;

import java.util.List;
import java.util.Map;

public record DeviceInfo(int device_id,
                         String device_name,
                         String efuse_mac,
                         String chip_model,
                         DeviceType device_type,
                         List<Map<String, Object>> service) {
    public static DeviceInfo of(Device device) {
        return new DeviceInfo(
                device.getId(),
                device.getDeviceName(),
                device.getEfuseMac(),
                device.getChipModel(),
                device.getType(),
                device.getDeviceControls().stream().map(DeviceControl::getParameter).toList()
        );
    }
}
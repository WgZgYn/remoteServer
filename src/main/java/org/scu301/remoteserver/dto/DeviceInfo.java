package org.scu301.remoteserver.dto;

import org.scu301.remoteserver.entity.DeviceType;

import java.util.List;
import java.util.Map;

public record DeviceInfo(Integer device_id,
                         String device_name,
                         String efuse_mac,
                         String model_name,
                         DeviceType device_type,
                         List<Map<String, Object>> service) {
}
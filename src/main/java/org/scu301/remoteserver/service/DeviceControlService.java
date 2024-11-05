package org.scu301.remoteserver.service;


import lombok.extern.slf4j.Slf4j;
import org.scu301.remoteserver.entity.Device;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class DeviceControlService {
    DataBaseReadService dbReadService;
    MqttClientService mqttClientService;

    DeviceControlService(MqttClientService mqttClientService, DataBaseReadService dbReadService) {
        this.mqttClientService = mqttClientService;
        this.dbReadService = dbReadService;
    }

    public boolean executeService(int deviceId, String serviceName) {
        Optional<Device> deviceOptional = dbReadService.getDevice(deviceId);
        if (deviceOptional.isEmpty()) return false;
        Device device = deviceOptional.get();
        try {
            // 首先订阅事件
            mqttClientService.subscribe("/device/" + device.getEfuseMac() + "/events");
            // 发送控制命令
            mqttClientService.publish("/device/" + device.getEfuseMac() + "/service", serviceName);
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }
}

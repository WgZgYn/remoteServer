package org.scu301.remoteserver.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.scu301.remoteserver.entity.Device;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class DeviceControlService {
    private final ObjectMapper jacksonObjectMapper;
    DataBaseReadService dbReadService;
    MqttClientService mqttClientService;

    DeviceControlService(MqttClientService mqttClientService, DataBaseReadService dbReadService, ObjectMapper jacksonObjectMapper) {
        this.mqttClientService = mqttClientService;
        this.dbReadService = dbReadService;
        this.jacksonObjectMapper = jacksonObjectMapper;
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

    // payload对应parameter中的value类型
    public boolean executeService(int deviceId, String serviceName, Object payload) {
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

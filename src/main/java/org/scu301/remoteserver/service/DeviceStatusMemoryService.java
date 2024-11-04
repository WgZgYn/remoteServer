package org.scu301.remoteserver.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.jetbrains.annotations.NotNull;
import org.scu301.remoteserver.entity.Device;
import org.scu301.remoteserver.event.events.DeviceMqttMessage;
import org.scu301.remoteserver.repository.DeviceRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class DeviceStatusMemoryService {
    private final Map<Integer, JsonNode> deviceInnerStatus;
    private final Map<Integer, Deque<String>> eventDeque;
    private Map<String, Integer> deviceMac2Id;
    private final DeviceRepository deviceRepository;
    private final MqttClientService mqttClientService;


    DeviceStatusMemoryService(DeviceRepository deviceRepository, MqttClientService mqttClientService) {
        deviceInnerStatus = new HashMap<>();
        eventDeque = new HashMap<>();
        deviceMac2Id = new HashMap<>();
        this.deviceRepository = deviceRepository;
        this.mqttClientService = mqttClientService;
    }

    public boolean isOnline(int deviceId) {
        return deviceMac2Id.containsValue(deviceId);
    }

    public void saveStatus(int deviceId, JsonNode deviceStatus) {
        deviceInnerStatus.put(deviceId, deviceStatus);
    }

    public Optional<JsonNode> getStatus(int deviceId) {
        return Optional.ofNullable(deviceInnerStatus.get(deviceId));
    }

    private void recordIdMac(int deviceId, String mac) {
        log.info("Device online: {}", deviceId);
        deviceMac2Id.put(mac, deviceId);
    }

    private void recordEvent(int deviceId, String event) {
        eventDeque.computeIfAbsent(deviceId, key -> new LinkedList<>()).add(event);
    }

    @EventListener
    public void handleDeviceMqttMessage(@NotNull DeviceMqttMessage event) {
        int deviceId;
        if (deviceMac2Id.containsKey(event.getEFuseMac())) {
            deviceId = deviceMac2Id.get(event.getEFuseMac());
        } else {
            Optional<Device> deviceOptional = deviceRepository.findDeviceByEfuseMac(event.getEFuseMac());
            if (deviceOptional.isEmpty()) {
                log.info("No device found with EfuseMac: {}", event.getEFuseMac());
                return;
            }
            Device device = deviceOptional.get();
            deviceId = device.getId();
        }
        recordIdMac(deviceId, event.getEFuseMac());
        switch (event.getType()) {
            case "status" -> saveStatus(deviceId, event.getPayload());
            case "event" -> recordEvent(deviceId, event.getPayload().asText());
        }
    }


    @Async
    @Scheduled(fixedRate = 50000)
    protected void updateStatus() {
        log.info("start schedule to fetch device's status");
        Map<String, Integer> temp = deviceMac2Id;
        deviceMac2Id = new HashMap<>();
        for (Map.Entry<String, Integer> entry : temp.entrySet()) {
            log.info("update device status: {} {}", entry.getValue(), entry.getKey());
            try {
                mqttClientService.publish("/device/" + entry.getKey() + "/service", "status");
            } catch (MqttException e) {
                log.error(e.getMessage());
            }
        }
    }
}

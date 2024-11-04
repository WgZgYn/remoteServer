package org.scu301.remoteserver.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.scu301.remoteserver.entity.Device;
import org.scu301.remoteserver.event.events.DeviceMqttMessage;
import org.scu301.remoteserver.repository.DeviceRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class DeviceStatusMemoryService {
    private final Map<Integer, JsonNode> deviceInnerStatus;
    private final Map<Integer, Deque<String>> eventDeque;
    private Map<Integer, String> deviceId2Mac;


    private final DeviceRepository deviceRepository;
    private final MqttClientService mqttClientService;


    DeviceStatusMemoryService(DeviceRepository deviceRepository, MqttClientService mqttClientService) {
        deviceInnerStatus = new HashMap<>();
        eventDeque = new HashMap<>();
        deviceId2Mac = new HashMap<>();
        this.deviceRepository = deviceRepository;
        this.mqttClientService = mqttClientService;
    }

    public boolean isOnline(int deviceId) {
        return deviceId2Mac.containsKey(deviceId);
    }

    public void saveStatus(int deviceId, JsonNode deviceStatus) {
        deviceInnerStatus.put(deviceId, deviceStatus);
    }

    public Optional<JsonNode> getStatus(int deviceId) {
        return Optional.ofNullable(deviceInnerStatus.get(deviceId));
    }

    private void recordIdMac(int deviceId, String mac) {
        log.info("Device online: {}", deviceId);
        deviceId2Mac.put(deviceId, mac);
    }

    private void recordEvent(int deviceId, String event) {
        eventDeque.getOrDefault(deviceId, new LinkedList<>()).add(event);
    }



    public void handleDeviceMqttMessage(DeviceMqttMessage event) {
        // TODO: need to be cached
        Optional<Device> deviceOptional = deviceRepository.findDeviceByEfuseMac(event.getEFuseMac());
        if (deviceOptional.isEmpty()) {
            log.info("No device found with EfuseMac: {}", event.getEFuseMac());
            return;
        }

        Device device = deviceOptional.get();
        recordIdMac(device.getId(), event.getEFuseMac());

        switch (event.getType()) {
            case "status" -> saveStatus(device.getId(), event.getPayload());
            case "event" -> recordEvent(device.getId(), event.getPayload().asText());
        }
    }


    @Async
    @Scheduled(fixedRate = 50000)
    protected void updateStatus() {
        log.info("start schedule to fetch device's status");
        Map<Integer, String> temp = deviceId2Mac;
        deviceId2Mac = new HashMap<>();
        for (Map.Entry<Integer, String> entry : temp.entrySet()) {
            log.info("update device status: {}", entry.getKey());
            try {
                mqttClientService.publish("/device/" + entry.getValue() + "/service", "status");
            } catch (MqttException e) {
                log.error(e.getMessage());
            }
        }
    }
}

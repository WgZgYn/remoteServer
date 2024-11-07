package org.scu301.remoteserver.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.scu301.remoteserver.dto.mqtt.HostMessage;
import org.scu301.remoteserver.dto.mqtt.DeviceMessage;
import org.springframework.context.ApplicationEventPublisher;
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
    private final MqttClientService mqttClientService;
    private final DataBaseReadService dbReadService;
    private final MemoryCacheService memoryCacheService;
    ApplicationEventPublisher applicationEventPublisher;

    DeviceStatusMemoryService(MqttClientService mqttClientService, DataBaseReadService dbReadService, ApplicationEventPublisher applicationEventPublisher, MemoryCacheService memoryCacheService) {
        this.deviceInnerStatus = new HashMap<>();
        this.eventDeque = new HashMap<>();
        this.mqttClientService = mqttClientService;
        this.dbReadService = dbReadService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.memoryCacheService = memoryCacheService;
    }

    public boolean isOnline(int deviceId) {
        return deviceInnerStatus.containsKey(deviceId);
    }

    public void saveStatus(int deviceId, JsonNode deviceStatus) {
        deviceInnerStatus.put(deviceId, deviceStatus);
    }

    public Optional<JsonNode> getStatus(int deviceId) {
        return Optional.ofNullable(deviceInnerStatus.get(deviceId));
    }

    private void recordIdMac(int deviceId, String mac) {
        log.info("Device online: {}", deviceId);

        // TODO:
    }

    private void recordEvent(int deviceId, String event) {
        eventDeque.computeIfAbsent(deviceId, key -> new LinkedList<>()).add(event);
    }

    @EventListener
    public void handleDeviceMqttMessage(@NotNull DeviceMessage event) {
        // TODO: add timestamp to the status

//        int deviceId;
//        if (deviceMac2Id.containsKey(event.getEFuseMac())) {
//            deviceId = deviceMac2Id.get(event.getEFuseMac());
//        } else {
//            Optional<Device> deviceOptional = dbReadService.getDeviceByMac(event.getEFuseMac());
//            if (deviceOptional.isEmpty()) {
//                log.info("No device found with EfuseMac: {}", event.getEFuseMac());
//                return;
//            }
//            Device device = deviceOptional.get();
//            deviceId = device.getId();
//        }
//        recordIdMac(deviceId, event.getEFuseMac());
//        switch (event.getType()) {
//            case "status" -> {
//                saveStatus(deviceId, event.getPayload());
//                Set<Integer> accountIds = memoryCacheService.getDeviceIdToAccountId(deviceId);
//                applicationEventPublisher.publishEvent(new DeviceStatusUpdateEvent(accountIds, deviceId));
//            }
//            case "event" -> recordEvent(deviceId, event.getPayload().asText());
//        }
    }


    @Async
    @Scheduled(fixedRate = 50000)
    protected void updateStatus() {
        try {
            deviceInnerStatus.clear();
            log.info("start schedule to fetch device's status");
            mqttClientService.publish("/device", HostMessage.empty("status"), 1, false);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

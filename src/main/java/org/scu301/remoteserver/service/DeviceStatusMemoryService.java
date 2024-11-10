package org.scu301.remoteserver.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.scu301.remoteserver.dto.mqtt.HostMessage;
import org.scu301.remoteserver.dto.mqtt.DeviceMessage;
import org.scu301.remoteserver.event.events.DeviceStatusUpdateEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class DeviceStatusMemoryService {
    private Map<Integer, JsonNode> lastDeviceInnerStatus;
    private Map<Integer, JsonNode> currDeviceInnerStatus;
    private final Map<Integer, Deque<String>> eventDeque;


    private final MqttClientService mqttClientService;
    private final MemoryCacheService memoryCacheService;
    ApplicationEventPublisher applicationEventPublisher;

    DeviceStatusMemoryService(
            MqttClientService mqttClientService,
            ApplicationEventPublisher applicationEventPublisher,
            MemoryCacheService memoryCacheService)
    {
        this.lastDeviceInnerStatus = new HashMap<>();
        this.eventDeque = new HashMap<>();
        this.mqttClientService = mqttClientService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.memoryCacheService = memoryCacheService;
        this.currDeviceInnerStatus = new HashMap<>();
    }
    public boolean isOnline(int deviceId) {
        return lastDeviceInnerStatus.containsKey(deviceId);
    }
    public Optional<JsonNode> getStatus(int deviceId) {
        return Optional.ofNullable(lastDeviceInnerStatus.get(deviceId));
    }
    public void saveStatus(int deviceId, JsonNode deviceStatus) {
        currDeviceInnerStatus.put(deviceId, deviceStatus);
    }
    private void saveEvent(int deviceId, String event) {
        eventDeque.computeIfAbsent(deviceId, key -> new LinkedList<>()).add(event);
    }

    @EventListener
    public void handleDeviceMqttMessage(@NotNull DeviceMessage event) {
        log.info("Received device mqtt message: {}", event);
        // TODO: add timestamp to the status
        String mac = event.getEFuseMac();
        Optional<Integer> deviceId = memoryCacheService.getDeviceId(mac);
        if (deviceId.isEmpty()) {
            log.info("No device found for mac {}", mac);
            return;
        }
        log.info("Device found for mac {}", mac);
        int id = deviceId.get();
        switch (event.getType()) {
            case "status" -> {
                saveStatus(id, event.getPayload());
                Set<Integer> accountIds = memoryCacheService.getAccountIds(id);
                applicationEventPublisher.publishEvent(new DeviceStatusUpdateEvent(accountIds, id));
            }
            case "event" -> saveEvent(id, event.getPayload().asText());
        }
    }


    @Async
    @Scheduled(fixedRate = 50000)
    protected void updateStatus() {
        try {
            Map<Integer, JsonNode> temp = lastDeviceInnerStatus;
            lastDeviceInnerStatus = currDeviceInnerStatus;
            currDeviceInnerStatus = temp;
            currDeviceInnerStatus.clear();
            log.info("start schedule to fetch device's status");
            mqttClientService.publish("/device", HostMessage.empty("status"), 1, false);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

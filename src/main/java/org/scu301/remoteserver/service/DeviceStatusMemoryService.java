package org.scu301.remoteserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class DeviceStatusMemoryService {
    private final ObjectMapper objectMapper;
    private final Map<Integer, Boolean> deviceOnlineStatus;
    private final Map<Integer, JsonNode> deviceInnerStatus;


    DeviceStatusMemoryService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        deviceInnerStatus = new HashMap<>();
        deviceOnlineStatus = new HashMap<>();
    }

    public boolean saveStatus(int deviceId, String status) {
        try {
            JsonNode node = objectMapper.readTree(status);
            deviceInnerStatus.put(deviceId, node);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    public Optional<JsonNode> getStatus(int deviceId) {
        return Optional.ofNullable(deviceInnerStatus.get(deviceId));
    }
}

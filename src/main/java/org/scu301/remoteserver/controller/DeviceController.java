package org.scu301.remoteserver.controller;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.scu301.remoteserver.security.Claims;
import org.scu301.remoteserver.service.DeviceControlService;
import org.scu301.remoteserver.service.DeviceStatusMemoryService;
import org.scu301.remoteserver.service.MemoryCacheService;
import org.scu301.remoteserver.util.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/my/device")
public class DeviceController {
    private final DeviceStatusMemoryService deviceStatusMemoryService;
    private final MemoryCacheService memoryCacheService;
    private final DeviceControlService deviceControlService;

    DeviceController(DeviceControlService deviceControlService, DeviceStatusMemoryService deviceStatusMemoryService, MemoryCacheService memoryCacheService) {
        this.deviceControlService = deviceControlService;
        this.deviceStatusMemoryService = deviceStatusMemoryService;
        this.memoryCacheService = memoryCacheService;
    }

    // TODO:
    // make the device_service run Post method
    // judge whether the device is in charge of the account use the cache
    //


    @GetMapping("/{deviceId}/{serviceName}")
    Result executeService(@RequestAttribute("claims") Claims claims, @PathVariable int deviceId, @PathVariable String serviceName) {
        int accountId = claims.id();
        memoryCacheService.isDeviceInChargeOfAccount(deviceId, accountId);
        return Result.of(deviceControlService.executeService(deviceId, serviceName), "operation failed");
    }


    @PostMapping( value = "/{deviceId}/{serviceName}", consumes = "text/plain")
    Result executeServiceV2(@RequestAttribute("claims") Claims claims,
                            @PathVariable int deviceId,
                            @PathVariable String serviceName,
                            @RequestBody String plainText) {
        log.info("plainText");
        int accountId = claims.id();
        memoryCacheService.isDeviceInChargeOfAccount(deviceId, accountId);
        return Result.of(deviceControlService.executeService(deviceId, serviceName), "operation failed");
    }


    @PostMapping(value = "/{deviceId}/{serviceName}", consumes = "application/json")
    Result executeServiceV2(@RequestAttribute("claims") Claims claims,
                            @PathVariable int deviceId,
                            @PathVariable String serviceName,
                            @RequestBody JsonNode json) {
        int accountId = claims.id();
        memoryCacheService.isDeviceInChargeOfAccount(deviceId, accountId);
        return Result.of(deviceControlService.executeService(deviceId, serviceName), "operation failed");
    }


    @PostMapping( value = "/{deviceId}/{serviceName}", consumes = "multipart/form-data")
    Result executeServiceV2(@RequestAttribute("claims") Claims claims,
                            @PathVariable int deviceId,
                            @PathVariable String serviceName,
                            @RequestBody MultipartFile file) {
        int accountId = claims.id();
        memoryCacheService.isDeviceInChargeOfAccount(deviceId, accountId);
        return Result.of(deviceControlService.executeService(deviceId, serviceName), "operation failed");
    }

    @GetMapping("/{deviceId}")
    Result deviceStatus(@RequestAttribute("claims") Claims claims, @PathVariable int deviceId) {
        int accountId = claims.id();
        memoryCacheService.isDeviceInChargeOfAccount(deviceId, accountId);
        return Result.of(deviceStatusMemoryService.getStatus(deviceId));
    }
}

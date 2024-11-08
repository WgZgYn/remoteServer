package org.scu301.remoteserver.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.scu301.remoteserver.dto.mqtt.HostMessage;
import org.scu301.remoteserver.security.Claims;
import org.scu301.remoteserver.service.DeviceControlService;
import org.scu301.remoteserver.service.DeviceDataService;
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
    private final DeviceDataService deviceDataService;

    DeviceController(DeviceControlService deviceControlService,
                     DeviceStatusMemoryService deviceStatusMemoryService,
                     MemoryCacheService memoryCacheService,
                     DeviceDataService deviceDataService) {
        this.deviceControlService = deviceControlService;
        this.deviceStatusMemoryService = deviceStatusMemoryService;
        this.memoryCacheService = memoryCacheService;
        this.deviceDataService = deviceDataService;
    }

    @GetMapping("")
    Result getAccountDevices(@RequestAttribute("claims") Claims claims) {
        log.info("get device");
        return Result.of(deviceDataService.getAccountDevices(claims.id()), "no such (account)");
    }

    // TODO: replace it to deviceInfo
    @GetMapping("/{deviceId}")
    Result deviceStatus(@RequestAttribute("claims") Claims claims, @PathVariable int deviceId) {
        int accountId = claims.id();
        memoryCacheService.isDeviceInChargeOfAccount(deviceId, accountId);
        return Result.of(deviceStatusMemoryService.getStatus(deviceId));
    }

    // This status is the last status, device report to the server.
    // if you want to have the latest device-status, using the sse's or other async event to get
    @GetMapping("/{deviceId}/status")
    Result deviceStatus2(@RequestAttribute("claims") Claims claims, @PathVariable int deviceId) {
        int accountId = claims.id();
        memoryCacheService.isDeviceInChargeOfAccount(deviceId, accountId);
        return Result.of(deviceStatusMemoryService.getStatus(deviceId));
    }

    @RestController
    @RequestMapping("/api/my/device")
    class DeviceServiceController {
        @GetMapping(value = "/{deviceId}/service/{serviceName}")
        Result executeServiceV2(@RequestAttribute("claims") Claims claims,
                                @PathVariable int deviceId,
                                @PathVariable String serviceName) {
            log.info("get");
            int accountId = claims.id();
            if (!memoryCacheService.isDeviceInChargeOfAccount(deviceId, accountId))
                return Result.err("the device is not in charge of account");

            return Result.of(() -> deviceControlService.executeService(deviceId, HostMessage.empty(serviceName)));
        }

        @PostMapping(value = "/{deviceId}/service/{serviceName}", consumes = "text/plain")
        Result executeServiceV2(@RequestAttribute("claims") Claims claims,
                                @PathVariable int deviceId,
                                @PathVariable String serviceName,
                                @RequestBody String plainText) {
            log.info("plainText");
            int accountId = claims.id();
            if (!memoryCacheService.isDeviceInChargeOfAccount(deviceId, accountId))
                return Result.err("the device is not in charge of account");

            return Result.of(() -> deviceControlService.executeService(deviceId, HostMessage.fromText(serviceName, plainText)));
        }


        @PostMapping(value = "/{deviceId}/service/{serviceName}", consumes = "application/json")
        Result executeServiceV2(@RequestAttribute("claims") Claims claims,
                                @PathVariable int deviceId,
                                @PathVariable String serviceName,
                                @RequestBody JsonNode json) {
            int accountId = claims.id();
            if (!memoryCacheService.isDeviceInChargeOfAccount(deviceId, accountId))
                return Result.err("the device is not in charge of account");

            return Result.of(() -> deviceControlService.executeService(deviceId, HostMessage.fromJson(serviceName, json)));
        }


        @PostMapping(value = "/{deviceId}/service/{serviceName}", consumes = "multipart/form-data")
        Result executeServiceV2(@RequestAttribute("claims") Claims claims,
                                @PathVariable int deviceId,
                                @PathVariable String serviceName,
                                @RequestBody MultipartFile file) {
            int accountId = claims.id();
            if (!memoryCacheService.isDeviceInChargeOfAccount(deviceId, accountId))
                return Result.err("the device is not in charge of account");

            return Result.of(() -> deviceControlService.executeService(deviceId, HostMessage.fromFile(serviceName, file)));
        }
    }
}

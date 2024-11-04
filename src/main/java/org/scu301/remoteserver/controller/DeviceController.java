package org.scu301.remoteserver.controller;

import org.scu301.remoteserver.security.Claims;
import org.scu301.remoteserver.service.DeviceControlService;
import org.scu301.remoteserver.service.DeviceStatusMemoryService;
import org.scu301.remoteserver.util.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/my/device")
public class DeviceController {
    private final DeviceStatusMemoryService deviceStatusMemoryService;
    DeviceControlService deviceControlService;

    DeviceController(DeviceControlService deviceControlService, DeviceStatusMemoryService deviceStatusMemoryService) {
        this.deviceControlService = deviceControlService;
        this.deviceStatusMemoryService = deviceStatusMemoryService;
    }


    // GET http://47.108.27.238/api/my/device/4/open
    // GET http://47.108.27.238/api/my/device/4/close
    // GET http://47.108.27.238/api/my/device/4/switch
    // GET http://47.108.27.238/api/my/device/4/light=false
    // GET http://47.108.27.238/api/my/device/4/light=true
    // GET http://47.108.27.238/api/my/device/4/light=0
    // GET http://47.108.27.238/api/my/device/4/light=100
    // GET http://47.108.27.238/api/my/device/4/light=50
    @GetMapping("/{deviceId}/{serviceName}")
    Result executeService(@RequestAttribute("claims") Claims claims, @PathVariable int deviceId, @PathVariable String serviceName) {
        int accountId = claims.id();
        // TODO:
        return Result.of(deviceControlService.executeService(deviceId, serviceName), "operation failed");
    }

    @GetMapping("/{deviceId}")
    Result deviceStatus(@RequestAttribute("claims") Claims claims, @PathVariable int deviceId) {
        int accountId = claims.id();
        // TODO:
        return Result.of(deviceStatusMemoryService.getStatus(deviceId));
    }
}

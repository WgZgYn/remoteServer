package org.scu301.remoteserver.controller;

import org.scu301.remoteserver.security.Claims;
import org.scu301.remoteserver.service.DeviceControlService;
import org.scu301.remoteserver.util.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/my/device")
public class DeviceController {
    DeviceControlService deviceControlService;

    DeviceController(DeviceControlService deviceControlService) {
        this.deviceControlService = deviceControlService;
    }

    @GetMapping("/{deviceId}/{serviceName}")
    Result executeService(@RequestAttribute("claims") Claims claims, @PathVariable int deviceId, @PathVariable String serviceName) {
        int accountId = claims.id();

        // TODO:

        boolean ok = deviceControlService.executeService(deviceId, serviceName);
        if (ok) return Result.ok();
        return Result.err("operation failed");
    }
}

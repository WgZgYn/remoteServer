package org.scu301.remoteserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.scu301.remoteserver.dto.http.AddAreaRequest;
import org.scu301.remoteserver.security.Claims;
import org.scu301.remoteserver.service.DeviceDataService;
import org.scu301.remoteserver.service.HouseAreaService;
import org.scu301.remoteserver.util.Response;
import org.scu301.remoteserver.util.Result;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/my/area")
public class AreaController {
    private final HouseAreaService houseAreaService;
    private final DeviceDataService deviceDataService;

    public AreaController(HouseAreaService houseAreaService, DeviceDataService deviceDataService) {
        this.houseAreaService = houseAreaService;
        this.deviceDataService = deviceDataService;
    }

    @GetMapping("")
    Result listArea(@RequestAttribute("claims") Claims claims) {
        return Response.of(houseAreaService.getAreasInfoByAccountId(claims.id()));
    }

    @PostMapping("")
    Result addArea(@RequestAttribute("claims") Claims claims, @RequestBody AddAreaRequest request) {
        return Result.of(houseAreaService.addArea(claims.id(), request.house_id(), request.area_name()), "add area error");
    }

    @GetMapping("/{id}")
    Result getAreaDevices(@RequestAttribute("claims") Claims claims, @PathVariable Integer id) {
        return Result.of(deviceDataService.getAreaDevices(claims.id(), id), "no such (account, area)");
    }
}

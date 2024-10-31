package org.scu301.remoteserver.controller;

import io.jsonwebtoken.JwtException;
import org.scu301.remoteserver.entity.House;
import org.scu301.remoteserver.security.Claims;
import org.scu301.remoteserver.service.DeviceService;
import org.scu301.remoteserver.service.HouseAreaService;
import org.scu301.remoteserver.util.Response;
import org.scu301.remoteserver.util.Result;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/my")
public class DeviceDataController {
    DeviceService deviceService;
    HouseAreaService houseAreaService;

    DeviceDataController(DeviceService deviceService, HouseAreaService houseAreaService) {
        this.deviceService = deviceService;
        this.houseAreaService = houseAreaService;
    }

    @GetMapping("/device")
    Result getHousesDevice(@RequestAttribute("claims") Claims claims) {
        try {
            return Response.ok(deviceService.getHousesDevices(claims.id()));
        } catch (JwtException e) {
            return Response.err(e.getMessage());
        }
    }

    @GetMapping("/area")
    Result listAreas(@RequestAttribute("claims") Claims claims) {
        return Response.ok(houseAreaService.getHouses(claims.id()).stream().map(House::getAreas));
    }

    @GetMapping("/area/{id}")
    Result listAreaDevices(@RequestAttribute("claims") Claims claims, @PathVariable String id) {
        return Response.ok(houseAreaService.getHouses(claims.id()).stream());
    }

    @GetMapping("/house")
    Result listHouses(@RequestAttribute("claims") Claims claims) {
        return Response.ok();
    }

    @GetMapping("/house/{id}")
    Result listHouseDevices(@RequestAttribute("claims") Claims claims, @PathVariable String id) {
        return Response.ok();
    }

    @GetMapping("/member")
    Result listHouseMember(@RequestAttribute("claims") Claims claims) {
        return Response.ok();
    }
}

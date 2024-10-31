package org.scu301.remoteserver.controller;

import io.jsonwebtoken.JwtException;
import org.scu301.remoteserver.entity.House;
import org.scu301.remoteserver.security.Claims;
import org.scu301.remoteserver.security.JwtUtils;
import org.scu301.remoteserver.service.DeviceService;
import org.scu301.remoteserver.service.HouseAreaService;
import org.scu301.remoteserver.util.Response;
import org.scu301.remoteserver.util.Result;
import org.springframework.web.bind.annotation.*;

/*
TODO: 自定义拦截器，简化代码重复
 */

@RestController
@RequestMapping("/api/my")
public class DeviceController {
    DeviceService deviceService;
    HouseAreaService houseAreaService;

    DeviceController(DeviceService deviceService, HouseAreaService houseAreaService) {
        this.deviceService = deviceService;
        this.houseAreaService = houseAreaService;
    }

    @GetMapping("/device")
    Result getHousesDevice(@RequestAttribute("claims") Claims claims) {
        try {
            return Response.ok(deviceService.getDevices(claims.id()));
        } catch (JwtException e) {
            return Response.err(e.getMessage());
        }
    }

    @GetMapping("/area")
    Result listAreas(@RequestAttribute("claims") Claims claims) {
        return Response.ok(houseAreaService.getHouses(claims.id()).stream().map(House::getAreas));
    }

    @GetMapping("/area/{id}")
    Result getAreaDevices(@RequestAttribute("claims") Claims claims, @PathVariable String id) {
        return Response.ok();
    }

    @GetMapping("/house")
    Result listHouses(@RequestHeader(value = "Authorization") String authHeader) {
        return Response.ok();
    }

    @GetMapping("/house/{id}")
    Result getHouseDevices(@RequestHeader(value = "Authorization") String authHeader, @PathVariable String id) {
        return Response.ok();
    }


    @GetMapping("/house/{house_id}/area/{area_id}")
    Result house(@RequestHeader(value = "Authorization") String authHeader, @PathVariable String house_id, @PathVariable String area_id) {
        return Response.ok();
    }

    @GetMapping("/family")
    Result family(@RequestHeader(value = "Authorization") String authHeader) {
        return Response.ok();
    }
}

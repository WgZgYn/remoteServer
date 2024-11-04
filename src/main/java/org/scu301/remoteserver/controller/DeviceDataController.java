package org.scu301.remoteserver.controller;

import org.scu301.remoteserver.dto.AccountDevices;
import org.scu301.remoteserver.entity.House;
import org.scu301.remoteserver.security.Claims;
import org.scu301.remoteserver.service.DeviceDataService;
import org.scu301.remoteserver.service.HouseAreaService;
import org.scu301.remoteserver.util.Response;
import org.scu301.remoteserver.util.Result;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/my")
public class DeviceDataController {
    DeviceDataService deviceService;
    HouseAreaService houseAreaService;

    DeviceDataController(DeviceDataService deviceService, HouseAreaService houseAreaService) {
        this.deviceService = deviceService;
        this.houseAreaService = houseAreaService;
    }

    @GetMapping("/device")
    Response<AccountDevices> getHousesDevice(@RequestAttribute("claims") Claims claims) {
        Optional<AccountDevices> ok = deviceService.getHousesDevices(claims.id());
        return ok.map(Response::ok).orElseGet(() -> Response.ok(AccountDevices.none()));
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

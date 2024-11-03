package org.scu301.remoteserver.controller;

import java.util.Optional;

import org.scu301.remoteserver.dto.AccountDevices;
import org.scu301.remoteserver.dto.AreaDevices;
import org.scu301.remoteserver.dto.HouseDevices;
import org.scu301.remoteserver.entity.House;
import org.scu301.remoteserver.security.Claims;
import org.scu301.remoteserver.service.DeviceDataService;
import org.scu301.remoteserver.service.HouseAreaService;
import org.scu301.remoteserver.util.Response;
import org.scu301.remoteserver.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        try {
            int areaId = Integer.parseInt(id);
            Optional<AreaDevices> devices = deviceService.getAreaDevices(claims.id(), areaId);
            return devices.map(Response::ok).orElseGet(() -> Response.ok(null));
        } catch (Exception e) {
            return Response.err();
        }
    }

    @GetMapping("/house")
    Result listHouses(@RequestAttribute("claims") Claims claims) {
        return Response.ok(houseAreaService.getHouses(claims.id()));
    }

    @GetMapping("/house/{id}")
    Result listHouseDevices(@RequestAttribute("claims") Claims claims, @PathVariable String id) {
        try {
            int houseId = Integer.parseInt(id);
            Optional<HouseDevices> devices = deviceService.getHouseDevices(claims.id(), houseId);
            return devices.map(Response::ok).orElseGet(() -> Response.ok(null));
        } catch (Exception e) {
            return Result.err();
        }
    }

    // TODO: 未实现
    @GetMapping("/member")
    Result listHouseMember(@RequestAttribute("claims") Claims claims) {
        return Response.ok();
    }
}

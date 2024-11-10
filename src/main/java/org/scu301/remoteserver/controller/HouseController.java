package org.scu301.remoteserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.scu301.remoteserver.dto.http.AddHouseRequest;
import org.scu301.remoteserver.security.Claims;
import org.scu301.remoteserver.service.DeviceDataService;
import org.scu301.remoteserver.service.HouseAreaService;
import org.scu301.remoteserver.util.Response;
import org.scu301.remoteserver.util.Result;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/my/house")
public class HouseController {
    private final HouseAreaService houseAreaService;
    private final DeviceDataService deviceDataService;

    public HouseController(HouseAreaService houseAreaService, DeviceDataService deviceDataService) {
        this.houseAreaService = houseAreaService;
        this.deviceDataService = deviceDataService;
    }

    @PostMapping("")
    Result addHouse(@RequestAttribute("claims") Claims claims, @RequestBody AddHouseRequest request) {
        return Result.of(houseAreaService.addHouse(claims.id(), request.house_name()), "add house error");
    }

    @GetMapping("")
    Result listHouses(@RequestAttribute("claims") Claims claims) {
        return Response.of(houseAreaService.getHousesInfoByAccountId(claims.id()));
    }

    @GetMapping("/{id}")
    Result getHouseDevices(@RequestAttribute("claims") Claims claims, @PathVariable Integer id) {
        return Result.of(deviceDataService.getHouseDevices(claims.id(), id), "no such (account, house)");
    }
}

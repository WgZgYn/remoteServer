package org.scu301.remoteserver.controller;


import lombok.extern.slf4j.Slf4j;
import org.scu301.remoteserver.dto.*;
import org.scu301.remoteserver.entity.Account;
import org.scu301.remoteserver.entity.Area;
import org.scu301.remoteserver.entity.House;
import org.scu301.remoteserver.entity.Member;
import org.scu301.remoteserver.repository.AccountRepository;
import org.scu301.remoteserver.security.Claims;
import org.scu301.remoteserver.service.*;
import org.scu301.remoteserver.util.Response;
import org.scu301.remoteserver.util.Result;
import org.scu301.remoteserver.dto.AreaInfo;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Slf4j
@RestController
@RequestMapping("/api/my")
public class DeviceDataController {
    DeviceDataService deviceService;
    HouseAreaService houseAreaService;
    DataBaseReadService dataBaseReadService;

    DeviceDataController(DeviceDataService deviceService, HouseAreaService houseAreaService,  DataBaseReadService dataBaseReadService) {
        this.deviceService = deviceService;
        this.houseAreaService = houseAreaService;
        this.dataBaseReadService = dataBaseReadService;
    }

    @GetMapping("/area")
    Result listAreas(@RequestAttribute("claims") Claims claims) {
        List<Area> areas = houseAreaService.getAllAreas(claims.id());
        return Response.of(areas.stream().map(AreaInfo::of));
    }

    @GetMapping("/house")
    Result listHouses(@RequestAttribute("claims") Claims claims) {
        List<House> houses = houseAreaService.getHousesByAccountId(claims.id());
        return Response.of(houses.stream().map(HouseInfo::of));
    }

    @GetMapping("/member")
    Result listHouseMember(@RequestAttribute("claims") Claims claims) {
        Optional<Account> accountOptional = dataBaseReadService.getAccount(claims.id());
        if (accountOptional.isPresent()) {
            Set<Member> members = accountOptional.get().getMembers();
            return Response.of(MemberInfo.of(members));
        }
        return Response.of(new ArrayList<>());
    }

    @GetMapping("/area/{id}")
    Result getAreaDevices(@RequestAttribute("claims") Claims claims, @PathVariable Integer id) {
        return Result.of(deviceService.getAreaDevices(claims.id(), id), "no such (account, area)");
    }

    @GetMapping("/house/{id}")
    Result getHouseDevices(@RequestAttribute("claims") Claims claims, @PathVariable Integer id) {
        return Result.of(deviceService.getHouseDevices(claims.id(), id), "no such (account, house)");
    }

    @GetMapping("/device")
    Result getAccountDevices(@RequestAttribute("claims") Claims claims) {
        log.info("get device");
        return Result.of(deviceService.getAccountDevices(claims.id()), "no such (account)");
    }
}

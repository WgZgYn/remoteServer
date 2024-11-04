package org.scu301.remoteserver.controller;

import org.scu301.remoteserver.dto.*;
import org.scu301.remoteserver.entity.Account;
import org.scu301.remoteserver.entity.Area;
import org.scu301.remoteserver.entity.House;
import org.scu301.remoteserver.entity.Member;
import org.scu301.remoteserver.repository.AccountRepository;
import org.scu301.remoteserver.security.Claims;
import org.scu301.remoteserver.service.DeviceDataService;
import org.scu301.remoteserver.service.HouseAreaService;
import org.scu301.remoteserver.util.Response;
import org.scu301.remoteserver.util.Result;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/my")
public class DeviceDataController {
    private final AccountRepository accountRepository;
    DeviceDataService deviceService;
    HouseAreaService houseAreaService;

    DeviceDataController(DeviceDataService deviceService, HouseAreaService houseAreaService, AccountRepository accountRepository) {
        this.deviceService = deviceService;
        this.houseAreaService = houseAreaService;
        this.accountRepository = accountRepository;
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
        Optional<Account> accountOptional = accountRepository.findById(claims.id());
        if (accountOptional.isPresent()) {
            Set<Member> members = accountOptional.get().getMembers();
            return Response.of(MemberInfo.of(members));
        }
        return Response.of(new ArrayList<>());
    }

    @GetMapping("/area/{id}")
    Result getAreaDevices(@RequestAttribute("claims") Claims claims, @PathVariable Integer id) {
        List<Area> areas = houseAreaService.getAllAreas(claims.id());
        for (Area area : areas) {
            if (area.getId().equals(id)) {
                return Response.of(AreaDevices.of(area));
            }
        }
        return Result.err("no such area");
    }

    @GetMapping("/house/{id}")
    Result getHouseDevices(@RequestAttribute("claims") Claims claims, @PathVariable Integer id) {
        List<House> houses = houseAreaService.getHousesByAccountId(claims.id());
        for (House house : houses) {
            if (house.getId().equals(id)) {
                return Response.of(HouseDevices.of(house));
            }
        }
        return Result.err("no such house");
    }

    @GetMapping("/device")
    Response<AccountDevices> getHousesDevice(@RequestAttribute("claims") Claims claims) {
        Optional<AccountDevices> ok = deviceService.getHousesDevices(claims.id());
        return ok.map(Response::of).orElseGet(() -> Response.of(AccountDevices.none()));
    }

}

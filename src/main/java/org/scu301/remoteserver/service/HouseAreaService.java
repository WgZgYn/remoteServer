package org.scu301.remoteserver.service;

import lombok.extern.slf4j.Slf4j;
import org.scu301.remoteserver.entity.Account;
import org.scu301.remoteserver.entity.Area;
import org.scu301.remoteserver.entity.House;

import org.scu301.remoteserver.repository.AccountRepository;
import org.scu301.remoteserver.repository.AreaRepository;
import org.scu301.remoteserver.repository.HouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class HouseAreaService {
    private final AreaRepository areaRepository;
    private final HouseRepository houseRepository;
    AccountRepository accountRepository;
    AccountService accountService;

    HouseAreaService(AccountRepository accountRepository, AreaRepository areaRepository, HouseRepository houseRepository) {
        this.accountRepository = accountRepository;
        this.areaRepository = areaRepository;
        this.houseRepository = houseRepository;
    }

    public List<House> getHouses(int accountId) {
        return accountService.getHouses(accountId);
    }

    public List<Area> getAreas(int houseId) {
        return areaRepository.findAreasByHouseId(houseId);
    }

    public boolean addHouse(int accountId, String houseName) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        Account account;
        if (accountOptional.isEmpty() || houseRepository.existsByHouseName(houseName))
            return false;
        account = accountOptional.get();
        House house = new House();
        house.setHouseName(houseName);
        house.setCreatedBy(account);
        houseRepository.save(house);
        log.info("{} add house {}", accountId, houseName);
        return true;
    }

    public boolean addArea(int accountId, int house_id, String areaName) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty() || !houseRepository.existsById(house_id) || areaRepository.existsByAreaName(areaName))
            return false;
        Account account = accountOptional.get();
        Area area = new Area();
        area.setAreaName(areaName);
        area.setCreatedBy(account);
        areaRepository.save(area);
        log.info("add area {} by user {} to house {}", areaName, accountId, house_id);
        return true;
    }

//    public List<Device> getAreaDevices(int accountId, int houseId, int areaId) {
//
//    }
}

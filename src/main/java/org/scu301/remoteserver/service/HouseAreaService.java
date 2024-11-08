package org.scu301.remoteserver.service;

import lombok.extern.slf4j.Slf4j;
import org.scu301.remoteserver.dto.AreaInfo;
import org.scu301.remoteserver.dto.HouseInfo;
import org.scu301.remoteserver.entity.Account;
import org.scu301.remoteserver.entity.Area;
import org.scu301.remoteserver.entity.House;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class HouseAreaService {
    private final DataBaseReadService dbReadService;
    private final DataBaseWriteService dbWriteService;

    HouseAreaService(DataBaseReadService dbReadService, DataBaseWriteService dbWriteService) {
        this.dbReadService = dbReadService;
        this.dbWriteService = dbWriteService;
    }

    @Transactional
    public List<HouseInfo> getHousesInfoByAccountId(int accountId) {
        return dbReadService
                .getHouses(accountId)
                .stream()
                .map(HouseInfo::of)
                .toList();
    }

    @Transactional
    public List<AreaInfo> getAreasInfoByAccountId(int accountId) {
        return dbReadService
                .getHouses(accountId)
                .stream()
                .map(House::getAreas)
                .flatMap(List::stream)
                .map(AreaInfo::of)
                .toList();
    }

    public boolean addHouse(int accountId, String houseName) {
        Optional<Account> accountOptional = dbReadService.getAccount(accountId);
        Account account;
        if (accountOptional.isEmpty() || dbReadService.existsHouseByUsername(houseName))
            return false;
        account = accountOptional.get();
        House house = new House();
        house.setHouseName(houseName);
        house.setCreatedBy(account);

        dbWriteService.saveHouse(house);
        log.info("{} add house {}", accountId, houseName);
        return true;
    }

    public boolean addArea(int accountId, int house_id, String areaName) {
        Optional<Account> accountOptional = dbReadService.getAccount(accountId);
        if (accountOptional.isEmpty() || !dbReadService.existsHouseById(house_id) || dbReadService.existsAccountByUsername(areaName))
            return false;
        Account account = accountOptional.get();
        Area area = new Area();
        area.setAreaName(areaName);
        area.setCreatedBy(account);

        dbWriteService.saveArea(area);
        log.info("add area {} by user {} to house {}", areaName, accountId, house_id);
        return true;
    }
}

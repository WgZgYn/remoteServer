package org.scu301.remoteserver.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.scu301.remoteserver.dto.AccountDevices;
import org.scu301.remoteserver.dto.AreaDevices;
import org.scu301.remoteserver.dto.HouseDevices;
import org.scu301.remoteserver.entity.Account;
import org.scu301.remoteserver.entity.Area;
import org.scu301.remoteserver.entity.Device;
import org.scu301.remoteserver.entity.House;
import org.scu301.remoteserver.entity.Member;
import org.springframework.stereotype.Service;

@Service
public class DeviceDataService {
    DataBaseReadService dbReadService;

    public DeviceDataService(DataBaseReadService dbReadService) {
        this.dbReadService = dbReadService;
    }

//    @Cacheable(value = "devices", key = "#accountId")
    public List<Device> getAllDevices(int accountId) {
        Optional<Account> accountOptional = dbReadService.getAccount(accountId);
        if (accountOptional.isEmpty()) return new ArrayList<>();
        Account account = accountOptional.get();
        ArrayList<Device> devices = new ArrayList<>();
        account.getMembers().stream().map(Member::getHouse).map(House::getAreas).forEach(areas -> {
                    for (Area area : areas) {
                        devices.addAll(area.getDevices());
                    }
                }
        );
        return devices;
    }

//    @Cacheable(value = "accountDevices", key = "#accountId")
    public Optional<AccountDevices> getHousesDevices(int accountId) {
        return dbReadService.getAccount(accountId).map(AccountDevices::of);
    }

//    @Cacheable(value = "houseDevices", key = "#houseId")
    public Optional<HouseDevices> getHouseDevices(int accountId, int houseId) {
        boolean ok = dbReadService.existsMemberByAccountIdAndHouseId(accountId, houseId);
        if (!ok) return Optional.empty();
        return dbReadService.getHouse(houseId).map(HouseDevices::of);
    }

    public Optional<AreaDevices> getAreaDevices(int accountId, int areaId) {
        boolean ok = dbReadService.getMembers(accountId).stream().anyMatch(member -> member.getHouse().getAreas().stream().anyMatch(area -> area.getId() == areaId));
        if (!ok) return Optional.empty();
        return dbReadService.getArea(areaId).map(AreaDevices::of);
    }

}

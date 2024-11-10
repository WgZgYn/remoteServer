package org.scu301.remoteserver.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.scu301.remoteserver.dto.AccountInfo;
import org.scu301.remoteserver.dto.AreaInfo;
import org.scu301.remoteserver.dto.DeviceInfo;
import org.scu301.remoteserver.dto.HouseInfo;
import org.scu301.remoteserver.dto.http.AccountDevicesResponse;
import org.scu301.remoteserver.dto.http.AreaDevicesResponse;
import org.scu301.remoteserver.dto.http.HouseDevicesResponse;
import org.scu301.remoteserver.entity.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeviceDataService {
    DataBaseReadService dbReadService;
    public DeviceDataService(DataBaseReadService dbReadService) {
        this.dbReadService = dbReadService;
    }

    @Transactional
    public Optional<AccountDevicesResponse> getAccountDevices(int accountId) {
        return dbReadService.getAccount(accountId).map(this::getAccountDevices);
    }

    @Transactional
    public Optional<HouseDevicesResponse> getHouseDevices(int accountId, int houseId) {
        return dbReadService.getHouseByAccountIdAndHouseId(accountId, houseId).map(this::getHouseDevices);
    }

    @Transactional
    public Optional<AreaDevicesResponse> getAreaDevices(int accountId, int areaId) {
        boolean ok = dbReadService.getMembers(accountId).stream().anyMatch(member -> member.getHouse().getAreas().stream().anyMatch(area -> area.getId() == areaId));
        if (!ok) return Optional.empty();
        return dbReadService.getArea(areaId).map(this::getAreaDevices);
    }

    @Transactional
    public Optional<DeviceInfo> getDeviceInfo(int deviceId) {
        return dbReadService.getDevice(deviceId).map(this::getDeviceInfo);
    }

    @Transactional
    public List<Integer> deviceOwners(int deviceId) {
        return dbReadService.getDevice(deviceId).map(device -> {
            Integer houseId = device.getArea().getHouse().getId();
            return dbReadService.getAllAccountIdByHouseId(houseId);
        }).orElse(new ArrayList<>());
    }

    private @NotNull AccountDevicesResponse getAccountDevices(@NotNull Account account) {
        AccountInfo info = new AccountInfo(account.getId(), account.getUsername());
        List<HouseDevicesResponse> houseDevices = account
                .getMembers()
                .stream()
                .map(Member::getHouse)
                .map(this::getHouseDevices)
                .toList();
        return new AccountDevicesResponse(info, houseDevices);
    }

    private @NotNull HouseDevicesResponse getHouseDevices(@NotNull House house) {
        HouseInfo info = new HouseInfo(house.getId(), house.getHouseName());
        List<AreaDevicesResponse> areaDevices = house
                .getAreas()
                .stream().map(this::getAreaDevices)
                .toList();
        return new HouseDevicesResponse(info, areaDevices);
    }

    private @NotNull AreaDevicesResponse getAreaDevices(@NotNull Area area) {
        AreaInfo info = new AreaInfo(area.getId(), area.getAreaName());
        List<DeviceInfo> areaDevices = area
                .getDevices()
                .stream().map(this::getDeviceInfo)
                .toList();
        return new AreaDevicesResponse(info, areaDevices);
    }


    @Contract("_ -> new")
    private @NotNull DeviceInfo getDeviceInfo(@NotNull Device device) {
        DeviceModel model = device.getModel();
        return new DeviceInfo(
                device.getId(),
                device.getDeviceName(),
                device.getEfuseMac(),
                model.getModelName(),
                model.getType(),
                model.getDeviceControls().stream().map(DeviceControl::getParameter).toList());
    }
}

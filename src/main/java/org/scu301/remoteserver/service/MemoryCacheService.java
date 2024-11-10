package org.scu301.remoteserver.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.scu301.remoteserver.dto.DeviceInfo;
import org.scu301.remoteserver.entity.*;
import org.scu301.remoteserver.dto.http.AccountDevicesResponse;
import org.scu301.remoteserver.dto.http.AreaDevicesResponse;
import org.scu301.remoteserver.dto.http.HouseDevicesResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class MemoryCacheService {
    Map<Integer, Set<Integer>> deviceId2AccountId = new ConcurrentHashMap<>();
    Map<Integer, Set<Integer>> accountId2DeviceId = new ConcurrentHashMap<>();
    Map<Integer, String> deviceId2EFuseMac = new ConcurrentHashMap<>();
    Map<String, Integer> eFuseMac2DeviceId = new ConcurrentHashMap<>();

    // This is not right when the data have Lazy,
    Map<Integer, Account> accountCache = new ConcurrentHashMap<>();
    Map<String, Account> accountCache2 = new ConcurrentHashMap<>();

    DataBaseReadService dbReadService;


    MemoryCacheService(DataBaseReadService dbReadService) {
        this.dbReadService = dbReadService;
    }

    @Transactional
    public Set<Integer> getAccountIds(Integer deviceId) {
        if (!deviceId2AccountId.containsKey(deviceId))
            fetchDeviceCacheFromDataBase(deviceId);
        return deviceId2AccountId.get(deviceId);
    }

    @Transactional
    protected Set<Integer> fetchDeviceCacheFromDataBase(int deviceId) {
        if (!deviceId2AccountId.containsKey(deviceId)) {
            Optional<Device> device1 = dbReadService.getDevice(deviceId);
            if (device1.isEmpty()) return new HashSet<>();
            Device device = device1.get();
            Area area = device.getArea();
            House house = area.getHouse();
            eFuseMac2DeviceId.put(device.getEfuseMac(), deviceId);
            house.getMembers().stream().map(Member::getAccount).map(Account::getId).forEach(accountId -> {
                accountId2DeviceId.computeIfAbsent(accountId, k -> new HashSet<>()).add(deviceId);
                deviceId2AccountId.computeIfAbsent(deviceId, k -> new HashSet<>()).add(accountId);
            });
        }
        return deviceId2AccountId.get(deviceId);
    }

    // be careful make sure the account is updated
    public Account getAccount(int accountId) {
        return accountCache.get(accountId);
    }

    // be careful make sure the account is updated
    public Account getAccount(String username) {
        return accountCache2.get(username);
    }

    // database write to toggle this
    public void putAccount(Account account) {
        accountCache.put(account.getId(), account);
        accountCache2.put(account.getUsername(), account);
    }

    // a bit difficult
    public boolean isDeviceInChargeOfAccount(int deviceId, int accountId) {
        return true;
    }

    // for mqtt message recognize sender
    @Transactional
    public Optional<Integer> getDeviceId(String eFuseMac) {
        if (!eFuseMac2DeviceId.containsKey(eFuseMac)) {
            log.info("ready to read database");
            Optional<Device> device = dbReadService.getDevice(eFuseMac);
            device.map(device1 -> {
                DeviceModel model = device1.getModel();
                DeviceInfo info = new DeviceInfo(
                        device1.getId(),
                        device1.getDeviceName(),
                        device1.getEfuseMac(),
                        model.getModelName(),
                        model.getType(),
                        model.getDeviceControls().stream().map(DeviceControl::getParameter).toList());
                log.info("ready to cache info");
                cache(info);
                return info;
            });
        }
        log.info("return");
        return Optional.of(eFuseMac2DeviceId.get(eFuseMac));
    }

    @Transactional
    public Optional<String> getEFuseMac(int deviceId) {
        if (!deviceId2EFuseMac.containsKey(deviceId)) {
            Optional<Device> device = dbReadService.getDevice(deviceId);
            if (device.isEmpty()) return Optional.empty();
            device.map(device1 -> {
                DeviceModel model = device1.getModel();
                DeviceInfo info = new DeviceInfo(
                        device1.getId(),
                        device1.getDeviceName(),
                        device1.getEfuseMac(),
                        model.getModelName(),
                        model.getType(),
                        model.getDeviceControls().stream().map(DeviceControl::getParameter).toList());
                cache(info);
                return info;
            });
        }
        return Optional.of(deviceId2EFuseMac.get(deviceId));
    }

    public void cache(@NotNull AccountDevicesResponse response) {
        Integer accountId = response.account_info().account_id();
        response.houses_devices().forEach(house -> cache(house, accountId));
    }

    public void cache(@NotNull HouseDevicesResponse response, Integer accountId) {
        response.areas_devices().forEach(area -> cache(area, accountId));
    }

    public void cache(@NotNull AreaDevicesResponse response, Integer accountId) {
        response.devices().forEach(deviceInfo -> cache(deviceInfo, accountId));
    }

    public void cache(@NotNull DeviceInfo deviceInfo, Integer accountId) {
        accountId2DeviceId.computeIfAbsent(accountId, k -> new HashSet<>()).add(deviceInfo.device_id());
        deviceId2AccountId.computeIfAbsent(deviceInfo.device_id(), k -> new HashSet<>()).add(accountId);

        deviceId2EFuseMac.put(deviceInfo.device_id(), deviceInfo.efuse_mac());
        deviceId2EFuseMac.put(deviceInfo.device_id(), deviceInfo.efuse_mac());
        eFuseMac2DeviceId.put(deviceInfo.efuse_mac(), deviceInfo.device_id());
    }

    public void cache(@NotNull DeviceInfo deviceInfo) {
        deviceId2EFuseMac.put(deviceInfo.device_id(), deviceInfo.efuse_mac());
        eFuseMac2DeviceId.put(deviceInfo.efuse_mac(), deviceInfo.device_id());
    }
}

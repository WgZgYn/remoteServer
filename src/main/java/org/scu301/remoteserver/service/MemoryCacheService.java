package org.scu301.remoteserver.service;

import org.jetbrains.annotations.NotNull;
import org.scu301.remoteserver.entity.*;
import org.scu301.remoteserver.dto.http.AccountDevicesResponse;
import org.scu301.remoteserver.dto.http.AreaDevicesResponse;
import org.scu301.remoteserver.dto.http.HouseDevicesResponse;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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

    public Set<Integer> getDeviceIdToAccountId(int deviceId) {
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

    public Account getAccount(int accountId) {
        return accountCache.get(accountId);
    }

    public Account getAccount(String username) {
        return accountCache2.get(username);
    }

    public void putAccount(Account account) {
        accountCache.put(account.getId(), account);
        accountCache2.put(account.getUsername(), account);
    }

    public boolean isDeviceInChargeOfAccount(int deviceId, int accountId) {
        return true;
    }
    public Integer getDeviceId(String eFuseMac) {
        return eFuseMac2DeviceId.get(eFuseMac);
    }

    // 多余的
    public void cache(@NotNull AccountDevicesResponse response) {
        Integer accountId = response.account_info().account_id();
        response.houses_devices().forEach(house -> cache(house, accountId));
    }

    public void cache(@NotNull HouseDevicesResponse response, Integer accountId) {
        response.areas_devices().forEach(area -> cache(area, accountId));
    }

    public void cache(@NotNull AreaDevicesResponse response, Integer accountId) {
        response.devices().forEach(device -> {
            accountId2DeviceId.computeIfAbsent(accountId, k -> new HashSet<>()).add(device.device_id());
            deviceId2AccountId.computeIfAbsent(device.device_id(), k -> new HashSet<>()).add(accountId);
            deviceId2EFuseMac.put(device.device_id(), device.device_name());
            eFuseMac2DeviceId.put(device.device_name(), device.device_id());
        });
    }
}

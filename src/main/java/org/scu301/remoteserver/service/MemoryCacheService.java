package org.scu301.remoteserver.service;

import org.scu301.remoteserver.entity.*;
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
    Map<String, Integer> eFuseMac2DeviceId = new ConcurrentHashMap<>();

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
}

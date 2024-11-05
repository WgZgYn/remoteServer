package org.scu301.remoteserver.service;

import org.scu301.remoteserver.entity.*;
import org.scu301.remoteserver.repository.*;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DataBaseReadService {
    private final HouseRepository houseRepository;
    private final AreaRepository areaRepository;
    private final MemberRepository memberRepository;
    private final DeviceRepository deviceRepository;
    private final AccountRepository accountRepository;


    DataBaseReadService(AccountRepository accountRepository, HouseRepository houseRepository, AreaRepository areaRepository, MemberRepository memberRepository, DeviceRepository deviceRepository) {
        this.accountRepository = accountRepository;
        this.houseRepository = houseRepository;
        this.areaRepository = areaRepository;
        this.memberRepository = memberRepository;
        this.deviceRepository = deviceRepository;
    }

    boolean existsAccountByUsername(String username) {
        return accountRepository.existsAccountByUsername(username);
    }

    boolean existsHouseByUsername(String username) {
        return houseRepository.existsByHouseName(username);
    }

    boolean existsHouseById(Integer houseId) {
        return houseRepository.existsById(houseId);
    }

    boolean existsMemberByAccountIdAndHouseId(Integer accountId, Integer houseId) {
        return memberRepository.existsByAccountIdAndHouseId(accountId, houseId);
    }

    public Optional<Device> getDevice(Integer deviceId) {
        return deviceRepository.findById(deviceId);
    }

    public Optional<Device> getDeviceByMac(String mac) {
        return deviceRepository.findDeviceByEfuseMac(mac);
    }

    public Optional<Account> getAccount(String username) {
        return accountRepository.findAccountByUsername(username);
    }

    public Optional<Account> getAccount(Integer accountId) {
        return accountRepository.findById(accountId);
    }

    public Optional<House> getHouse(Integer houseId) {
        return houseRepository.findById(houseId);
    }

    public Optional<Area> getArea(Integer areaId) {
        return areaRepository.findById(areaId);
    }

    public Set<Member> getMembers(Integer accountId) {
        return getAccount(accountId).map(Account::getMembers).orElse(new LinkedHashSet<>());
    }

    public List<House> getHouses(Integer accountId) {
        return getMembers(accountId).stream().map(Member::getHouse).toList();
    }

    public List<Account> getFamily(Integer accountId) {
        return getMembers(accountId).stream().map(Member::getAccount).toList();
    }
}

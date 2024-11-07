package org.scu301.remoteserver.service;

import org.scu301.remoteserver.entity.*;
import org.scu301.remoteserver.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DataBaseReadService {
    private final HouseRepository houseRepository;
    private final AreaRepository areaRepository;
    private final MemberRepository memberRepository;
    private final DeviceRepository deviceRepository;
    private final AccountRepository accountRepository;
    private final UserInfoRepository userInfoRepository;


    DataBaseReadService(AccountRepository accountRepository, HouseRepository houseRepository, AreaRepository areaRepository, MemberRepository memberRepository, DeviceRepository deviceRepository, UserInfoRepository userInfoRepository) {
        this.accountRepository = accountRepository;
        this.houseRepository = houseRepository;
        this.areaRepository = areaRepository;
        this.memberRepository = memberRepository;
        this.deviceRepository = deviceRepository;
        this.userInfoRepository = userInfoRepository;
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

    public Optional<House> getHouseByAccountIdAndHouseId(Integer accountId, Integer houseId) {
        return memberRepository.findHouseByAccountIdAndHouseId(accountId, houseId);
    }

    public Optional<Area> getArea(Integer areaId) {
        return areaRepository.findById(areaId);
    }

    public List<Member> getMembers(Integer accountId) {
        return memberRepository.findMembersByAccountId(accountId);
    }

    public List<Integer> getAllHouseIdByAccountId(Integer accountId) {
        return memberRepository.findAllHouseIdByAccountId(accountId);
    }

    public List<Integer> getAllAccountIdByHouseId(Integer houseId) {
        return memberRepository.findAllAccountIdByHouseId(houseId);
    }

    public List<House> getHouses(Integer accountId) {
//        return
        return getMembers(accountId).stream().map(Member::getHouse).toList();
    }

    public List<Account> getFamily(Integer accountId) {
        return getMembers(accountId).stream().map(Member::getAccount).toList();
    }

    public Optional<UserInfo> getUserInfoByAccountId(Integer accountId) {
        return userInfoRepository.findUserInfoByAccountId(accountId);
    }
}

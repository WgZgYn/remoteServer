package org.scu301.remoteserver.service;

import org.scu301.remoteserver.dto.AccountDevices;
import org.scu301.remoteserver.dto.HouseDevices;
import org.scu301.remoteserver.entity.*;
import org.scu301.remoteserver.repository.AccountRepository;
import org.scu301.remoteserver.repository.HouseRepository;
import org.scu301.remoteserver.repository.MemberRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceDataService {
    private final AccountRepository accountRepository;
    private final HouseRepository houseRepository;
    private final MemberRepository memberRepository;


    public DeviceDataService(AccountRepository accountRepository, HouseRepository houseRepository, MemberRepository memberRepository) {
        this.accountRepository = accountRepository;
        this.houseRepository = houseRepository;
        this.memberRepository = memberRepository;
    }

//    @Cacheable(value = "devices", key = "#accountId")
    public List<Device> getAllDevices(int accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);
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
        return accountRepository.findById(accountId).map(AccountDevices::of);
    }

//    @Cacheable(value = "houseDevices", key = "#houseId")
    public Optional<HouseDevices> getHouseDevices(int accountId, int houseId) {
        boolean ok = memberRepository.existsByAccountIdAndHouseId(accountId, houseId);
        if (!ok) return Optional.empty();
        return houseRepository.findById(houseId).map(HouseDevices::of);
    }
}

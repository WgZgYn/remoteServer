package org.scu301.remoteserver.service;

import org.scu301.remoteserver.dto.AccountDevices;
import org.scu301.remoteserver.repository.AccountRepository;
import org.scu301.remoteserver.repository.HouseRepository;
import org.scu301.remoteserver.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceService {
    private final AccountRepository accountRepository;
    private final HouseRepository houseRepository;
    private final MemberRepository memberRepository;


    public DeviceService(AccountRepository accountRepository, HouseRepository houseRepository, MemberRepository memberRepository) {
        this.accountRepository = accountRepository;
        this.houseRepository = houseRepository;
        this.memberRepository = memberRepository;
    }

    public Optional<AccountDevices> getHousesDevices(int accountId) {
        return accountRepository.findById(accountId).map(AccountDevices::of);
    }

    public Optional<AccountDevices.HouseDevices> getHouseDevices(int accountId, int houseId) {
        boolean ok = memberRepository.existsByAccountIdAndHouseId(accountId, houseId);
        if (!ok) return Optional.empty();
        return houseRepository.findById(houseId).map(AccountDevices.HouseDevices::of);
    }
}

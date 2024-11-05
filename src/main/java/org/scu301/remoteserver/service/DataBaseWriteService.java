package org.scu301.remoteserver.service;

import lombok.extern.slf4j.Slf4j;
import org.scu301.remoteserver.entity.Account;
import org.scu301.remoteserver.entity.Area;
import org.scu301.remoteserver.entity.House;
import org.scu301.remoteserver.repository.AccountRepository;
import org.scu301.remoteserver.repository.AreaRepository;
import org.scu301.remoteserver.repository.HouseRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DataBaseWriteService {
    private final HouseRepository houseRepository;
    private final AccountRepository accountRepository;
    private final AreaRepository areaRepository;

    public DataBaseWriteService(HouseRepository houseRepository, AccountRepository accountRepository, AreaRepository areaRepository) {
        this.houseRepository = houseRepository;
        this.accountRepository = accountRepository;
        this.areaRepository = areaRepository;
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public House saveHouse(House house) {
        return houseRepository.save(house);
    }

    public Area saveArea(Area area) {
        return areaRepository.save(area);
    }
}

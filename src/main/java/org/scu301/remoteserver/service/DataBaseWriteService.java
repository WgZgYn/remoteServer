package org.scu301.remoteserver.service;

import lombok.extern.slf4j.Slf4j;
import org.scu301.remoteserver.entity.Account;
import org.scu301.remoteserver.entity.Area;
import org.scu301.remoteserver.entity.House;
import org.scu301.remoteserver.entity.UserInfo;
import org.scu301.remoteserver.repository.AccountRepository;
import org.scu301.remoteserver.repository.AreaRepository;
import org.scu301.remoteserver.repository.HouseRepository;
import org.scu301.remoteserver.repository.UserInfoRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DataBaseWriteService {
    private final HouseRepository houseRepository;
    private final AccountRepository accountRepository;
    private final AreaRepository areaRepository;
    private final UserInfoRepository userInfoRepository;

    public DataBaseWriteService(HouseRepository houseRepository, AccountRepository accountRepository, AreaRepository areaRepository, UserInfoRepository userInfoRepository) {
        this.houseRepository = houseRepository;
        this.accountRepository = accountRepository;
        this.areaRepository = areaRepository;
        this.userInfoRepository = userInfoRepository;
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

    public UserInfo saveUserInfo(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }
}

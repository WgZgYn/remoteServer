package org.scu301.remoteserver.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.scu301.remoteserver.dto.UserInfoDTO;
import org.scu301.remoteserver.entity.Account;
import org.scu301.remoteserver.entity.UserInfo;
import org.scu301.remoteserver.security.Argon2Utils;
import org.scu301.remoteserver.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class AccountService {
    DataBaseReadService dbReadService;
    DataBaseWriteService dbWriteService;

    AccountService(DataBaseReadService dbReadService, DataBaseWriteService dbWriteService) {
        this.dbReadService = dbReadService;
        this.dbWriteService = dbWriteService;
    }



    @Transactional
    public boolean addAccount(String username, String password) {
        boolean exist = dbReadService.existsAccountByUsername(username);
        if (exist) return false;
        Pair<String, byte[]> pair = Argon2Utils.encrypt(password.toCharArray());
        Account account = new Account();
        account.setUsername(username);
        account.setPasswordHash(pair.key());
        account.setSalt(Argon2Utils.hex(pair.value()));
        account.setRole("user");
        log.info(account.toString());
        dbWriteService.saveAccount(account);
        log.info("add account {}", username);
        return true;
    }

    @Transactional
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Optional<Account> accountOptional = dbReadService.getAccount(username);
        if (accountOptional.isEmpty()) return false;
        Account account = accountOptional.get();
        boolean ok = Argon2Utils.verify(oldPassword, account.getPasswordHash().toCharArray());
        if (!ok) return false;
        account.setPasswordHash(newPassword);
        dbWriteService.saveAccount(account);
        log.info("user {} change password", username);
        return true;
    }

    public Optional<UserInfoDTO> getUserInfo(Integer accountId) {
        return dbReadService.getUserInfoByAccountId(accountId).map(userInfo -> new UserInfoDTO(userInfo.getId(), userInfo.getAge(), userInfo.getLocation(), userInfo.getGender(), userInfo.getEmail()));
    }

    @Transactional
    public boolean saveUserInfo(@NotNull UserInfoDTO userInfoDTO) {
        Optional<UserInfo> userInfoOptional = dbReadService.getUserInfoByAccountId(userInfoDTO.id());
        if (userInfoOptional.isEmpty()) {
            return false;
        }
        UserInfo userInfo = userInfoOptional.get();
        if (userInfoDTO.age() != null) {
            userInfo.setAge(userInfoDTO.age());
        }
        if (userInfoDTO.location() != null) {
            userInfo.setLocation(userInfoDTO.location());
        }
        if (userInfoDTO.gender() != null) {
            userInfo.setGender(userInfoDTO.gender());
        }
        if (userInfoDTO.email() != null) {
            userInfo.setEmail(userInfoDTO.email());
        }
        return dbWriteService.saveUserInfo(userInfo) != null;
    }
}

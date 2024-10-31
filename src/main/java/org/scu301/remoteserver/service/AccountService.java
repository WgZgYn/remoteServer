package org.scu301.remoteserver.service;

import lombok.extern.slf4j.Slf4j;
import org.scu301.remoteserver.entity.Account;
import org.scu301.remoteserver.entity.House;
import org.scu301.remoteserver.entity.Member;
import org.scu301.remoteserver.repository.AccountRepository;
import org.scu301.remoteserver.security.Argon2Utils;
import org.scu301.remoteserver.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountService {
    AccountRepository accountRepository;

    AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean addAccount(String username, String password) {
        boolean exist = accountRepository.existsAccountByUsername(username);
        if (exist) return false;
        Pair<String, byte[]> pair = Argon2Utils.encrypt(password.toCharArray());
        Account account = new Account();
        account.setUsername(username);
        account.setPasswordHash(pair.key());
        account.setSalt(Argon2Utils.hex(pair.value()));
        account.setRole("user");
        log.info(account.toString());
        accountRepository.save(account);
        log.info("add account {}", username);
        return true;
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        Optional<Account> accountOptional = accountRepository.findAccountByUsername(username);
        if (accountOptional.isEmpty()) return false;
        Account account = accountOptional.get();
        boolean ok = Argon2Utils.verify(oldPassword, account.getPasswordHash().toCharArray());
        if (!ok) return false;
        account.setPasswordHash(newPassword);
        accountRepository.save(account);
        log.info("user {} change password", username);
        return true;
    }

    public Optional<Account> getAccount(int accountId) {
        return accountRepository.findById(accountId);
    }

    public List<Member> getMembers(int accountId) {
        return getAccount(accountId).map(Account::getMembers).orElse(new ArrayList<>());
    }

    public List<House> getHouses(int accountId) {
        return getMembers(accountId).stream().map(Member::getHouse).toList();
    }

    public List<Account> getFamily(int accountId) {
        return getMembers(accountId).stream().map(Member::getAccount).toList();
    }
}

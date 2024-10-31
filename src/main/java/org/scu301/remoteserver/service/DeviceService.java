package org.scu301.remoteserver.service;

import org.scu301.remoteserver.dto.AccountDevices;
import org.scu301.remoteserver.repository.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceService {
    private final AccountRepository accountRepository;

    public DeviceService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<AccountDevices> getDevices(int accountId) {
        return accountRepository.findById(accountId).map(AccountDevices::of);
    }
}

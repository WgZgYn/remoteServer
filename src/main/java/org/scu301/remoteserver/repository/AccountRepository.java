package org.scu301.remoteserver.repository;

import org.scu301.remoteserver.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findAccountByUsername(String username);
    boolean existsAccountByUsername(String username);
}

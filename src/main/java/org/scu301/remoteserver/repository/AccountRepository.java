package org.scu301.remoteserver.repository;

import org.scu301.remoteserver.dto.AccountId;
import org.scu301.remoteserver.dto.HouseId;
import org.scu301.remoteserver.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findAccountByUsername(String username);
    boolean existsAccountByUsername(String username);

    @Query("SELECT new org.scu301.remoteserver.dto.AccountId(a.id) FROM Account a")
    List<AccountId> findAllAccountId();

    @Query("SELECT new org.scu301.remoteserver.dto.HouseId(m.house.id) FROM Member m WHERE m.account.id = :id")
    List<HouseId> findAllHouseIdById(@Param("id") Integer id);
}

package org.scu301.remoteserver.repository;

import org.scu301.remoteserver.entity.House;
import org.scu301.remoteserver.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    List<Member> findMembersByAccountId(Integer accountId);

    boolean existsByAccountIdAndHouseId(Integer accountId, Integer houseId);

    @Query("SELECT m.house FROM Member m WHERE m.account.id = :accountId")
    List<House> findAllHousesByAccountId(@Param("accountId") Integer accountId);

    @Query("SELECT m.house.id FROM Member m WHERE m.account.id = :id")
    List<Integer> findAllHouseIdByAccountId(@Param("id") Integer id);

    @Query("SELECT m.house.id FROM Member m WHERE m.account.id = :id")
    List<Integer> findAllAccountIdByHouseId(@Param("id") Integer id);

    @Query("SELECT m.house FROM Member m WHERE m.account.id = :accountId AND m.house.id = :houseId")
    Optional<House> findHouseByAccountIdAndHouseId(@Param("accountId") Integer accountId, @Param("houseId") Integer houseId);
}

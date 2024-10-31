package org.scu301.remoteserver.repository;

import org.scu301.remoteserver.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findMembersByAccountId(Integer accountId);
}

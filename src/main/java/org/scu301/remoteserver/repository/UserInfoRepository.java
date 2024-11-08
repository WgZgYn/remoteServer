package org.scu301.remoteserver.repository;

import org.scu301.remoteserver.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findUserInfoById(Integer accountId);
}

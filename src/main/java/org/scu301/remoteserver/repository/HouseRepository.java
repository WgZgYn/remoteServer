package org.scu301.remoteserver.repository;

import org.scu301.remoteserver.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<House, Integer> {
    boolean existsByHouseName(String name);
}

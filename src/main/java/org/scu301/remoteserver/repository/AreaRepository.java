package org.scu301.remoteserver.repository;

import java.util.List;

import org.scu301.remoteserver.entity.Area;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area, Integer> {
    boolean existsByAreaName(String name);
    List<Area> findAreasByHouseId(Integer houseId);
//    boolean existsByHouseId(Integer houseId,Integer areaId);
}

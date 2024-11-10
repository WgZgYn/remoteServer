package org.scu301.remoteserver.repository;

import org.scu301.remoteserver.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
    List<Device> findDevicesByAreaId(Integer area_id);
    Optional<Device> findByEfuseMac(String eFuseMac);
    boolean existsByEfuseMac(String eFuseMac);
    Optional<Device> findDeviceByEfuseMac(String efuseMac);
}


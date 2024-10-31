package org.scu301.remoteserver.repository;

import org.scu301.remoteserver.entity.DeviceControl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceControlRepository extends JpaRepository<DeviceControl, Integer> {
}

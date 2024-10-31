package org.scu301.remoteserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.scu301.remoteserver.entity.*;

import java.util.List;

@Data
@AllArgsConstructor
public class AccountDevices {
    public record DeviceInfo(int device_id,
                             String device_name,
                             String efuse_mac,
                             String chip_model,
                             DeviceType device_type) {
        public static DeviceInfo of(Device device) {
            return new DeviceInfo(
                    device.getId(),
                    device.getDeviceName(),
                    device.getEfuseMac(),
                    device.getChipModel(),
                    device.getType()
            );
        }
    }

    public record AreaDevices(int area_id,
                              String area_name,
                              List<DeviceInfo> devices) {
        public static AreaDevices of(Area area) {
            return new AccountDevices.AreaDevices(
                    area.getId(),
                    area.getAreaName(),
                    area.getDevices()
                            .stream()
                            .map(AccountDevices.DeviceInfo::of)
                            .toList()
            );
        }
    }

    public record HouseDevices(int house_id,
                               String house_name,
                               List<AreaDevices> areas_devices) {
        public static HouseDevices of(House house) {
            return new AccountDevices.HouseDevices(
                    house.getId(),
                    house.getHouseName(),
                    house.getAreas()
                            .stream()
                            .map(AreaDevices::of)
                            .toList());
        }
    }

    List<HouseDevices> house_devices;

    public static AccountDevices from(List<HouseDevices> house_devices) {
        return new AccountDevices(house_devices);
    }

    public static AccountDevices of(Account account) {
        return AccountDevices
                .from(account
                        .getMembers()
                        .stream()
                        .map(Member::getHouse)
                        .map(HouseDevices::of)
                        .toList());
    }
}
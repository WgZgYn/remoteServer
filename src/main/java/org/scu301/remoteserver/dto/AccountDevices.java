package org.scu301.remoteserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.scu301.remoteserver.entity.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class AccountDevices {
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

    public static AccountDevices none() {
        return AccountDevices.from(new ArrayList<>());
    }
}
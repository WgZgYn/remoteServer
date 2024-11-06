package org.scu301.remoteserver.vo;

import java.util.List;

public record AccountDevicesResponse(AccountInfo account_info, List<HouseDevicesResponse> houses_devices) {
}
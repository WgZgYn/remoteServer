package org.scu301.remoteserver.dto.http;

import org.scu301.remoteserver.dto.AccountInfo;

import java.util.List;

public record AccountDevicesResponse(AccountInfo account_info, List<HouseDevicesResponse> houses_devices) {
}
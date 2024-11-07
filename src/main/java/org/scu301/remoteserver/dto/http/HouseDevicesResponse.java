package org.scu301.remoteserver.dto.http;

import org.scu301.remoteserver.dto.HouseInfo;

import java.util.List;

public record HouseDevicesResponse(HouseInfo house_info, List<AreaDevicesResponse> areas_devices) {
}
package org.scu301.remoteserver.dto.http;

import org.scu301.remoteserver.dto.AreaInfo;
import org.scu301.remoteserver.dto.DeviceInfo;

import java.util.List;

public record AreaDevicesResponse(AreaInfo area_info, List<DeviceInfo> devices) {
}
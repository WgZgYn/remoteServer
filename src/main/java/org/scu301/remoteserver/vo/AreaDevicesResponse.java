package org.scu301.remoteserver.vo;

import java.util.List;

public record AreaDevicesResponse(AreaInfo area_info, List<DeviceInfo> devices) {
}
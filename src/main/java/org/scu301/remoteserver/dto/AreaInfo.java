package org.scu301.remoteserver.dto;

import org.scu301.remoteserver.entity.Area;

public record AreaInfo(Integer area_id, String area_name) {
    public static AreaInfo of(Area area) {
        return new AreaInfo(area.getId(), area.getAreaName());
    }
}

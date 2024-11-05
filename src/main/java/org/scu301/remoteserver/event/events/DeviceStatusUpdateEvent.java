package org.scu301.remoteserver.event.events;

import java.util.Set;

public record DeviceStatusUpdateEvent(Set<Integer> account_id, int device_id) {
}

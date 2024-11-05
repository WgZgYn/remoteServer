package org.scu301.remoteserver.service;

import org.jetbrains.annotations.NotNull;
import org.scu301.remoteserver.event.events.DeviceStatusUpdateEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseConnectionService {
    private final ConcurrentHashMap<Integer, Sinks.Many<String>> userSinks = new ConcurrentHashMap<>();

    public Sinks.Many<String> getUserSink(int accountId) {
        return userSinks.computeIfAbsent(accountId, key -> Sinks.many().multicast().onBackpressureBuffer());
    }

    public void sendEvent(Integer userId, String event) {
        Sinks.Many<String> sink = userSinks.get(userId);
        if (sink != null) {
            sink.tryEmitNext(event);
        }
    }

    @EventListener
    private void onDeviceStatusUpdate(@NotNull DeviceStatusUpdateEvent event) {
        for (Integer accountId : event.account_id()) {
            sendEvent(accountId, String.valueOf(event.device_id()));
        }
    }
}

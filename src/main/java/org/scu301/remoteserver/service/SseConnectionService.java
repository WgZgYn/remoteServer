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

    // 获取或创建用户的Sink
    public Sinks.Many<String> getUserSink(int accountId) {
        return userSinks.computeIfAbsent(accountId, key -> Sinks.many().multicast().onBackpressureBuffer());
    }

    // 发送事件
    public void sendEvent(Integer userId, String event) {
        Sinks.Many<String> sink = userSinks.get(userId);
        if (sink != null) {
            // 检查是否可以发送数据
            if (!sink.tryEmitNext(event).isSuccess()) {
                // 如果发送失败，则尝试关闭连接
                closeUserConnections(userId);
            }
        }
    }

    // 关闭用户的所有连接
    public void closeUserConnections(Integer userId) {
        Sinks.Many<String> sink = userSinks.remove(userId);
        if (sink != null) {
            sink.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST);
        }
    }

    // 监听设备状态更新事件，推送到用户
    @EventListener
    private void onDeviceStatusUpdate(@NotNull DeviceStatusUpdateEvent event) {
        for (Integer accountId : event.account_id()) {
            sendEvent(accountId, String.valueOf(event.device_id()));
        }
    }
}

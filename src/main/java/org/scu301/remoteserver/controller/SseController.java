package org.scu301.remoteserver.controller;

import org.scu301.remoteserver.security.Claims;
import org.scu301.remoteserver.service.SseConnectionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.concurrent.ConcurrentHashMap;


/**
 * 处理与浏览器的<code>SSE</code>连接<br>
 * <code>account</code>来选定<code>SSE</code>内容<br>
 * 每一个<code>account</code>可以有多个<code>Session</code>连接
 * <h2>功能</h2>
 * <ul>
 *     <li>streamSse</li>
 *     <li>sendEvent</li>
 * </ul>
 * @author wzy
 * @version 0.1
 * @since 2024/11/1
 */
@RestController
@RequestMapping("/api")
public class SseController {
    // 存储每个用户的 Sink
    SseConnectionService sseConnectionService;
    SseController(SseConnectionService sseConnectionService) {
        this.sseConnectionService = sseConnectionService;
    }

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamSse(@RequestAttribute("claims") Claims claims) {
        Integer accountId = claims.id();
        // 获取或创建用户的 Sink
        Sinks.Many<String> sink = sseConnectionService.getUserSink(accountId);
        return sink.asFlux();
    }
}

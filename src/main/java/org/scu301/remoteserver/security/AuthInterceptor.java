package org.scu301.remoteserver.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Instant start = Instant.now();
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring("Bearer ".length());
            try {
                Claims claims = JwtUtils.parseToken(token);
                request.setAttribute("claims", claims); // 存储在请求属性中
                log.info("{}", Duration.between(start, Instant.now()));
                return true; // 继续请求
            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: " + e.getMessage());
                log.info("{}", Duration.between(start, Instant.now()));
                return false; // 拦截请求
            }
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("Missing Authorization Header");
        log.info("{}", Duration.between(start, Instant.now()));
        return false; // 拦截请求
    }
}

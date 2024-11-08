package org.scu301.remoteserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.scu301.remoteserver.dto.UserInfoDTO;
import org.scu301.remoteserver.dto.http.*;
import org.scu301.remoteserver.security.Claims;
import org.scu301.remoteserver.service.AccountService;
import org.scu301.remoteserver.service.AuthAccountService;
import org.scu301.remoteserver.util.Response;
import org.scu301.remoteserver.util.Result;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@RestController
@RequestMapping("/api")
public class AccountController {
    AuthAccountService authAccountService;
    AccountService accountService;

    AccountController(AuthAccountService authAccountService, AccountService accountService) {
        this.authAccountService = authAccountService;
        this.accountService = accountService;
    }

    @PostMapping("/login")
    Result login(@RequestBody LoginRequest request) {
        Instant now = Instant.now();
        try {
            LoginResponse ok = authAccountService.authenticate(request.username(), request.password());
            log.info("{}", Duration.between(now, Instant.now()));
            return Response.of(ok);
        } catch (AuthAccountService.AuthError e) {
            log.info("{}", Duration.between(now, Instant.now()));
            return Response.err(e.getMessage());
        }
    }

    @PostMapping("/signup")
    Result signup(@RequestBody SignupRequest request) {
        boolean ok = accountService.addAccount(request.username(), request.password());
        return ok ? Result.ok() : Result.err("User already exists");
    }

    @PostMapping("/auth")
    Result auth() {
        return Result.ok();
    }

    @PostMapping("/update/account")
    Result updateInfo(@RequestBody AccountUpdateRequest request) {
        return Result.of(authAccountService.updateInfo(request), "error on update account");
    }

    @GetMapping("/userinfo")
    Result getUserInfo(@RequestAttribute("claims") Claims claims) {
        return Result.of(accountService.getUserInfo(claims.id()), "no userinfo");
    }

    @PostMapping("/userinfo")
    Result updateUserInfo(@RequestAttribute("claims") Claims claims, @RequestBody UserInfoDTO request) {
        if (claims.id().intValue() != request.id().intValue()) {
            return Result.err("id mismatch");
        }
        return Result.of(accountService.saveUserInfo(request), "save userinfo error");
    }
}

package org.scu301.remoteserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.scu301.remoteserver.dto.LoginRequest;
import org.scu301.remoteserver.dto.LoginResponse;
import org.scu301.remoteserver.dto.SignupRequest;
import org.scu301.remoteserver.service.AccountService;
import org.scu301.remoteserver.service.AuthAccountService;
import org.scu301.remoteserver.util.Response;
import org.scu301.remoteserver.util.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            return Response.ok(ok);
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
}

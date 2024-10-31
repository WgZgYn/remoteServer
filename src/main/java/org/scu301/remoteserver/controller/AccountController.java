package org.scu301.remoteserver.controller;

import org.scu301.remoteserver.dto.LoginRequest;
import org.scu301.remoteserver.dto.LoginResponse;
import org.scu301.remoteserver.dto.SignupRequest;
import org.scu301.remoteserver.service.AccountService;
import org.scu301.remoteserver.service.AuthAccountService;
import org.scu301.remoteserver.util.Response;
import org.scu301.remoteserver.util.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        try {
            LoginResponse ok = authAccountService.authenticate(request.username(), request.password());
            return Response.ok(ok);
        } catch (AuthAccountService.AuthError e) {
            return Response.err(e.getMessage());
        }
    }

    @PostMapping("/signup")
    Result signup(@RequestBody SignupRequest request) {
        boolean ok = accountService.addAccount(request.username(), request.password());
        return ok ? Result.ok() : Result.err("User already exists");
    }
}

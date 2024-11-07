package org.scu301.remoteserver.service;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.scu301.remoteserver.dto.AccountUpdateRequest;
import org.scu301.remoteserver.dto.LoginResponse;
import org.scu301.remoteserver.entity.Account;
import org.scu301.remoteserver.security.Argon2Utils;
import org.scu301.remoteserver.security.Claims;
import org.scu301.remoteserver.security.JwtUtils;
import org.scu301.remoteserver.util.Pair;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;


@Slf4j
@Service
public class AuthAccountService {
    public static class AuthError extends Throwable {
        ErrorCode code;
        AuthError(ErrorCode code) {
            this.code = code;
        }
        @Override
        public String getMessage() {
            return code.getMessage();
        }
    }

    @Getter
    public enum ErrorCode {
        USER_NOT_FOUND(500, "User not found"),
        PASSWORD_ERROR(501, "Password error");

        private final int code;
        private final String message;

        ErrorCode(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    private final DataBaseReadService dbReadService;
    private final DataBaseWriteService dbWriteService;

    AuthAccountService(DataBaseReadService dbReadService, DataBaseWriteService dbWriteService) {
        this.dbReadService = dbReadService;
        this.dbWriteService = dbWriteService;
    }


    public LoginResponse authenticate(String username, String password) throws AuthError {
        Optional<Account> user = dbReadService.getAccount(username);
        if (user.isPresent()) {
            Account account = user.get();
            String passwordHash = account.getPasswordHash();
            if (Argon2Utils.verify(passwordHash, password.toCharArray())) {
                // 创建 Claims
                account.setLastLogin(Instant.now());
                dbWriteService.saveAccount(account);
                log.info("Authenticated user {}, update lastLogin", username);
                Claims claims = new Claims(account.getId(), username, account.getRole(), System.currentTimeMillis());
                // 生成 JWT
                String token = JwtUtils.createToken(claims);
                return new LoginResponse(token, claims.role());
            }
            throw new AuthError(ErrorCode.PASSWORD_ERROR);
        }
        throw new AuthError(ErrorCode.USER_NOT_FOUND);
    }

    public boolean updateInfo(AccountUpdateRequest request) {
        String username = request.username();
        String password = request.password();

        Optional<Account> user = dbReadService.getAccount(username);
        if (user.isPresent()) {
            Account account = user.get();
            String passwordHash = account.getPasswordHash();
            if (Argon2Utils.verify(passwordHash, password.toCharArray())) {
                Pair<String, byte[]> newAuth = Argon2Utils.encrypt(request.newPassword().toCharArray());

                account.setUsername(request.newUsername());
                account.setSalt(new String(newAuth.value()));
                account.setPasswordHash(newAuth.key());

                dbWriteService.saveAccount(account);
                return true;
            }
            return false;
        }
        return false;
    }
}

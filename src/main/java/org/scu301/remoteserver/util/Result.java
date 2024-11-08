package org.scu301.remoteserver.util;

import lombok.Data;

import java.util.Optional;

@Data
public class Result {
    enum Code {
        Ok(200),
        Err(500);
        int code;

        Code(int code) {
            this.code = code;
        }
    }

    protected int code;
    protected String message;
    protected long timestamp;

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public static Result ok() {
        return Result.ok("success");
    }

    public static Result ok(String message) {
        return new Result(200, message);
    }

    public static Result err() {
        return Result.err("error");
    }

    public static Result err(String message) {
        return new Result(500, message);
    }

    public static <T> Result of(Optional<T> data) {
        if (data.isPresent()) {
            return Response.of(data.get());
        }
        return Result.err();
    }

    public static <T> Result of(Optional<T> data, String errorMsg) {
        if (data.isPresent()) {
            return Response.of(data.get());
        }
        return Result.err();
    }

    public static Result of(boolean ok, String elseMessage) {
        if (ok) return Result.ok();
        return err(elseMessage);
    }

    public interface Runnable {
        void run() throws Exception;
    }

    public static Result of(Runnable runnable) {
        try {
            runnable.run();
            return Result.ok();
        } catch (Exception e) {
            return Result.err(e.getMessage());
        }
    }


    public static class Builder {
        Result result = Result.ok();
        public static Builder ok() {
            return new Builder();
        }
        public Builder then(Runnable runnable) {
            try {
                runnable.run();
                this.result = Result.ok();
            } catch (Exception e) {
                this.result = Result.err(e.getMessage());
            }
            return this;
        }
        public Result build() {
            return result;
        }
    }
}

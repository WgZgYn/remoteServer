package org.scu301.remoteserver.util;

import lombok.Data;

import java.util.Optional;

@Data
public class Result {
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

    public static<T> Result of(Optional<T> data) {
        if (data.isPresent()) {
            return Response.of(data.get());
        }
        return Result.err();
    }

    public static<T> Result of(Optional<T> data, String errorMsg) {
        if (data.isPresent()) {
            return Response.of(data.get());
        }
        return Result.err();
    }

    public static Result of(boolean ok, String elseMessage) {
        if (ok) return Result.ok();
        return err(elseMessage);
    }
}

package org.scu301.remoteserver.util;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class Response<T> extends Result {
    private final T data;

    public Response(int code, String message, T data) {
        super(code, message);
        this.data = data;
    }


    public static <T> Response<T> of(T data) {
        return new Response<>(200, "success", data);
    }
}

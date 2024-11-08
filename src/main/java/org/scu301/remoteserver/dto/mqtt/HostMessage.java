package org.scu301.remoteserver.dto.mqtt;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

@Slf4j
public class HostMessage {
    private static final Charset defaultCharset = Charset.defaultCharset();
    private static final int MAX_URL_LENGTH = Byte.MAX_VALUE;
    public enum ContentType {
        None,
        Text,
        Json,
        File,
    }

    private final ContentType contentType;
    private final int contentLength;
    private final byte serviceLength;
    private final byte[] serviceName;
    private final byte[] body;

    private HostMessage(byte[] serviceName, ContentType contentType, byte[] body) {
        if (serviceName.length >= MAX_URL_LENGTH) {
            throw new IllegalArgumentException("Service name too long");
        }
        this.serviceLength = (byte) (serviceName.length + 1);
        this.serviceName = serviceName;
        this.contentType = contentType;
        this.contentLength = body.length + 1;
        this.body = body;

        log.info(new String(serviceName));
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(1 + serviceLength + 1 + 1 + 4 + contentLength);
        buffer.put((byte) contentType.ordinal());
        buffer.putInt(contentLength);
        buffer.put(serviceLength); // The length content the \0
        buffer.put(serviceName);
        buffer.put((byte) '\0'); // c str padding
        buffer.put(body);
        buffer.put((byte) '\0'); // c str padding

        // Just For Test
        {
            byte[] data = buffer.array();
            for (byte b : data) {
                System.out.print(b);
                System.out.print(' ');
            }
            System.out.println();
        }

        return buffer.array();
    }

    @Contract("_ -> new")
    public static @NotNull HostMessage empty(@NotNull String serviceName) throws IOException {
        return new HostMessage(serviceName.getBytes(defaultCharset), ContentType.None, new byte[0]);
    }

    @Contract("_, _ -> new")
    public static @NotNull HostMessage fromText(@NotNull String serviceName, @NotNull String bytes) throws IOException {
        return new HostMessage(serviceName.getBytes(defaultCharset), ContentType.Text, bytes.getBytes(defaultCharset));
    }

    @Contract("_, _ -> new")
    public static @NotNull HostMessage fromJson(@NotNull String serviceName, @NotNull JsonNode node) throws IOException {
        return new HostMessage(serviceName.getBytes(defaultCharset), ContentType.Json, node.binaryValue());
    }

    @Contract("_, _ -> new")
    public static @NotNull HostMessage fromFile(@NotNull String serviceName, @NotNull MultipartFile file) throws IOException {
        return new HostMessage(serviceName.getBytes(defaultCharset), ContentType.Json, file.getBytes());
    }
}

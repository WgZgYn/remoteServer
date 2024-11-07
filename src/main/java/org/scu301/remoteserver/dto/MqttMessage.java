package org.scu301.remoteserver.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.nio.charset.Charset;

public class MqttMessage {
    private static final Charset defaultCharset = Charset.defaultCharset();
    public enum ContentType {
        Text,
        Json,
        File,
    }

    final byte[] data;
    private MqttMessage(final byte[] data) {
        this.data = data;
    }


    public static MqttMessage fromText(byte[] data) {
        return new MqttMessage(data);
    }

    public static MqttMessage fromText(String data) {
        return new MqttMessage(data.getBytes());
    }

    public static MqttMessage fromJson(String json) {
        return fromText(json);
    }

    public static MqttMessage fromJson(JsonNode json) {
        return fromJson(json.toString());
    }


//    public static MqttMessage fromFile(File file) {
//        file.
//    }

    public byte[] getData() {
        return data;
    }
}

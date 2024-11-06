package org.scu301.remoteserver.event.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class DeviceMqttMessage {
    @JsonProperty("efuse_mac")
    String eFuseMac;
    @JsonProperty("model_id")
    Integer modelId;
    @JsonProperty("type_id")
    Integer typeId;
    String type;
    JsonNode payload;
}

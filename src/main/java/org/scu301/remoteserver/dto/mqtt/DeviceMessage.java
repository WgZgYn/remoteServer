package org.scu301.remoteserver.dto.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class DeviceMessage {
    @JsonProperty("efuse_mac")
    String eFuseMac;

    @JsonProperty("model_id")
    Integer modelId;

    @JsonProperty("type_id")
    Integer typeId;

    @JsonProperty("model_name")
    String modelName;

    String type;
    JsonNode payload;
}

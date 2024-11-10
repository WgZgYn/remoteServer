package org.scu301.remoteserver.config;

import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mqtt")
public class MqttConfig {
    private String deviceList;
    private String deviceEvent;
    private String broker;
//    private static final String clientId = "remote-server-java";
    private static final String clientId = MqttClient.generateClientId();
    public String getClientId() {
        return clientId;
    }
}

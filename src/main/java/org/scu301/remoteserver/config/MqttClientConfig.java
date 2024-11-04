package org.scu301.remoteserver.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MqttClientConfig {
    private final MqttConfig config;

    MqttClientConfig(MqttConfig config) {
        this.config = config;
    }

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(config.getClientId());
        options.setServerURIs(new String[]{config.getBroker()});
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        options.setKeepAliveInterval(60);
        options.setConnectionTimeout(10);
        return options;
    }

    @Bean
    public MqttClient mqttClient() throws MqttException {
        return new MqttClient(config.getBroker(), config.getClientId(), new MemoryPersistence());
    }
}

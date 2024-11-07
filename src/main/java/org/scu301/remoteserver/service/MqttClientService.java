package org.scu301.remoteserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.scu301.remoteserver.dto.mqtt.HostMessage;
import org.scu301.remoteserver.dto.mqtt.DeviceMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;

/**
 * Mqtt服务
 */
@Slf4j
@Service
public class MqttClientService {
    private final MqttClient client;
    private final MqttConnectOptions options;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    MqttClientService(MqttConnectOptions options, MqttClient client, ObjectMapper objectMapper, ApplicationEventPublisher applicationEventPublisher) {
        this.options = options;
        this.client = client;
        this.objectMapper = objectMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostConstruct
    void init() {
        log.info("init mqtt client");
        try {
            client.setCallback(new Callback());
            client.connect(options);
            setSubscribedTopics();
            log.info("mqtt client init finished");
        } catch (MqttException e) {
            log.error("mqtt client init error", e);
        }
    }

    void setSubscribedTopics() throws MqttException {
        client.subscribe("/status", 1);
        client.subscribe("/events", 2);
    }

    public void disconnect() throws MqttException {
        if (client.isConnected()) {
            client.disconnect();
        }
    }

    public void publish(String topic, @NotNull HostMessage message, int qos, boolean retain) throws MqttException {
        client.publish(topic, message.toBytes(), qos, retain);
    }

    public void publish(String topic, String message) throws MqttException {
        publish(topic, message, 2);
    }

    public void publish(String topic, @NotNull String message, int qos) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(qos);
        client.publish(topic, mqttMessage);
    }

    public void subscribe(String topic) throws MqttException {
        client.subscribe(topic, 2);
    }

    class Callback implements MqttCallback, MqttCallbackExtended {
        @Override
        public void connectionLost(Throwable throwable) {
            log.info("MqttClient Connection lost");
        }

        @Override
        public void messageArrived(String topic, @NotNull MqttMessage mqttMessage) {
            log.info("HostMessage Arrived: topic: {}, {}", topic, mqttMessage);
            try {
                DeviceMessage msg = objectMapper.readValue(mqttMessage.getPayload(), DeviceMessage.class);
                applicationEventPublisher.publishEvent(msg);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            log.info("Delivery complete");
        }

        @Override
        public void connectComplete(boolean b, String s) {
            try {
                setSubscribedTopics();
                log.info("Reconnect and subscribe success");
            } catch (MqttException e) {
                log.error(e.getMessage());
            }
        }
    }
}

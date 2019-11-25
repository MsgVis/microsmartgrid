package com.microsmartgrid.database.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;

import java.util.concurrent.TimeUnit;


public class LocalMqttAsyncClient implements MqttCallback {
	private static final Logger logger = LogManager.getLogger(LocalMqttAsyncClient.class.getName());
	private static MqttAsyncClient mqtt_client = null;

	public LocalMqttAsyncClient() {
	}

	public void init(String serverURI) {
		try {
			this.mqtt_client = new MqttAsyncClient(serverURI, MqttAsyncClient.generateClientId());
		} catch (MqttException e) {
			logger.fatal("Mqtt Client could not be started.\n"
				+ "Reason: " + e.toString() + ", " + e.getCause() + "\n"
				+ "Details: " + e.getMessage() + "\n"
				+ "Aborting.");
			System.exit(e.getReasonCode());
		}
	}

	public void connect() throws NullPointerException {
		if (this.mqtt_client == null) {
			throw new NullPointerException("mqtt client must be initialised before it can be connected.");
		}
		MqttConnectOptions options = new MqttConnectOptions();
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		try {
			logger.info("Connecting to server. Waiting for completion...");
			IMqttToken connect_token = this.mqtt_client.connect(options);
			connect_token.waitForCompletion();
			logger.info("Connection successful.");
		} catch (MqttSecurityException e) {
			logger.error(e.toString() + "occurred, due to " + e.getCause());
		} catch (MqttException e) {
			logger.error("Was unable to connect to server.");
			e.printStackTrace();
		}
	}

	public void standardSubscribe(String topic) {
		if (this.mqtt_client == null || !this.mqtt_client.isConnected()) {
			throw new RuntimeException("mqtt client must be initialised" +
				" and connected before a subscription" +
				" can be performed.");
		}
		try {
			logger.info("Subscribing to " + topic + "at QoS 2.\nWaiting for subscription to finish.");
			IMqttToken subToken = this.mqtt_client.subscribe(topic, 2, null, null);
			subToken.waitForCompletion();
			logger.info("Subsciption successful.");
		} catch (MqttException e) {
			logger.error("Subscription failed. Aborting.");
			e.printStackTrace();
			System.exit(e.getReasonCode());
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		//TODO
	}

	@Override
	public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
		//TODO
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		//intentionally left blank - this client does not publish
		logger.fatal("This program should have never gone here..." +
			"Whats wrong with you?!");
		logger.fatal("No, seriously... This MQTT client is not intended to" +
			"broadcast. Please use another client to publish messages. Aborting.");
		System.exit(42); //<- if anyone ever lands here the person sure knows the answer to everything
	}
}

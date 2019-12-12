package com.microsmartgrid.database.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;


public class LocalMqttAsyncClient {
	private static final Logger logger = LogManager.getLogger();
	private static MqttAsyncClient mqtt_client = null;

	public LocalMqttAsyncClient() {
	}

	public void init(String serverURI) {
		try {
			logger.info("Setting up Client...");
			mqtt_client = new MqttAsyncClient(serverURI, "com-microsmartgrid-database");
			mqtt_client.setCallback(new LocalMqttCallback());
			logger.info("Setup successful.");
		} catch (MqttException e) {
			logger.fatal("Mqtt Client could not be started.\n"
				+ "Reason: " + e.toString() + ", " + e.getCause() + "\n"
				+ "Details: " + e.getMessage() + "\n"
				+ "Aborting.");
			throw new RuntimeException(e);
		}
	}

	public void connect(int... timeout) throws NullPointerException, RuntimeException {
		if (mqtt_client == null) {
			throw new NullPointerException("mqtt client must be initialised before it can be connected.");
		}
		if (mqtt_client.isConnected()) {
			logger.info("Client already connected to " + mqtt_client.getCurrentServerURI());
			return;
		}
		MqttConnectOptions options = new MqttConnectOptions();
		options.setConnectionTimeout(timeout.length > 0 ? timeout[0] : 30);
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		try {
			logger.info("Connecting to server. Waiting for completion...");
			IMqttToken con_token = mqtt_client.connect(options);
			con_token.waitForCompletion();
			logger.info("Connection successful.");
		} catch (MqttSecurityException e) {
			logger.error(e.toString() + "occurred, due to " + e.getCause());
		} catch (MqttException e) {
			logger.error("Was unable to connect to server.");
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void standardSubscribe(String topic) {
		if (mqtt_client == null || !mqtt_client.isConnected()) {
			throw new RuntimeException("mqtt client must be initialised" +
				" and connected before a subscription" +
				" can be performed.");
		}
		try {
			logger.info("Subscribing to \"" + topic + "\" at QoS 0. Waiting for subscription to finish.");
			IMqttToken sub_Token = mqtt_client.subscribe(topic, 0, null, null);
			sub_Token.waitForCompletion();
			logger.info("Subscription successful.");
		} catch (MqttException e) {
			logger.error("Subscription failed. Aborting.");
			e.printStackTrace();
			System.exit(e.getReasonCode());
		}
	}

	public boolean isConnected() {
		return mqtt_client.isConnected();
	}

	public void disconnect() {
		if (!mqtt_client.isConnected()) {
			logger.info("Client was not connected.");
		}
		try {
			mqtt_client.disconnect();
		} catch (MqttException e) {
			logger.error("Disconnection failed.");
			logger.error(e.getCause());
			e.printStackTrace();
			logger.error("Continuing without disconnection.");
		}
	}
}

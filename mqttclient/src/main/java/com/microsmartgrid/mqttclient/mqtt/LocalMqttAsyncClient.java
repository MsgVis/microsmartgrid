package com.microsmartgrid.mqttclient.mqtt;

import lombok.extern.log4j.Log4j2;
import org.eclipse.paho.client.mqttv3.*;

@Log4j2
public class LocalMqttAsyncClient {
	private static MqttAsyncClient mqtt_client = null;

	public LocalMqttAsyncClient() {
	}

	public void init(String serverURI) {
		try {
			log.info("Setting up Client...");
			mqtt_client = new MqttAsyncClient(serverURI, "com-microsmartgrid-mqttclient");
			mqtt_client.setCallback(new LocalMqttCallback());
			log.info("Setup successful.");
		} catch (MqttException e) {
			log.fatal("Mqtt Client could not be started.\n"
				+ "Reason: " + e.toString() + ", " + e.getCause() + "\n"
				+ "Details: " + e.getMessage() + "\n"
				+ "Aborting.");
			throw new RuntimeException(e);
		}
	}

	public void connect(int... timeout) throws RuntimeException {
		if (mqtt_client == null) {
			throw new NullPointerException("mqtt client must be initialised before it can be connected.");
		}
		if (mqtt_client.isConnected()) {
			log.info("Client already connected to " + mqtt_client.getCurrentServerURI());
			return;
		}
		MqttConnectOptions options = new MqttConnectOptions();
		options.setConnectionTimeout(timeout.length > 0 ? timeout[0] : 30);
		options.setAutomaticReconnect(true);
		options.setCleanSession(true);
		try {
			log.info("Connecting to server. Waiting for completion...");
			IMqttToken con_token = mqtt_client.connect(options);
			con_token.waitForCompletion();
			log.info("Connection successful.");
		} catch (MqttSecurityException e) {
			log.error(e.toString() + "occurred, due to " + e.getCause());
		} catch (MqttException e) {
			log.error("Was unable to connect to server.");
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
			log.info("Subscribing to \"" + topic + "\" at QoS 0. Waiting for subscription to finish.");
			IMqttToken sub_Token = mqtt_client.subscribe(topic, 0, null, null);
			sub_Token.waitForCompletion();
			log.info("Subscription successful.");
		} catch (MqttException e) {
			log.error("Subscription failed. Aborting.");
			e.printStackTrace();
			System.exit(e.getReasonCode());
		}
	}

	public boolean isConnected() {
		return mqtt_client.isConnected();
	}

	public void disconnect() {
		if (!mqtt_client.isConnected()) {
			log.info("Client was not connected.");
		}
		try {
			mqtt_client.disconnect();
		} catch (MqttException e) {
			log.error("Disconnection failed.");
			log.error(e.getCause());
			e.printStackTrace();
			log.error("Continuing without disconnection.");
		}
	}
}

package com.microsmartgrid.database.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import java.util.concurrent.TimeUnit;


public class LocalMqttAsyncClient {
	private static final Logger logger = LogManager.getLogger(LocalMqttAsyncClient.class.getName());
	private static MqttAsyncClient mqtt_client;

	public LocalMqttAsyncClient() {
	}

	//TODO: Use Tokens in all methods to enforce synchronous setup
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

	public void connect() {
		int attempt_counter = 0;
		try {
			while (!this.mqtt_client.isConnected()) {
				this.mqtt_client.connect();
			}
		} catch (MqttSecurityException e) {
			logger.error(e.toString() + "occurred, due to " + e.getCause());
		} catch (MqttException e) {
			if (attempt_counter != 10) {
				logger.error("Was unable to connect to server. Trying again...");
				try {
					TimeUnit.SECONDS.sleep(attempt_counter);
				} catch (InterruptedException ex) {
					logger.error("An error occured while waiting: " + e.getCause());
				}
			} else {
				logger.fatal("Connection not possible. Aborting");
				System.exit(e.getReasonCode());
			}
		}
	}
	/*
	TODO:
	public void standardSubscribe() {

	}
*/
}

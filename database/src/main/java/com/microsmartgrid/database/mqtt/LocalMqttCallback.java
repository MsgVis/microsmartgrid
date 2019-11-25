package com.microsmartgrid.database.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class LocalMqttCallback implements MqttCallback {
	private static final Logger logger = LogManager.getLogger(LocalMqttCallback.class.getName());

	/**
	 * As explained here: https://stackoverflow.com/questions/1268817/create-new-object-from-a-string-in-java
	 * No checking for
	 *
	 * @param class_name - the name of the
	 * @throws ClassNotFoundException
	 */
	public void createClassFromString(String class_name) throws ClassNotFoundException {
		//TODO
	}

	@Override
	public void connectionLost(Throwable cause) {
		//TODO
	}

	@Override
	public void messageArrived(String topic_name, MqttMessage mqttMessage) throws Exception {
		try {
			createClassFromString(topic_name);
		} catch (Exception e) { //TODO appropriate choice of exceptions
			//TODO logging
		}
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

package com.microsmartgrid.database.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microsmartgrid.database.dbDataStructures.AbstractDevice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;

import static com.microsmartgrid.database.HelperFunctions.deserializeJson;
import static com.microsmartgrid.database.HelperFunctions.getClassFromIdentifier;

public class LocalMqttCallback implements MqttCallback {
	private static final Logger logger = LogManager.getLogger(LocalMqttCallback.class);

	@Override
	public void connectionLost(Throwable cause) {
		//TODO
	}

	@Override
	public void messageArrived(String topic_name, MqttMessage mqttMessage) throws IOException {
		Class<? extends AbstractDevice> cls = getClassFromIdentifier(topic_name);
		try {
			deserializeJson(mqttMessage.toString(), topic_name, cls);
		} catch (JsonProcessingException e) {
			logger.error("Couldn't construct instance from topic " + topic_name);
			logger.error(e);
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

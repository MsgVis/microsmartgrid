package com.microsmartgrid.database.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.microsmartgrid.database.dbDataStructures.AbstractDevice;
import com.microsmartgrid.database.dbcom.DbWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.Type;

public class LocalMqttCallback implements MqttCallback {
	private static final Logger logger = LogManager.getLogger(LocalMqttCallback.class.getName());

	/**
	 * @param class_name - Snippet of topic for which class names are defined
	 */
	public Class getClassFromTopic(String class_name) {
		Class clazz;
		// TODO: split topic_name to into parts so we can determine the appropriate class to deserialize to
		try {
			clazz = Class.forName(class_name);
		} catch(ClassNotFoundException ex) {
			// log error
			// use default class to save fields in 'meta_information' and create an 'id', 'timestamp', and a corresponding 'DeviceInformation'
			clazz = AbstractDevice.class;
		}
		return clazz;
	}

	@Override
	public void connectionLost(Throwable cause) {
		//TODO
	}

	@Override
	public void messageArrived(String topic_name, MqttMessage mqttMessage) throws Exception {
		Class clazz = getClassFromTopic(topic_name);
		try {
			DbWriter.deserializeJson(mqttMessage.toString(), clazz);
		} catch (JsonProcessingException e) {
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

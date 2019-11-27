package com.microsmartgrid.database.mqtt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.microsmartgrid.database.dbDataStructures.AbstractDevice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.lang.reflect.Type;

public class LocalMqttCallback implements MqttCallback {
	private static final Logger logger = LogManager.getLogger(LocalMqttCallback.class.getName());

	/**
	 * @param json - Json to be deserialized
	 * @param class_name - Snippet of topic for which class names are defined
	 */
	// TODO: MOVE THIS @dustin
	public <T extends AbstractDevice> T createObjectFromJson(String json, String class_name) throws JsonParseException {
		// TODO: configure gson
		Gson gson = new Gson();
		Class<?> clazz;
		try {
			clazz = Class.forName(class_name);
		} catch(ClassNotFoundException ex) {
			// log error
			// use default class to save fields in 'meta_information' and create an 'id', 'timestamp', and a corresponding 'DeviceInformation'
			clazz = AbstractDevice.class;
		}

		// TODO: check 'Device_Information' for existing 'device' and 'name' and create 'DeviceInformation' object for new devices

		// create object from json TODO: handle unchecked cast
		return gson.fromJson(json, (Class<T>) clazz);
	}

	@Override
	public void connectionLost(Throwable cause) {
		//TODO
	}

	@Override
	public void messageArrived(String topic_name, MqttMessage mqttMessage) throws Exception {
		try {
			// TODO: split topic_name to into parts so we can determine the appropriate class to deserialize to
			createObjectFromJson(mqttMessage.toString(), topic_name);
		} catch (JsonParseException e) {
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

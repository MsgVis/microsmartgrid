package com.microsmartgrid.database.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microsmartgrid.database.Device;
import com.microsmartgrid.database.dbDataStructures.AbstractDevice;
import com.microsmartgrid.database.dbcom.DbWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class LocalMqttCallback implements MqttCallback {
	private static final Logger logger = LogManager.getLogger(LocalMqttCallback.class.getName());

	/**
	 * @param class_name - Snippet of topic for which class names are defined
	 */
	public Class<? extends AbstractDevice> getClassFromTopic(String class_name) {
		Class<? extends AbstractDevice> cls;
		// TODO: Replace with configuration file to map classes
		final String package_start = "com.microsmartgrid.database.dbDataStructures.";
		String[] cls_parts = class_name.split("/");
		class_name = package_start + cls_parts[0] + "." +
			(cls_parts[1].split("_").length > 1
				? cls_parts[1].split("_")[0]
				: cls_parts[1].split("-")[0]);
		try {
			cls = Class.forName(class_name).asSubclass(AbstractDevice.class);
		} catch (ClassNotFoundException ex) {
			logger.warn(class_name + " is not yet implemented. The information will be saved to a json.");
			// use default class to save fields in 'meta_information'
			cls = Device.class;
		}
		return cls;
	}

	@Override
	public void connectionLost(Throwable cause) {
		//TODO
	}

	@Override
	public void messageArrived(String topic_name, MqttMessage mqttMessage) {
		Class<? extends AbstractDevice> cls = getClassFromTopic(topic_name);
		try {
			DbWriter.deserializeJson(mqttMessage.toString(), topic_name, cls);
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

package com.microsmartgrid.mqttclient.mqtt;

import com.fasterxml.jackson.databind.node.TextNode;
import com.microsmartgrid.mqttclient.WritingService;
import feign.FeignException;
import javassist.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;

@Log4j2
public class LocalMqttCallback implements MqttCallback {

	@Override
	public void connectionLost(Throwable cause) {
		log.info(cause.toString() + ". Automatically reconnecting...");
	}

	@Override
	public void messageArrived(String topic_name, MqttMessage mqttMessage) throws IOException {
		try {
			WritingService.getDataBaseWriter().writeDeviceToDatabase(topic_name, new TextNode(mqttMessage.toString()));
		} catch (NotFoundException | FeignException e) {
			// If a message caused an internal server error, skip it
			log.warn(e);
		}
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		//intentionally left blank - this client does not publish
		log.fatal("This program should have never gone here..." +
			"Whats wrong with you?!");
		log.fatal("No, seriously... This MQTT client is not intended to" +
			"broadcast. Please use another client to publish messages. Aborting.");
		System.exit(42); //<- if anyone ever lands here the person sure knows the answer to everything
	}
}

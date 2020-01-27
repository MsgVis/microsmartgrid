package com.microsmartgrid.mqttclient.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@EnableFeignClients
@RestController
public class LocalMqttCallback implements MqttCallback {

	@Autowired
	DatabaseWriter databaseWriter;

	@FeignClient("database")
	interface DatabaseWriter {
		@RequestMapping(value = "/", method = GET)
		public <T extends AbstractDevice> void writeDeviceToDatabase(String topic, T device);
	}

	private static final Logger logger = LogManager.getLogger(LocalMqttCallback.class);

	@Override
	public void connectionLost(Throwable cause) {
		logger.info(cause.toString() + ". Automatically reconnecting...");
	}

	@Override
	public void messageArrived(String topic_name, MqttMessage mqttMessage) throws IOException {
		Class<? extends AbstractDevice> cls = HelperFunctions.getClassFromIdentifier(topic_name);
		try {
			AbstractDevice device = HelperFunctions.deserializeJson(mqttMessage.toString(), topic_name, cls);
			if (device == null) return;
			this.databaseWriter.writeDeviceToDatabase(topic_name, device);
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

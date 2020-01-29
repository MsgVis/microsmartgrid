package com.microsmartgrid.mqttclient.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@EnableFeignClients
@RestController
public class LocalMqttCallback implements MqttCallback {

	private static final Logger logger = LogManager.getLogger(LocalMqttCallback.class);

	@Autowired
	private WritingClient databaseWriter;

	@Override
	public void connectionLost(Throwable cause) {
		logger.info(cause.toString() + ". Automatically reconnecting...");
	}

	@Override
	public void messageArrived(String topic_name, MqttMessage mqttMessage) {
		this.databaseWriter.writeDeviceToDatabase(topic_name, mqttMessage.toString());
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

	@FeignClient("timescaleDbWriter")
	interface WritingClient {
		@RequestMapping(path = "/", method = RequestMethod.POST)
		@ExceptionHandler({IOException.class})
		void writeDeviceToDatabase(@RequestParam("topic") String topic, @RequestParam("json") String json) throws IOException;
	}
}

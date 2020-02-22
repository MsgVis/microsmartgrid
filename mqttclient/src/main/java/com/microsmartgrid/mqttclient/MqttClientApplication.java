package com.microsmartgrid.mqttclient;

import com.microsmartgrid.mqttclient.mqtt.LocalMqttAsyncClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@Log4j2
public class MqttClientApplication extends SpringBootServletInitializer implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MqttClientApplication.class, args);
	}


	/**
	 * @param args - : first: serverURI, second: topic, third: timeout
	 */
	@Override
	public void run(String... args) {
		String msg_serverURI;
		if (args.length < 1 || args[0].isEmpty()) {
			msg_serverURI = "tcp://192.168.121.172";
		} else {
			msg_serverURI = args[0];
		}

		String mqtt_topic;
		if (args.length < 2 || args[1].isBlank()) {
			mqtt_topic = "#";
		} else {
			mqtt_topic = args[1];
		}

		String mqtt_timeout;
		if (args.length < 3 || args[2].isBlank()) {
			mqtt_timeout = "30";
		} else {
			mqtt_timeout = args[2];
		}

		log.info(String.format("Using the server %s with topic %s and timeout %s seconds.", msg_serverURI, mqtt_topic, mqtt_timeout));

		LocalMqttAsyncClient mqtt_client = new LocalMqttAsyncClient();
		mqtt_client.init(msg_serverURI);
		mqtt_client.connect(Integer.parseInt(mqtt_timeout));
		mqtt_client.standardSubscribe(mqtt_topic);
	}
}

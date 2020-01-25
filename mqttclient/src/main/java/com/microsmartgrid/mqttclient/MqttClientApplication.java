package com.microsmartgrid.mqttclient;

import com.microsmartgrid.mqttclient.mqtt.LocalMqttAsyncClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MqttClientApplication implements CommandLineRunner {
	private static final Logger logger = LogManager.getLogger(MqttClientApplication.class);

	private static String msg_serverURI;
	private static String mqtt_topic;
	private static String mqtt_timeout;

	public static void main(String[] args) {
		SpringApplication.run(MqttClientApplication.class, args);
	}


	/**
	 * @param args - : first: serverURI, second: topic, third: timeout
	 */
	@Override
	public void run(String... args) throws Exception {
		if (args.length < 1 || args[0].isEmpty()) msg_serverURI = "tcp://192.168.121.172";
		else msg_serverURI = args[0];
		if (args.length < 2 || args[1].isBlank()) mqtt_topic = "#";
		else mqtt_topic = args[1];
		if (args.length < 3 || args[2].isBlank()) {
			logger.info("No connection timeout was specified. Connecting with timeout 30 seconds.");
			mqtt_timeout = "30";
		} else {
			mqtt_timeout = args[2];
		}

		logger.info(String.format("Using the server %s with topic %s and timeout %s seconds.", msg_serverURI, mqtt_topic, mqtt_timeout));

		LocalMqttAsyncClient mqtt_client = new LocalMqttAsyncClient();
		mqtt_client.init(msg_serverURI);
		mqtt_client.connect(Integer.parseInt(mqtt_timeout));
		mqtt_client.standardSubscribe(mqtt_topic);
	}
}

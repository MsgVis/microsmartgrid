package com.microsmartgrid.database;


import com.microsmartgrid.database.mqtt.LocalMqttAsyncClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Database {
	private static final Logger logger = LogManager.getLogger();
	private static String msg_serverURI;
	private static String mqtt_topic;
	private static String mqtt_timeout;

	/**
	 * @param args - first: serverURI, second: topic, third: timeout
	 */
	public static void main(String[] args) {
		if (args.length == 0 || args[0].isEmpty()) msg_serverURI = "tcp://192.168.121.172";
		else msg_serverURI = args[0];
		if (args.length < 2 || args[1].isBlank()) mqtt_topic = "#";
		else mqtt_topic = args[1];
		if (args.length < 3 || args[2].isBlank()) {
			logger.info("No connection timeout was specified. Connecting with timeout 30.");
			mqtt_timeout = "30";
		} else mqtt_timeout = args[2];

		LocalMqttAsyncClient mqtt_client = new LocalMqttAsyncClient();
		mqtt_client.init(msg_serverURI);
		mqtt_client.connect(Integer.parseInt(mqtt_timeout));
		mqtt_client.standardSubscribe(mqtt_topic);

	}
}

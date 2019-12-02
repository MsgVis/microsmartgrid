package com.microsmartgrid.database;


import com.microsmartgrid.database.mqtt.LocalMqttAsyncClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Database {
	private static final Logger logger = LogManager.getLogger(Database.class.getName());
	private static String msg_serverURI;
	private static String mqtt_topic;

	/**
	 * @param args - first: serverURI, second: topic
	 */
	public static void main(String[] args) {
		if (args.length == 0 || args[0].isEmpty()) msg_serverURI = "tcp://192.168.121.172";
		else msg_serverURI = args[0];
		if (args.length < 2 || args[1].isEmpty()) mqtt_topic = "#";
		else mqtt_topic = args[1];

		LocalMqttAsyncClient mqtt_client = new LocalMqttAsyncClient();
		mqtt_client.init(msg_serverURI);
		mqtt_client.connect();
		mqtt_client.standardSubscribe(mqtt_topic);

	}
}

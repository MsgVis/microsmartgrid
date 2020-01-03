package com.microsmartgrid.database;


import com.microsmartgrid.database.mqtt.LocalMqttAsyncClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Database {
	private static final Logger logger = LogManager.getLogger(Database.class);
	private static String msg_serverURI;
	private static String mqtt_topic;
	private static String mqtt_timeout;

	/**
	 * @param args - first: jdbc database url, second: database username, third: database password,
	 *             fourth: serverURI, fifth: topic, sixth: timeout
	 */
	public static void main(String[] args) {
		if (args.length < 3)
			throw new IllegalArgumentException("Please specify the database url, username, and password.");
		if (args.length < 4 || args[3].isEmpty()) msg_serverURI = "tcp://192.168.121.172";
		else msg_serverURI = args[3];
		if (args.length < 5 || args[4].isBlank()) mqtt_topic = "#";
		else mqtt_topic = args[4];
		if (args.length < 6 || args[5].isBlank()) {
			logger.info("No connection timeout was specified. Connecting with timeout 30 seconds.");
			mqtt_timeout = "30";
		} else {
			mqtt_timeout = args[5];
		}

		logger.info(String.format("Using the server %s with topic %s and timeout %s.", msg_serverURI, mqtt_topic, mqtt_timeout));

		Configurations.setJdbcConfiguration(args[0], args[1], args[2]);

		LocalMqttAsyncClient mqtt_client = new LocalMqttAsyncClient();
		mqtt_client.init(msg_serverURI);
		mqtt_client.connect(Integer.parseInt(mqtt_timeout));
		mqtt_client.standardSubscribe(mqtt_topic);

	}
}

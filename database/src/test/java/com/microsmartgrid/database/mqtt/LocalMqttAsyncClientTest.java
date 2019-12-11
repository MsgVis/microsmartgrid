package com.microsmartgrid.database.mqtt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class LocalMqttAsyncClientTest {
	private static LocalMqttAsyncClient client;

	@BeforeEach
	void setup() {
		client = new LocalMqttAsyncClient();
	}

	@AfterEach
	void cleanup() {
		if (client.isConnected()) {
			client.disconnect();
		}
		client = null;
	}

	@Test
	void testInitNull() {
		assertThrows(RuntimeException.class, () -> client.init(null));
	}

	/*
	@Test
	void testAlreadyConnected() {
		client.init("tcp://mqtt.eclipse.org:1883");
		try {		client.connect();}
		catch (Exception e) {
			System.out.println("\n\n\nExpected Error:\n\n\n\n");
			System.out.println("Cause: " + e.getCause());
			System.out.println("Message: " + e.getMessage());
			e.printStackTrace();
		}
		client.connect();
	}
	*/


	@Test
	void setupSuccessful() {
		client.init("tcp://mqtt.eclipse.org:1883");
		//this.client.init("tcp://192.168.121.172:1883"); if connected to msg-VPN...
		client.connect();
		client.standardSubscribe("bbc/subtitles/notice");
	}
}

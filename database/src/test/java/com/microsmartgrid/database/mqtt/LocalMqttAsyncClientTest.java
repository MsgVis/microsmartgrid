package com.microsmartgrid.database.mqtt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocalMqttAsyncClientTest {
	private static LocalMqttAsyncClient client;

	@BeforeEach
	void setup() {
		this.client = new LocalMqttAsyncClient();
	}

	@AfterEach
	void cleanup() {
		if (this.client.isConnected()) {
			this.client.disconnect();
		}
		this.client = null;
	}

	@Test
	void testInitNull() {
		assertThrows(RuntimeException.class, () -> this.client.init(null));
	}

	@Test
	void testAlreadyConnected() {
		this.client.init("tcp://mqtt.eclipse.org:1883");
		this.client.connect();
		this.client.connect();
	}


	@Test
	void setupSuccessful() {
		client.init("tcp://mqtt.eclipse.org:1883");
		client.connect();
		client.standardSubscribe("#");
	}
}

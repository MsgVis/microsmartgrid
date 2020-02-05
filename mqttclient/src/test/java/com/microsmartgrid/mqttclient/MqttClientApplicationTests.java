package com.microsmartgrid.mqttclient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class MqttClientApplicationTests {

	@Autowired
	private CommandLineRunner clr;

	@Test
	public void testMainRoutine() throws Exception {
		this.clr.run("tcp://mqtt.eclipse.org:1883", "bbc/subtitles/notice");
	}
}

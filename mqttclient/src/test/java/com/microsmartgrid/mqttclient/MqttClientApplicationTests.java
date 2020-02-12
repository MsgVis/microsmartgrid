package com.microsmartgrid.mqttclient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(args = {"tcp://mqtt.eclipse.org:1883", "bbc/subtitles/notice"})
@ExtendWith(SpringExtension.class)
class MqttClientApplicationTests {

	@Test
	public void testCorrectStartAndSubscription() {
		return;
	}
}

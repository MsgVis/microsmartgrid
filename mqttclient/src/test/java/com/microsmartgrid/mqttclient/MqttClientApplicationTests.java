package com.microsmartgrid.mqttclient;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.SocketTimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MqttClientApplicationTests {

	@Test
	@Disabled("Takes too long")
	public void validateSocketTimeout() {
		// Positive test to make sure timout value doesn't affect it
		MqttClientApplication.main(new String[]{"tcp://mqtt.eclipse.org:1883", "bbc/subtitles/notice", "5"});
		// Incorrect endpoint
		RuntimeException e = assertThrows(RuntimeException.class, () -> MqttClientApplication.main(new String[]{"tcp://iot.eclipse.org:1883", "bbc/subtitles/notice", "5"}));
		// Wrapped in RuntimeException -> MqttException -> SocketTimeoutException
		assertEquals(SocketTimeoutException.class, e.getCause().getCause().getClass());
	}

	@Test
	public void testMainRoutine() throws Exception {
		new MqttClientApplication().run("tcp://mqtt.eclipse.org:1883", "bbc/subtitles/notice");
	}
}

package com.microsmartgrid.database;

import org.junit.jupiter.api.Test;

import java.net.SocketTimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DatabaseTests {

	@Test
	void validateSocketTimeout() {
		// Positive test to make sure timout value doesn't affect it
		Database.main(new String[]{"tcp://mqtt.eclipse.org:1883"}, 5);
		// Incorrect endpoint
		RuntimeException e = assertThrows(RuntimeException.class, () -> Database.main(new String[]{"tcp://iot.eclipse.org:1883"}, 5));
		// Wrapped in RuntimeException -> MqttException -> SocketTimeoutException
		assertEquals(SocketTimeoutException.class, e.getCause().getCause().getClass());
	}

	@Test
	void testMainRoutine() {
		Database.main(new String[]{"tcp://mqtt.eclipse.org:1883"});
	}
}

package com.microsmartgrid.database;

import org.junit.jupiter.api.Test;

import java.net.SocketTimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DatabaseTests {

	@Test
	public void validateSocketTimeout() {
		// Positive test to make sure timout value doesn't affect it
		Database.main(new String[]{"jdbc:h2:mem:db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1", "sa", "", "tcp://mqtt.eclipse.org:1883", "bbc/subtitles/notice", "5"});
		// Incorrect endpoint
		RuntimeException e = assertThrows(RuntimeException.class, () -> Database.main(new String[]{"jdbc:h2:mem:db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1", "sa", "", "tcp://iot.eclipse.org:1883", "bbc/subtitles/notice", "5"}));
		// Wrapped in RuntimeException -> MqttException -> SocketTimeoutException
		assertEquals(SocketTimeoutException.class, e.getCause().getCause().getClass());
	}

	@Test
	public void testMainRoutine() {
		Database.main(new String[]{"jdbc:h2:mem:db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1", "sa", "", "tcp://mqtt.eclipse.org:1883", "bbc/subtitles/notice"});
	}
}

package com.microsmartgrid.database;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTests {
	@Test
	void canConnectToMqttPublisher() {
		new Database().main(new String[]{"tcp://iot.eclipse.org:1883"});
	}
}

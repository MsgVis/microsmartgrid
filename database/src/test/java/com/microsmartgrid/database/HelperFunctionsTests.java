package com.microsmartgrid.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microsmartgrid.database.model.AbstractDevice;
import com.microsmartgrid.database.model.DaiSmartGrid.Readings;
import com.microsmartgrid.database.model.Device;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class HelperFunctionsTests {

	@Test
	public void testClassFromNamePositiveMatch() throws IOException {
		assertThat(HelperFunctions.getClassFromIdentifier("DaiSmartGrid/MainMeter")).isSameAs(Readings.class);
	}

	@Test
	public void testClassFromNameNegativeMatch() throws IOException {
		assertThat(HelperFunctions.getClassFromIdentifier("DaiSmartGrid/Weather")).isNotSameAs(Readings.class);
	}

	@Test
	public void testClassFromNameFallback() throws IOException {
		assertThat(HelperFunctions.getClassFromIdentifier("not mapped")).isSameAs(Device.class);
	}

	@Test
	public void testDeserializationNoObject() throws JsonProcessingException {
		assertThat(HelperFunctions.deserializeJson("", Device.class)).isNull();
		assertThat(HelperFunctions.deserializeJson("String", Device.class)).isNull();
		assertThat(HelperFunctions.deserializeJson("[\"array\", \"of\", \"strings\"]", Device.class)).isNull();
		assertThat(HelperFunctions.deserializeJson("[{\"array\": \"object\"}]", Device.class)).isNull();
	}

	@Test
	public void testDeserializationPositive() throws JsonProcessingException {
		AbstractDevice device = HelperFunctions.deserializeJson("{\"Timestamp\": 0, \"unrecognized\": 42}", Device.class);
		assertThat(device).isNotNull();
		assertThat(device.getDeviceInformation()).isNull();
		assertThat(device.getTimestamp()).is(new Condition<>(t -> t == Instant.EPOCH, "epoch"));
		assertThat(device.getMetaInformation().get("unrecognized")).isEqualTo(42);
	}

}

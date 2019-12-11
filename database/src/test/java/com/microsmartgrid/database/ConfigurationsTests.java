package com.microsmartgrid.database;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigurationsTests {

	@Test
	public void checkClassMapExistance() {
		Configurations.retrieveClassMap();
		// just make sure this doesn't produce a RuntimeException
	}

	@Test
	public void checkPropertiesExistance() {
		Configurations.retrieveProperties();
		// just make sure this doesn't produce a RuntimeException
	}

	@Test
	public void checkTestDbConfiguration() {
		Map<String, String> jdbc = Configurations.getJdbcConfiguration("test");
		assertFalse(jdbc.get("url").isBlank());
		assertTrue(jdbc.containsKey("username"));
		assertTrue(jdbc.containsKey("password"));
	}

	@Test
	public void checkProductionDbConfiguration() {
		Map<String, String> jdbc = Configurations.getJdbcConfiguration("prod");
		assertFalse(jdbc.get("url").isBlank());
		assertTrue(jdbc.containsKey("username"));
		assertTrue(jdbc.containsKey("password"));
	}
}

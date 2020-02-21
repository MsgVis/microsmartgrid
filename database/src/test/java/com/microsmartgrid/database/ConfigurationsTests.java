package com.microsmartgrid.database;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class ConfigurationsTests {

	final static String package_start = "com.microsmartgrid.database.model.";

	@Test
	@Order(1)
	public void checkClassMapExistence() throws IOException {
		ArrayList<LinkedHashMap<String, String>> config = Configurations.retrieveClassMap();

		assertThat(config.size()).isGreaterThan(0);
	}

	@Test
	@Order(2)
	public void testValidity() throws IOException {
		ArrayList<LinkedHashMap<String, String>> config = Configurations.retrieveClassMap();

		config.forEach(c -> {
			try {
				Pattern.compile(c.get("regex"));
			} catch (PatternSyntaxException e) {
				fail(e);
			}

			try {
				Class.forName(package_start + c.get("path"));
			} catch (ClassNotFoundException e) {
				fail(package_start + c.get("path") + " class doesn't exist.");
			}
		});
	}

}

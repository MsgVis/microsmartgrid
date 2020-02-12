package com.microsmartgrid.database;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ConfigurationsTests {

	@Test
	public void checkClassMapExistance() throws IOException {
		Configurations.retrieveClassMap();
		// just make sure this doesn't produce a RuntimeException
	}

}

package com.microsmartgrid.database;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectMapperManagerTests {

	@Test
	public void checkYamlMapper() {
		assertThat(ObjectMapperManager.getYmlMapper()).isNotNull();
	}

	@Test
	public void testRegisteredModules() {
		assertThat(ObjectMapperManager.getMapper().getRegisteredModuleIds().size()).isEqualTo(1);
	}
}

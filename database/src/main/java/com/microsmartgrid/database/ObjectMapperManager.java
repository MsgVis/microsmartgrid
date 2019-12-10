package com.microsmartgrid.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class ObjectMapperManager {

	private static ObjectMapper objMapper = configureMapper(new ObjectMapper());
	private static ObjectMapper ymlMapper = new YAMLMapper();

	public static ObjectMapper getMapper() {
		return objMapper;
	}

	public static ObjectMapper getYmlMapper() {
		return ymlMapper;
	}

	private static ObjectMapper configureMapper(ObjectMapper objMapper) {
		objMapper
			.registerModule(new JavaTimeModule());

		return objMapper;
	}
}

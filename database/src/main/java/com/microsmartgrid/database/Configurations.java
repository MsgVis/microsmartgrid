package com.microsmartgrid.database;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.microsmartgrid.database.ObjectMapperManager.getYmlMapper;

public class Configurations {
	private static final Logger logger = LogManager.getLogger();
	private static final String FOLDER_PATH = "src/main/resources/config/";
	private static Map<String, String> dataSource;

	public static ArrayList<LinkedHashMap<String, String>> retrieveClassMap() {
		File classMapFile = new File(FOLDER_PATH + "ClassMap.yml");
		ArrayList<LinkedHashMap<String, String>> classMap;

		try {
			classMap = getYmlMapper().readValue(classMapFile, new TypeReference<>() {
			});
		} catch (IOException e) {
			logger.error("Couldn't read from ClassMap.yml configuration file!");
			throw new RuntimeException(e);
		}
		return classMap;
	}

	/**
	 * @return Map with 'url', 'username', and 'password' as keys
	 */
	public static Map<String, String> getJdbcConfiguration() {
		return dataSource;
	}

	public static void setJdbcConfiguration(String arg0, String arg1, String arg2) {
		dataSource = new HashMap<>();
		dataSource.put("url", arg0);
		dataSource.put("username", arg1);
		dataSource.put("password", arg2);
	}
}

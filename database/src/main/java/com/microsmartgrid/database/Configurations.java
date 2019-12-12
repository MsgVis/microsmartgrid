package com.microsmartgrid.database;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;

import static com.microsmartgrid.database.ObjectMapperManager.getYmlMapper;

public class Configurations {
	private static final Logger logger = LogManager.getLogger();
	private static final String FOLDER_PATH = "src/main/resources/config/";

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

	public static Properties retrieveProperties() {
		Properties props = new Properties();
		// TODO: find a good place to store this external (non-vcs) file
		try (FileInputStream in = new FileInputStream(FOLDER_PATH + "db-config.properties")) {
			props.load(in);
		} catch (IOException e) {
			logger.fatal("Can't load database configuration file from " + FOLDER_PATH + "db-config.properties");
			throw new RuntimeException(e);
		}

		return props;
	}

	/**
	 * @param environment Configuration to environment variables: prod, dev, test
	 * @return Map with 'url', 'username', and 'password' as keys
	 */
	public static HashMap<String, String> getJdbcConfiguration(String environment) {
		Properties props = retrieveProperties();
		HashMap<String, String> confMap = new HashMap<>();
		String start = "jdbc." + (environment != null ? environment + "." : "");

		confMap.put("url", props.getProperty(start + "url"));
		confMap.put("username", props.getProperty(start + "username"));
		confMap.put("password", props.getProperty(start + "password"));

		return confMap;
	}
}

package com.microsmartgrid.database;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Configurations {
	private static final Logger logger = LogManager.getLogger(Configurations.class);

	public static ArrayList<LinkedHashMap<String, String>> retrieveClassMap() throws IOException {
		InputStream classMapFile = Configurations.class.getResourceAsStream("/config/ClassMap.yml");
		ArrayList<LinkedHashMap<String, String>> classMap;

		try {
			classMap = ObjectMapperManager.getYmlMapper().readValue(classMapFile, new TypeReference<>() {
			});
		} catch (IOException e) {
			logger.error("Couldn't read from ClassMap.yml configuration file!");
			e.printStackTrace();
			throw new IOException(e);
		} finally {
			classMapFile.close();
		}
		return classMap;
	}

}

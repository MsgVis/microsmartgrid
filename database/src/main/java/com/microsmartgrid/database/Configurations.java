package com.microsmartgrid.database;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Log4j2
public class Configurations {

	public static ArrayList<LinkedHashMap<String, String>> retrieveClassMap() throws IOException {
		InputStream classMapFile = Configurations.class.getResourceAsStream("/config/ClassMap.yml");
		ArrayList<LinkedHashMap<String, String>> classMap;

		try {
			classMap = ObjectMapperManager.getYmlMapper().readValue(classMapFile, new TypeReference<>() {
			});
		} catch (IOException e) {
			log.error("Couldn't read from ClassMap.yml configuration file!");
			throw new IOException(e);
		} finally {
			classMapFile.close();
		}
		return classMap;
	}

}

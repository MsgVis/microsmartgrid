package com.microsmartgrid.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.model.AbstractDevice;
import com.microsmartgrid.database.model.Device;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

@Log4j2
public class HelperFunctions {

	/**
	 * @param name - Mqtt topic or other String that matches the regular expression
	 *             in the ClassMap.yml config file
	 */
	public static Class<? extends AbstractDevice> getClassFromIdentifier(final String name) throws IOException {
		Class<? extends AbstractDevice> cls;
		String class_name;
		final String package_start = "com.microsmartgrid.database.model.";

		try {
			ArrayList<LinkedHashMap<String, String>> classMaps = Configurations.retrieveClassMap();
			class_name = package_start +
				classMaps.stream()
					.filter(t -> name.matches(t.get("regex")))
					.map(t -> t.get("path"))
					.findFirst().orElseThrow();
		} catch (NoSuchElementException e) {
			// No associated class for the name in ClassMaps.yml
			log.warn(name + " could not be mapped to a class defined in ClassMap.yml;\n" +
				"Defaulting to a standard class in order to save information in json.");
			// use default class to save fields in 'meta_information'
			class_name = package_start + "Device";
		}

		try {
			cls = Class.forName(class_name).asSubclass(AbstractDevice.class);
		} catch (ClassNotFoundException ex) {
			// Associated Class in ClassMaps.yml doesn't exist yet or there is a typo
			log.warn(class_name + " is not yet implemented;\n" +
				"Defaulting to a standard class in order to group information in json.");
			// use default class to save fields in 'meta_information'
			cls = Device.class;
		}
		log.debug("Using " + class_name + " class for topic " + name);
		return cls;
	}

	public static <T extends AbstractDevice> AbstractDevice deserializeJson(String json, Class<T> cls) throws JsonProcessingException, IllegalArgumentException {
		ObjectMapper objMapper = ObjectMapperManager.getMapper();

		T device;

		if (objMapper.canSerialize(cls) && json.startsWith("{")) {
			// create object from json
			log.debug("Deserializing json to class.");
			device = objMapper.readValue(json, cls);
		} else {
			// TODO: figure out a way to handle jsonArrays and single attributes
			log.warn("Input is not a json object.");
			return null;
		}

		return device;
	}
}

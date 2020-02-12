package com.microsmartgrid.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.model.AbstractDevice;
import com.microsmartgrid.database.model.Device;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class HelperFunctions {

	/**
	 * @param name - Mqtt topic or other String that matches the regular expression
	 *             in the ClassMap.yml config file
	 */
	public static Class<? extends AbstractDevice> getClassFromIdentifier(final String name) throws IOException {
		Class<? extends AbstractDevice> cls;
		AtomicReference<String> class_name = new AtomicReference<>();
		final String package_start = "com.microsmartgrid.database.model.";


		ArrayList<LinkedHashMap<String, String>> classMaps = Configurations.retrieveClassMap();
		classMaps.stream()
			.filter(t -> name.matches(t.get("regex")))
			.map(t -> t.get("path"))
			.findFirst().ifPresentOrElse(s -> class_name.set(package_start + s),
			() -> {
				// No associated class for the name in ClassMaps.yml
				log.warn(name + " could not be mapped to a class defined in ClassMap.yml;\n" +
					"Defaulting to a standard class in order to save information in json.");
				class_name.set(package_start + "Device");
			});

		try {
			cls = Class.forName(class_name.get()).asSubclass(AbstractDevice.class);
		} catch (ClassNotFoundException ex) {
			log.warn(class_name + " in ClassMaps.yml doesn't exist yet or there is a typo;\n" +
				"Defaulting to a standard class in order to group information in json.");
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
			device = objMapper.readValue(json, cls);
		} else {
			// TODO: figure out a way to handle jsonArrays and single attributes
			log.warn("Input is not a json object.");
			return null;
		}

		return device;
	}
}

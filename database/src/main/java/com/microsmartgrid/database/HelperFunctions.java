package com.microsmartgrid.database;

import com.microsmartgrid.database.dbDataStructures.AbstractDevice;
import com.microsmartgrid.database.dbDataStructures.Device;
import com.microsmartgrid.database.mqtt.LocalMqttCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

public class HelperFunctions {
	private static final Logger logger = LogManager.getLogger(LocalMqttCallback.class.getName());

	/**
	 * @param name - Mqtt topic or other String that matches the regular expression
	 *             in the ClassMap.yml config file
	 */
	public static Class<? extends AbstractDevice> getClassFromIdentifier(final String name) {
		Class<? extends AbstractDevice> cls;
		String class_name;
		final String package_start = "com.microsmartgrid.database.dbDataStructures.";

		try {
			ArrayList<LinkedHashMap<String, String>> classMaps = Configurations.retrieveClassMap();
			class_name = package_start +
				classMaps.stream()
					.filter(t -> name.matches(t.get("regex")))
					.map(t -> t.get("path"))
					.findFirst().orElseThrow();
		} catch (NoSuchElementException e) {
			// No associated class for the name in ClassMaps.yml
			logger.warn(name + " could not be mapped to a class defined in ClassMap.yml;\n" +
				"Defaulting to a standard class in order to group information in json.");
			// use default class to save fields in 'meta_information'
			class_name = package_start + "Device";
		}


		try {
			cls = Class.forName(class_name).asSubclass(AbstractDevice.class);
		} catch (ClassNotFoundException ex) {
			// Associated Class in ClassMaps.yml doesn't exist yet or there is a typo
			logger.warn(class_name + " is not yet implemented;\n" +
				"Defaulting to a standard class in order to group information in json.");
			// use default class to save fields in 'meta_information'
			cls = Device.class;
		}
		logger.debug("Using " + class_name + " class for topic " + name);
		return cls;
	}
}

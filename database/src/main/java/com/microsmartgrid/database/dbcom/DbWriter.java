package com.microsmartgrid.database.dbcom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.ObjectMapperManager;
import com.microsmartgrid.database.dbDataStructures.Device;

public class DbWriter {

	public static <T extends Device> void deserializeJson(String json, String topic, Class cls) throws JsonProcessingException {
		ObjectMapper objMapper = ObjectMapperManager.getObjectMapper();

		// TODO: infer Class (at least <T extends AbstractDevice>)
		Object device;
		Object deviceInfo;

		// TODO: check 'Device_Information' for existing 'device' and 'name' and create 'DeviceInformation' object for new devices
		deviceInfo = DbReader.getDeviceInfo(topic);

		if (objMapper.canSerialize(cls)) {
			// create object from json
			device = objMapper.readValue(json, cls);
		} else {
			// not a json, throw an exception for now
			throw new UnsupportedOperationException("Input is not a json.");
		}

		writeDeviceToDatabase(deviceInfo, device);
	}

	private static void writeDeviceToDatabase(Object deviceInfo, Object device) {
		// if deviceInfo null, create it first
		// (note: new object probably needs to be updated (flushed) to be able to retrieve generated id)

		// assign id from deviceInfo to device
	}


}

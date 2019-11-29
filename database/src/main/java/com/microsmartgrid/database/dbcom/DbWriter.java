package com.microsmartgrid.database.dbcom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.dbDataStructures.AbstractDevice;

public class DbWriter {

	public static <T extends AbstractDevice> void deserializeJson(String json, String topic, Class clazz) throws JsonProcessingException {
		// TODO: configure objectMapper
		ObjectMapper objMapper = new ObjectMapper();
		objMapper.addMixIn(clazz, objMapper.findMixInClassFor(clazz));

		// TODO: infer Class (at least <T extends AbstractDevice>)
		Object device = null;
		Object deviceInfo = null;

		// TODO: check 'Device_Information' for existing 'device' and 'name' and create 'DeviceInformation' object for new devices
		DbReader.getDeviceInfo(topic);

		if(objMapper.canSerialize(clazz)){
			// create object from json
			device = objMapper.readValue(json, clazz);
		}

		writeToDatabase(deviceInfo, device);
	}

	private static void writeToDatabase(Object deviceInfo, Object device) {

	}


}

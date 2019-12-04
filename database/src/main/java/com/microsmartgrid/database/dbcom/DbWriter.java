package com.microsmartgrid.database.dbcom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.ObjectMapperManager;
import com.microsmartgrid.database.dbDataStructures.AbstractDevice;
import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;

public class DbWriter {

	public static <T extends AbstractDevice> void deserializeJson(String json, String topic, Class<T> cls) throws JsonProcessingException {
		ObjectMapper objMapper = ObjectMapperManager.getObjectMapper();

		T device;
		AdditionalDeviceInformation deviceInfo;

		// TODO: check 'Device_Information' for existing 'device' and 'name' and create 'DeviceInformation' object for new devices
		deviceInfo = DbReader.getDeviceInfo(topic);

		if (objMapper.canSerialize(cls)) {
			// create object from json
			device = objMapper.readValue(json, cls);
		} else {
			// figure out a way to handle jsonArrays and single attributes
			throw new UnsupportedOperationException("Input is not a json.");
		}

		if (deviceInfo == null) {
			// create new additionalDeviceInformation to the corresponding 'device' and save topic to 'name'
			deviceInfo = new AdditionalDeviceInformation(topic, device);
		}

		writeDeviceToDatabase(deviceInfo, device);
	}

	private static <T extends AbstractDevice> void writeDeviceToDatabase(AdditionalDeviceInformation deviceInfo, T device) {
		// if deviceInfo null, create it first
		// (note: new object probably needs to be updated (flushed) to be able to retrieve generated id)

		// assign id from deviceInfo to device
	}


}

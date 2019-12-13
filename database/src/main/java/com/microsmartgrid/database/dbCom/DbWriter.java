package com.microsmartgrid.database.dbCom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.ObjectMapperManager;
import com.microsmartgrid.database.dbDataStructures.AbstractDevice;
import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Readings;

public class DbWriter {

	public static <T extends AbstractDevice> void deserializeJson(String json, String topic, Class<T> cls) throws JsonProcessingException {
		ObjectMapper objMapper = ObjectMapperManager.getMapper();
		DbHandle db = new DbHandle();

		T device;
		AdditionalDeviceInformation deviceInfo;

		deviceInfo = db.queryDevices(topic);

		if (objMapper.canSerialize(cls)) {
			// create object from json
			device = objMapper.readValue(json, cls);
		} else {
			// TODO: figure out a way to handle jsonArrays and single attributes
			throw new UnsupportedOperationException("Input is not a json.");
		}

		if (deviceInfo == null) {
			// create new additionalDeviceInformation to the corresponding device and save topic to 'name'
			deviceInfo = new AdditionalDeviceInformation(topic);
			deviceInfo.setId(db.insertDeviceInfo(deviceInfo));
		}

		writeDeviceToDatabase(deviceInfo.getId(), device);

	}

	private static <T extends AbstractDevice> void writeDeviceToDatabase(int id, T device) {
		device.setId(id);

		DbHandle db = new DbHandle();
		db.insertReadings((Readings) device);
	}
}

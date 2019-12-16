package com.microsmartgrid.database.dbCom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.ObjectMapperManager;
import com.microsmartgrid.database.dbDataStructures.AbstractDevice;
import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Readings;

import static com.microsmartgrid.database.dbCom.DbHandle.*;

public class DbWriter {

	public static <T extends AbstractDevice> void deserializeJson(String json, String topic, Class<T> cls) throws JsonProcessingException {
		ObjectMapper objMapper = ObjectMapperManager.getMapper();

		T device;
		AdditionalDeviceInformation deviceInfo;

		deviceInfo = queryDevices(topic);

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
			deviceInfo.setId(insertDeviceInfo(deviceInfo));
		}

		writeDeviceToDatabase(deviceInfo.getId(), device);

	}

	private static <T extends AbstractDevice> void writeDeviceToDatabase(int id, T device) {
		device.setId(id);

		insertReadings((Readings) device);
	}
}

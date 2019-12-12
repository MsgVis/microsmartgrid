package com.microsmartgrid.database.dbCom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.ObjectMapperManager;
import com.microsmartgrid.database.dbDataStructures.AbstractDevice;
import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Readings;
import java.sql.*;

public class DbWriter {

	public static <T extends AbstractDevice> void deserializeJson(String json, String topic, Class<T> cls) throws JsonProcessingException {
		ObjectMapper objMapper = ObjectMapperManager.getMapper();

		T device;
		AdditionalDeviceInformation deviceInfo;

		// TODO: check 'Device_Information' for existing 'device' and 'name' and create 'DeviceInformation' object for new devices
		DbHandle db = new DbHandle();
		deviceInfo = db.queryDevices(topic);

		if (objMapper.canSerialize(cls)) {
			// create object from json
			device = objMapper.readValue(json, cls);
		} else {
			// figure out a way to handle jsonArrays and single attributes
			throw new UnsupportedOperationException("Input is not a json.");
		}

		if (deviceInfo == null) {
			// create new additionalDeviceInformation to the corresponding 'device' and save topic to 'name'
			deviceInfo = new AdditionalDeviceInformation(topic);
		}

		try{
			writeDeviceToDatabase(deviceInfo, device);
		}catch(SQLException e){
			e.printStackTrace();
		}

	}

	private static <T extends AbstractDevice> void writeDeviceToDatabase(AdditionalDeviceInformation deviceInfo, T device) throws SQLException{
		// TODO: if deviceInfo null, create it first
		// (note: new object probably needs to be updated (flushed) to be able to retrieve generated id)

		// TODO: assign id from deviceInfo to device
		device.setId(deviceInfo.getId());

		DbHandle db = new DbHandle();
		db.insertDeviceInfo(deviceInfo);
		db.insertReadings((Readings) device);
	}
}

package com.microsmartgrid.database.dbCom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.ObjectMapperManager;
import com.microsmartgrid.database.dbDataStructures.AbstractDevice;
import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Battery;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.microsmartgrid.database.dbCom.SqlCommands.CREATE_DEVICE_TABLE;
import static com.microsmartgrid.database.dbCom.SqlCommands.CREATE_READINGS_TABLE;

public class DbWriterTest {

	@AfterAll
	static void cleanUp() {
		DbHandle db = new DbHandle();
		db.execute("DROP ALL OBJECTS;");
	}

	@Test
	void testDeserializeJson() throws ClassNotFoundException, JsonProcessingException {
		Battery bat = new Battery((float) 0.0);
		bat.setTimestamp(Instant.now());

		ObjectMapper objMapper = ObjectMapperManager.getMapper();
		String json = objMapper.writeValueAsString(bat);

		DbHandle db = new DbHandle();
		db.execute(CREATE_DEVICE_TABLE);

		AdditionalDeviceInformation info = new AdditionalDeviceInformation("battery");
		info.setType(AdditionalDeviceInformation.Type.CONSUMER);
		info.setSubtype(AdditionalDeviceInformation.Subtype.BATTERY);
		info.setChildren(new Integer[]{1, 2});
		db.insertDeviceInfo(info);

		db.execute(CREATE_READINGS_TABLE);

		DbWriter.deserializeJson(json, "battery", Battery.class.asSubclass(AbstractDevice.class));
	}

	@Test
	void testDeserializeJsonWithoutDeviceInfo() throws ClassNotFoundException, JsonProcessingException {
		Battery bat = new Battery((float) 0.0);
		bat.setTimestamp(Instant.now());

		ObjectMapper objMapper = ObjectMapperManager.getMapper();
		String json = objMapper.writeValueAsString(bat);

		DbHandle db = new DbHandle();
		db.execute(CREATE_DEVICE_TABLE);

		db.execute(CREATE_READINGS_TABLE);

		DbWriter.deserializeJson(json, "battery", Battery.class.asSubclass(AbstractDevice.class));
	}

}

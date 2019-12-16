package com.microsmartgrid.database.dbCom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.ObjectMapperManager;
import com.microsmartgrid.database.dbDataStructures.AbstractDevice;
import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Battery;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.microsmartgrid.database.Configurations.setJdbcConfiguration;
import static com.microsmartgrid.database.dbCom.DbHandle.execute;
import static com.microsmartgrid.database.dbCom.DbHandle.insertDeviceInfo;
import static com.microsmartgrid.database.dbCom.SqlCommands.CREATE_DEVICE_TABLE;
import static com.microsmartgrid.database.dbCom.SqlCommands.CREATE_READINGS_TABLE;

public class DbWriterTest {

	@AfterAll
	static void cleanUp() {
		execute("DROP ALL OBJECTS;");
	}

	@BeforeEach
	void setup() {
		setJdbcConfiguration("jdbc:h2:mem:db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1", "sa", "");
	}

	@Test
	void testDeserializeJson() throws JsonProcessingException {
		Battery bat = new Battery((float) 0.0);
		bat.setTimestamp(Instant.now());

		ObjectMapper objMapper = ObjectMapperManager.getMapper();
		String json = objMapper.writeValueAsString(bat);

		execute(CREATE_DEVICE_TABLE);

		AdditionalDeviceInformation info = new AdditionalDeviceInformation("battery");
		info.setType(AdditionalDeviceInformation.Type.CONSUMER);
		info.setSubtype(AdditionalDeviceInformation.Subtype.BATTERY);
		info.setChildren(new Integer[]{1, 2});
		insertDeviceInfo(info);

		execute(CREATE_READINGS_TABLE);

		DbWriter.deserializeJson(json, "battery", Battery.class.asSubclass(AbstractDevice.class));
	}

	@Test
	void testDeserializeJsonWithoutDeviceInfo() throws JsonProcessingException {
		Battery bat = new Battery((float) 0.0);
		bat.setTimestamp(Instant.now());

		ObjectMapper objMapper = ObjectMapperManager.getMapper();
		String json = objMapper.writeValueAsString(bat);

		execute(CREATE_DEVICE_TABLE);

		execute(CREATE_READINGS_TABLE);

		DbWriter.deserializeJson(json, "battery", Battery.class.asSubclass(AbstractDevice.class));
	}

}

package com.microsmartgrid.database.dbCom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsmartgrid.database.ObjectMapperManager;
import com.microsmartgrid.database.dbDataStructures.AbstractDevice;
import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Battery;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.time.Instant;

import static com.microsmartgrid.database.HelperFunctions.deserializeJson;
import static com.microsmartgrid.database.dbCom.DbHandle.execute;
import static com.microsmartgrid.database.dbCom.DbWriter.insertDeviceInfo;
import static com.microsmartgrid.database.dbCom.SqlCommands.CREATE_DEVICE_TABLE;
import static com.microsmartgrid.database.dbCom.SqlCommands.CREATE_READINGS_TABLE;

@SpringBootTest
public class DbWriterTest {

	@AfterAll
	public static void cleanUp() throws SQLException {
		execute("DROP ALL OBJECTS;");
	}

	@Test
	public void testDeserializeJson() throws JsonProcessingException, SQLException {
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

		deserializeJson(json, "battery", Battery.class.asSubclass(AbstractDevice.class));
	}

	@Test
	public void testDeserializeJsonWithoutDeviceInfo() throws JsonProcessingException, SQLException {
		Battery bat = new Battery((float) 0.0);
		bat.setTimestamp(Instant.now());

		ObjectMapper objMapper = ObjectMapperManager.getMapper();
		String json = objMapper.writeValueAsString(bat);

		execute(CREATE_DEVICE_TABLE);

		execute(CREATE_READINGS_TABLE);

		deserializeJson(json, "battery", Battery.class.asSubclass(AbstractDevice.class));
	}

}

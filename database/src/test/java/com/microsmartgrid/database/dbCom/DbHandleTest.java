package com.microsmartgrid.database.dbCom;

import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Battery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.time.Instant;

import static com.microsmartgrid.database.dbCom.DbHandle.execute;
import static com.microsmartgrid.database.dbCom.DbReader.queryDevices;
import static com.microsmartgrid.database.dbCom.DbWriter.insertDeviceInfo;
import static com.microsmartgrid.database.dbCom.DbWriter.insertReadings;
import static com.microsmartgrid.database.dbCom.SqlCommands.CREATE_DEVICE_TABLE;
import static com.microsmartgrid.database.dbCom.SqlCommands.CREATE_READINGS_TABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DbHandleTest {

	@AfterEach
	public void cleanUp() throws SQLException {
		execute("DROP ALL OBJECTS;");
	}

	@Test
	@Order(1)
	public void testInsertReadings() throws SQLException {
		// TODO hier wird gar nichts getested? Fehlermeldungen
		execute(CREATE_READINGS_TABLE);

		Battery bat = new Battery((float) 0.0);
		bat.setTimestamp(Instant.now());
		insertReadings(bat);
	}

	@Test
	@Order(2)
	public void testInsertDevicesInfo() throws SQLException {
		execute(CREATE_DEVICE_TABLE);

		AdditionalDeviceInformation info = new AdditionalDeviceInformation("topic");
		info.setType(AdditionalDeviceInformation.Type.CONSUMER);
		info.setSubtype(AdditionalDeviceInformation.Subtype.BATTERY);
		info.setChildren(new Integer[]{1, 2});
		insertDeviceInfo(info);

		AdditionalDeviceInformation queried_info = queryDevices("topic");
		assertEquals("topic", queried_info.getName());
	}
}

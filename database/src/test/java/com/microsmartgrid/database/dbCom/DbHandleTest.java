package com.microsmartgrid.database.dbCom;

import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Battery;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.Instant;

import static com.microsmartgrid.database.dbCom.SqlCommands.CREATE_DEVICE_TABLE;
import static com.microsmartgrid.database.dbCom.SqlCommands.CREATE_READINGS_TABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbHandleTest {

	private static DbHandle db;

	@BeforeEach
	void setup() throws SQLException {
		db = new DbHandle();
	}

	@AfterEach
	void cleanUp() throws SQLException {
		db.execute("DROP ALL OBJECTS;");
	}

	@Test
	@Order(1)
	void testInsertReadings() throws SQLException {
		db.execute(CREATE_READINGS_TABLE);

		Battery bat = new Battery((float) 0.0);
		bat.setTimestamp(Instant.now());
		db.insertReadings(bat);
	}

	@Test
	@Order(2)
	void testInsertDevicesInfo() throws SQLException {
		db.execute(CREATE_DEVICE_TABLE);

		AdditionalDeviceInformation info = new AdditionalDeviceInformation("topic");
		info.setType(AdditionalDeviceInformation.Type.CONSUMER);
		info.setSubtype(AdditionalDeviceInformation.Subtype.BATTERY);
		info.setChildren(new Integer[]{1, 2});
		db.insertDeviceInfo(info);

		AdditionalDeviceInformation queried_info = db.queryDevices("topic");
		assertEquals("topic", queried_info.getName());
	}
}

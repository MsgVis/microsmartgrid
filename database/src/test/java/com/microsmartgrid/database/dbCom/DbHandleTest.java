package com.microsmartgrid.database.dbCom;

import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.*;
import java.time.Instant;

import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Battery;

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
		db.execute(
			"CREATE TABLE readings (" +
				"time TIMESTAMP," +
				"device_id INTEGER," +
				"a_minus REAL," +
				"a_plus REAL," +
				"r_minus REAL," +
				"r_plus REAL," +
				"p_total REAL," +
				"p_r REAL," +
				"p_s REAL," +
				"p_t REAL," +
				"q_total REAL," +
				"q_r REAL," +
				"q_s REAL," +
				"q_t REAL," +
				"s_total REAL," +
				"s_r REAL," +
				"s_s REAL," +
				"s_t REAL," +
				"i_avg REAL," +
				"i_r REAL," +
				"i_s REAL," +
				"i_t REAL," +
				"u_avg REAL," +
				"u_r REAL," +
				"u_s REAL," +
				"u_t REAL," +
				"f REAL," +
				"meta JSON" +
				");");

		Battery bat = new Battery((float) 0.0);
		bat.setTimestamp(Instant.now());
		db.insertReadings(bat);
	}

	@Test
	@Order(2)
	void testInsertDevicesInfo() throws SQLException {
		db.execute(
			"CREATE TABLE devices (" +
				"id INTEGER auto_increment," +
				"name TEXT," +
				"description TEXT," +
				"type TEXT," +
				"subtype TEXT," +
				"children ARRAY" +
				");");

		AdditionalDeviceInformation info = new AdditionalDeviceInformation("topic");
		info.setType(AdditionalDeviceInformation.Type.CONSUMER);
		info.setSubtype(AdditionalDeviceInformation.Subtype.BATTERY);
		info.setChildren(new Integer[0]);
		db.insertDeviceInfo(info);

		AdditionalDeviceInformation queried_info = db.queryDevices("topic");
		assertEquals("topic", queried_info.getName());
	}
}

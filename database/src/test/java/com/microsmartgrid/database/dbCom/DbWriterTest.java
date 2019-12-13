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
		db.execute(
			"CREATE TABLE devices (" +
				"id INTEGER auto_increment," +
				"name TEXT," +
				"description TEXT," +
				"type TEXT," +
				"subtype TEXT," +
				"children ARRAY" +
				");");

		AdditionalDeviceInformation info = new AdditionalDeviceInformation("battery");
		info.setType(AdditionalDeviceInformation.Type.CONSUMER);
		info.setSubtype(AdditionalDeviceInformation.Subtype.BATTERY);
		info.setChildren(new Integer[]{1, 2});
		db.insertDeviceInfo(info);

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

		DbWriter.deserializeJson(json, "battery", Class.forName("com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Battery").asSubclass(AbstractDevice.class));
	}

	@Test
	void testDeserializeJsonWithoutDeviceInfo() throws ClassNotFoundException, JsonProcessingException {
		Battery bat = new Battery((float) 0.0);
		bat.setTimestamp(Instant.now());

		ObjectMapper objMapper = ObjectMapperManager.getMapper();
		String json = objMapper.writeValueAsString(bat);

		DbHandle db = new DbHandle();
		db.execute(
			"CREATE TABLE devices (" +
				"id INTEGER auto_increment," +
				"name TEXT," +
				"description TEXT," +
				"type TEXT," +
				"subtype TEXT," +
				"children ARRAY" +
				");");

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

		DbWriter.deserializeJson(json, "battery", Class.forName("com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Battery").asSubclass(AbstractDevice.class));
	}

}

package com.microsmartgrid.database.dbCom;

import com.microsmartgrid.database.dbDataStructures.AdditionalDeviceInformation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DayOfWeek;
import java.time.Instant;
import java.util.ArrayList;

import com.microsmartgrid.database.dbDataStructures.DaiSmartGrid.Battery;
import com.microsmartgrid.database.dbDataStructures.AbstractDevice;

public class DbHandleTest {

	private static DbHandle db;

	private void printResultSet(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		while (rs.next()) {
			for (int i = 1; i <= columnsNumber; i++) {
				if (i > 1) System.out.print(",  ");
				String columnValue = rs.getString(i);
				System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
			}
			System.out.println();
		}
	}

	@BeforeEach
	void setup() throws SQLException {
		db = new DbHandle();
		db.connect("jdbc:h2:./test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE", "sa", "");
	}

	@AfterEach
	void cleanUp() throws SQLException {
		db.execute("DROP ALL OBJECTS;");
		db.cleanUp();
	}

	@Test
	@Order(1)
	void testConnectTwice() throws SQLException {
		DbHandle handle = new DbHandle();
		handle.connect("jdbc:h2:./test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE", "sa", "");
		assertThrows(IllegalStateException.class, () -> handle.connect("jdbc:h2:./test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE", "sa", ""));
		handle.cleanUp();
	}

	@Test
	@Order(2)
	void testExecuteWithoutConnection() throws SQLException {
		DbHandle handle = new DbHandle();
		assertThrows(NullPointerException.class, () -> handle.execute("CREATE TABLE names (name text);"));
		handle.cleanUp();
	}

	@Test
	@Order(3)
	void testSimpleCreateInsertSelect() throws SQLException {
		db.execute("CREATE TABLE names (name text);");
		db.execute("INSERT INTO names VALUES ('lucca');");
		ResultSet rs = db.executeQuery("SELECT * FROM names;");
		rs.first();
		assertEquals(rs.getString(1), "lucca", "Retrieved value is not equal to inserted value");
	}

	@Test
	@Order(4)
	void testInsertReadings() throws SQLException {
		db.execute(
			"CREATE TABLE readings (\n" +
				"    time\t\tTIMESTAMP,\n" +
				"    device_id\tINTEGER,\n" +
				"\ta_minus\t\tREAL,\n" +
				"\ta_plus\t\tREAL,\n" +
				"\tr_minus \tREAL,\n" +
				"\tr_plus \t\tREAL,\n" +
				"\tp_total \tREAL,\n" +
				"\tp_r\t\t    REAL,\n" +
				"\tp_s\t\t    REAL,\n" +
				"\tp_t\t\t    REAL,\n" +
				"\tq_total\t\tREAL,\n" +
				"\tq_r\t\t    REAL,\n" +
				"\tq_s\t\t    REAL,\n" +
				"\tq_t\t\t    REAL,\n" +
				"\ts_total\t\tREAL,\n" +
				"    s_r\t\t    REAL,\n" +
				"    s_s\t\t    REAL,\n" +
				"    s_t\t\t    REAL,\n" +
				"\ti_avg\t\tREAL,\n" +
				"\ti_r\t\t    REAL,\n" +
				"\ti_s\t\t    REAL,\n" +
				"\ti_t\t\t    REAL,\n" +
				"\tu_avg\t\tREAL,\n" +
				"\tu_r\t\t    REAL,\n" +
				"\tu_s\t\t    REAL,\n" +
				"\tu_t\t\t    REAL,\n" +
				"\tf\t\t    REAL,\n" +
				"\tmeta\t\tJSON\n" +
				");");

		Battery bat = new Battery((float) 0.0);
		bat.setTimestamp(Instant.now());
		db.insertReadings(bat);
	}

	@Test
	@Order(5)
	void testInsertDevicesInfo() throws SQLException {
		db.execute(
			"CREATE TABLE devices (\n" +
				"id INTEGER auto_increment,\n" +
				"\tname\t\tTEXT,\n" +
				"\tdescription\tTEXT,\n" +
				"\ttype\t\tTEXT,\n" +
				"\tsubtype\t\tTEXT,\n" +
				"\tdepth\t\tINTEGER,\n" +
				"\ticon\t\tTEXT,\n" +
				"\tchildren\tARRAY\n" +
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

package com.microsmartgrid.database.dbCom;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
	void testInsertObject() throws SQLException {
		db.execute(
			"CREATE TABLE readings (\n" +
				"    time\t\tTIMESTAMP,\n" +
				"    device_id\tINTEGER\tREFERENCES devices (id),\n" +
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
				"\tmeta\t\tJSON,\n" +
				"\tPRIMARY KEY (time,device_id)\n" +
				");");

		// db.insertObject(new Battery(), "readings");
	}

	@Test
	@Order(5)
	void testInstant() throws SQLException {
		Instant now = Instant.now();
		Timestamp current = Timestamp.from(now);
		System.out.println(now);
		System.out.println(current);
	}
}

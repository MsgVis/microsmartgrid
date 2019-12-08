package com.microsmartgrid.database.dbCom;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DBHandleTest {

	private static DBHandle db;

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
		db = new DBHandle();
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
		DBHandle handle = new DBHandle();
		handle.connect("jdbc:h2:./test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE", "sa", "");
		handle.connect("jdbc:h2:./test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE", "sa", "");
		handle.cleanUp();
	}

	@Test
	@Order(2)
	void testExecuteWithoutConnection() throws SQLException {
		DBHandle handle = new DBHandle();
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
}

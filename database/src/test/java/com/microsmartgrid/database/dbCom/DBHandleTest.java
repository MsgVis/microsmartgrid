package com.microsmartgrid.database.dbCom;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class DBHandleTest {

	private static DBHandle db;

	private void printResultSet(ResultSet rs){
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1) System.out.print(",  ");
					String columnValue = rs.getString(i);
					System.out.print(columnValue + " " + rsmd.getColumnName(i));
				}
				System.out.println("");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@BeforeEach
	void setup() {
		db = new DBHandle();
		db.connect("jdbc:h2:./test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE", "sa", "");
	}

	@AfterEach
	void cleanUp() {
		db.execute("drop all objects;");
		db.cleanUp();
	}

	@Test
	@Order(1)
	void testConnectTwice() throws Exception {
		DBHandle handle = new DBHandle();
		handle.connect("jdbc:h2:./test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE", "sa", "");
		handle.connect("jdbc:h2:./test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE", "sa", "");
		handle.cleanUp();
	}

	@Test
	@Order(2)
	void testExecuteWithoutConnection() throws Exception {
		DBHandle handle = new DBHandle();
		handle.execute("create table msg (name text);");
		handle.cleanUp();
	}

	@Test
	@Order(3)
	void testSimpleCreateInsertSelect(){
		db.execute("create table msg (name text);");
		db.execute("insert into msg values ('lucca');");
		ResultSet rs = db.executeQuery("select * from msg;");
		try{
			rs.next();
			assertEquals(rs.getString(1),"lucca","Retrieved value is not equal to inserted value");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

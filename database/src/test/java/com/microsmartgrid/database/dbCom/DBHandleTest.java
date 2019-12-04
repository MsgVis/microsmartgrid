package com.microsmartgrid.database.dbCom;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class DBHandleTest {

	private static DBHandle db;

	@BeforeEach
	void setup() {
		db = new DBHandle();
		db.connect("jdbc:h2:./test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE", "sa", "");
	}

	@AfterEach
	void cleanup() {
		db.cleanUp();
	}

	@Test
	@Order(1)
	void testConnectTwice() {
		DBHandle handle = new DBHandle();

		handle.connect("jdbc:h2:./test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE", "sa", "");

		handle.cleanUp();
	}
}

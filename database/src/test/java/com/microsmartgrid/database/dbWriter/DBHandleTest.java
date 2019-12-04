package com.microsmartgrid.database.dbWriter;

import org.junit.jupiter.api.Test;

public class DBHandleTest {
	@Test
	void testConnectTwice(){
		DBHandle handle = new DBHandle();
		handle.connect("jdbc:h2:~/test;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE","msg","msg");
		handle.connect("jdbc:h2:mem:testdb","msg","msg");
	}
}

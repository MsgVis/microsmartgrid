package com.microsmartgrid.database.dbWriter;

import org.junit.jupiter.api.Test;

public class DBHandleTest {
	@Test
	void testConnectTwice(){
		DBHandle handle = new DBHandle();
		handle.connect("msg","msg","msg");
		handle.connect("msg","msg","msg");
	}
}

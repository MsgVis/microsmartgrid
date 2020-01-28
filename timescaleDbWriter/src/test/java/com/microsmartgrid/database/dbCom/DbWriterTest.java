package com.microsmartgrid.database.dbCom;

import org.junit.jupiter.api.AfterAll;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static com.microsmartgrid.database.dbCom.DbHandle.execute;

@SpringBootTest
public class DbWriterTest {

	@AfterAll
	public static void cleanUp() throws SQLException {
		execute("DROP ALL OBJECTS;");
	}


}

package com.microsmartgrid.database;

import com.microsmartgrid.database.dbCom.DatabaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ConfigurationsTests {

	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;

	@Test
	public void checkClassMapExistance() throws IOException {
		Configurations.retrieveClassMap();
		// just make sure this doesn't produce a RuntimeException
	}

	@Test
	public void checkTestDbConfiguration() {
		assertTrue(url.startsWith("jdbc:h2:mem:db"));
		assertEquals("sa", username);
		assertTrue(password.isBlank());
	}

	@Test
	public void checkConfigurationFunction() throws SQLException {
		assertNotNull(new DatabaseConfig().getConnection());
	}
}

package com.microsmartgrid.database.dbCom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {

	@Autowired
	private DataSource dataSource;

	public DatabaseConfig() {
	}

	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
}

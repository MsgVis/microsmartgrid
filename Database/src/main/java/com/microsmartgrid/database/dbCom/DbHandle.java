package com.microsmartgrid.database.dbCom;

import com.microsmartgrid.database.Configurations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Connection to PostgreSQL database
 */
public abstract class DbHandle {

	private static final Logger logger = LogManager.getLogger(DbHandle.class);

	/**
	 * Execute SQL command
	 *
	 * @param command
	 */
	public static void execute(String command) {
		try (Connection conn = getConnection();
			 Statement stmt = conn.createStatement()) {
			stmt.execute(command);
		} catch (SQLException e) {
			logger.error("Couldn't excecute: " + command);
			logger.error(e);
		}
	}

	public static Connection getConnection() throws SQLException {
		Map<String, String> cfg = Configurations.getJdbcConfiguration();
		return DriverManager.getConnection(cfg.get("url"), cfg.get("username"), cfg.get("password"));
	}

}

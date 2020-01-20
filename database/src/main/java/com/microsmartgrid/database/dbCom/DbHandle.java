package com.microsmartgrid.database.dbCom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
	public static void execute(String command) throws SQLException {
		try (Connection conn = new DatabaseConfig().getConnection();
			 Statement stmt = conn.createStatement()) {
			stmt.execute(command);
		} catch (SQLException e) {
			logger.error("Couldn't excecute: " + command);
			throw new SQLException(e);
		}
	}

}

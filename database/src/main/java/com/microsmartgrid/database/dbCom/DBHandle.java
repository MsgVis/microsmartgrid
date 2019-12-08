package com.microsmartgrid.database.dbCom;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Connection to PostgreSQL database
 */
public class DBHandle {

	private static final Logger logger = LogManager.getLogger(DBHandle.class.getName());

	private Connection conn;
	private Statement stmt;
	private ResultSet rs;

	/**
	 * Connect to postgres database on same machine (localhost)
	 *
	 * @param database
	 * @param username
	 * @param password
	 */
	public void connect(String database, String username, String password) throws SQLException {
		try {
			if (conn != null && conn.isValid(0)) throw new IllegalStateException("Handle already connected");
			conn = DriverManager.getConnection(database, username, password);
		} catch (SQLException e) {
			logger.fatal("Couldn't establish connection to database.");
			throw new SQLException(e);
		}
	}

	/**
	 * Execute SQL command
	 *
	 * @param command
	 */
	public void execute(String command) throws SQLException {
		try {
			if (conn == null || conn.isClosed()) throw new NullPointerException("Handle not connected");
			stmt = conn.createStatement();
			stmt.execute(command);
		} catch (SQLException e) {
			logger.error("Couldn't excecute: " + command);
			logger.error(e);
		}
	}

	/**
	 * Execute SQL command which returns ResultSet
	 *
	 * @param command
	 * @return
	 */
	public ResultSet executeQuery(String command) {
		try {
			if (conn == null || conn.isClosed()) throw new NullPointerException("Handle not connected");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(command);
		} catch (SQLException e) {
			logger.error("Couldn't execute query: " + command);
			logger.error(e);
		}
		return rs;
	}

	/**
	 * Close connection
	 */
	public void cleanUp() throws SQLException {
		try {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		} catch (SQLException e) {
			logger.fatal("Couldn't close connection to database.");
			throw new SQLException(e);
		}
	}
}

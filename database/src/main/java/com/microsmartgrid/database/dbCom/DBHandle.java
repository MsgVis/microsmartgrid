package com.microsmartgrid.database.dbCom;

import java.sql.*;

/**
 * Connection to PostgreSQL database
 */
public class DBHandle {

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
	public void connect(String database, String username, String password) {
		try {
			// jdbc:postgresql://localhost/
			conn = DriverManager.getConnection(database, username, password);
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Execute SQL command
	 *
	 * @param command
	 */
	public void execute(String command) {
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(command);
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Close connection
	 */
	public void cleanUp() {
		try {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

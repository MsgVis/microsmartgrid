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
			if(conn != null && conn.isValid(0)) throw new Exception("Handle already connected");
			conn = DriverManager.getConnection(database, username, password);
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
			if(conn == null || conn.isClosed()) throw new Exception("Handle not connected");
			stmt = conn.createStatement();
			stmt.execute(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Execute SQL command which returns ResultSet
	 * @param command
	 * @return
	 */
	public ResultSet executeQuery(String command) {
		try {
			if(conn == null || conn.isClosed()) throw new Exception("Handle not connected");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * Close connection
	 */
	public void cleanUp() {
		try {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

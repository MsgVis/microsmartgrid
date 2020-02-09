package com.microsmartgrid.database.dbCom;

public abstract class SqlCommands {

	public static final String INSERT_READINGS = "INSERT INTO " +
		"readings (time, device_id, a_minus, a_plus, r_minus, r_plus, p_total, p_r, p_s, p_t, q_total, q_r, q_s, q_t, s_total, s_r, s_s, s_t, i_avg, i_r, i_s, i_t, u_avg, u_r, u_s, u_t, f, meta) " +
		"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (?::json));";

	public static final String INSERT_DEVICES = "INSERT INTO " +
		"devices (id, name, description, type, subtype, children) " +
		"VALUES (DEFAULT, ?, ?, ?, ?, ?);";

	public static final String QUERY_DEVICES = "SELECT * FROM devices WHERE name=?;";
	public static final String QUERY_DEVICES_BY_ID = "SELECT * FROM devices WHERE id=?;";
	public static final String QUERY_ALL_DEVICES = "SELECT * FROM devices";

	public static String CREATE_DEVICE_TABLE =
		"CREATE TABLE devices (" +
			"id INTEGER auto_increment," +
			"name TEXT," +
			"description TEXT," +
			"type TEXT," +
			"subtype TEXT," +
			"children ARRAY" +
			");";

	public static String CREATE_READINGS_TABLE = "CREATE TABLE readings (" +
		"time TIMESTAMP," +
		"device_id INTEGER," +
		"a_minus REAL," +
		"a_plus REAL," +
		"r_minus REAL," +
		"r_plus REAL," +
		"p_total REAL," +
		"p_r REAL," +
		"p_s REAL," +
		"p_t REAL," +
		"q_total REAL," +
		"q_r REAL," +
		"q_s REAL," +
		"q_t REAL," +
		"s_total REAL," +
		"s_r REAL," +
		"s_s REAL," +
		"s_t REAL," +
		"i_avg REAL," +
		"i_r REAL," +
		"i_s REAL," +
		"i_t REAL," +
		"u_avg REAL," +
		"u_r REAL," +
		"u_s REAL," +
		"u_t REAL," +
		"f REAL," +
		"meta JSON" +
		");";
}

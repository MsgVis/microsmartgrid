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
	public static final String QUERY_ALL_DEVICES = "SELECT * FROM devices;";

	public static final String QUERY_READINGS_SELECT_START = "SELECT device_id, meta";
	public static final String QUERY_READINGS_BUCKET = " time_bucket(?, time) AS bucket,";
	public static final String QUERY_READINGS_GROUP_BUCKET = " GROUP BY bucket ORDER BY bucket ASC;";
	public static final String QUERY_READINGS_TIME = " time,";
	public static final String QUERY_READINGS = " a_minus," +
		" a_plus," +
		" r_minus," +
		" r_plus," +
		" p_total," +
		" p_r," +
		" p_s," +
		" p_t," +
		" q_total," +
		" q_r," +
		" q_s," +
		" q_t," +
		" s_total," +
		" s_r," +
		" s_s," +
		" s_t," +
		" i_avg," +
		" i_r," +
		" i_s," +
		" i_t," +
		" u_avg," +
		" u_r," +
		" u_s," +
		" u_t," +
		" f";
	public static final String QUERY_READINGS_AVERAGES = " avg(a_minus) AS a_minus," +
		" avg(a_plus) AS a_plus," +
		" avg(r_minus) AS r_minus," +
		" avg(r_plus) AS r_plus," +
		" avg(p_total) AS p_total," +
		" avg(p_r) AS p_r," +
		" avg(p_s) AS p_s," +
		" avg(p_t) AS p_t," +
		" avg(q_total) AS q_total," +
		" avg(q_r) AS q_r," +
		" avg(q_s) AS q_s," +
		" avg(q_t) AS q_t," +
		" avg(s_total) AS s_total," +
		" avg(s_r) AS s_r," +
		" avg(s_s) AS s_s," +
		" avg(s_t) AS s_t," +
		" avg(i_avg) AS i_avg," +
		" avg(i_r) AS i_r," +
		" avg(i_s) AS i_s," +
		" avg(i_t) AS i_t," +
		" avg(u_avg) AS u_avg," +
		" avg(u_r) AS u_r," +
		" avg(u_s) AS u_s," +
		" avg(u_t) AS u_t," +
		" avg(f) as f";
	public static final String FROM_READINGS = " FROM readings";
	public static final String FILTER_READINGS_ID = " device_id = ? AND";
	public static final String FILTER_READINGS_TIME_AFTER = " time > ? AND";
	public static final String FILTER_READINGS_TIME_BEFORE = " time < ? AND";

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

package com.microsmartgrid.database.dbCom;

public abstract class SqlCommands {
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

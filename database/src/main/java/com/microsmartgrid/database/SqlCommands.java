package com.microsmartgrid.database;

public abstract class SqlCommands {
	public static final String FROM_READINGS_AGG = " FROM readings" +
		" WHERE (:id = 0 OR device_id = :id)" +
		" AND (:since = 0 OR time >= to_timestamp(:since))" +
		" AND (:until = 0 OR time <= to_timestamp(:until))" +
		" GROUP BY agg_time, device_id" +
		" ORDER BY agg_time DESC";

	public static final String QUERY_READINGS = "SELECT *" +
		" FROM readings" +
		" WHERE (:id = 0 OR device_id = :id)" +
		" AND (:since = 0 OR time >= to_timestamp(:since))" +
		" AND (:until = 0 OR time <= to_timestamp(:until))" +
		" GROUP BY time, device_id" +
		" ORDER BY time DESC";
	public static final String QUERY_READINGS_AVERAGES = "SELECT device_id," +
		" time_bucket(CAST(:step AS INTERVAL), time) AS agg_time," +
		" avg(a_minus) AS agg_a_minus," +
		" avg(a_plus) AS agg_a_plus," +
		" avg(r_minus) AS agg_r_minus," +
		" avg(r_plus) AS agg_r_plus," +
		" avg(p_total) AS agg_p_total," +
		" avg(p_r) AS agg_p_r," +
		" avg(p_s) AS agg_p_s," +
		" avg(p_t) AS agg_p_t," +
		" avg(q_total) AS agg_q_total," +
		" avg(q_r) AS agg_q_r," +
		" avg(q_s) AS agg_q_s," +
		" avg(q_t) AS agg_q_t," +
		" avg(s_total) AS agg_s_total," +
		" avg(s_r) AS agg_s_r," +
		" avg(s_s) AS agg_s_s," +
		" avg(s_t) AS agg_s_t," +
		" avg(i_avg) AS agg_i_avg," +
		" avg(i_r) AS agg_i_r," +
		" avg(i_s) AS agg_i_s," +
		" avg(i_t) AS agg_i_t," +
		" avg(u_avg) AS agg_u_avg," +
		" avg(u_r) AS agg_u_r," +
		" avg(u_s) AS agg_u_s," +
		" avg(u_t) AS agg_u_t," +
		" avg(f) as agg_f";
	public static final String QUERY_READINGS_STDDEV = "SELECT device_id," +
		" time_bucket(CAST(:step AS INTERVAL), time) AS agg_time," +
		" stddev_pop(a_minus) AS agg_a_minus," +
		" stddev_pop(a_plus) AS agg_a_plus," +
		" stddev_pop(r_minus) AS agg_r_minus," +
		" stddev_pop(r_plus) AS agg_r_plus," +
		" stddev_pop(p_total) AS agg_p_total," +
		" stddev_pop(p_r) AS agg_p_r," +
		" stddev_pop(p_s) AS agg_p_s," +
		" stddev_pop(p_t) AS agg_p_t," +
		" stddev_pop(q_total) AS agg_q_total," +
		" stddev_pop(q_r) AS agg_q_r," +
		" stddev_pop(q_s) AS agg_q_s," +
		" stddev_pop(q_t) AS agg_q_t," +
		" stddev_pop(s_total) AS agg_s_total," +
		" stddev_pop(s_r) AS agg_s_r," +
		" stddev_pop(s_s) AS agg_s_s," +
		" stddev_pop(s_t) AS agg_s_t," +
		" stddev_pop(i_avg) AS agg_i_avg," +
		" stddev_pop(i_r) AS agg_i_r," +
		" stddev_pop(i_s) AS agg_i_s," +
		" stddev_pop(i_t) AS agg_i_t," +
		" stddev_pop(u_avg) AS agg_u_avg," +
		" stddev_pop(u_r) AS agg_u_r," +
		" stddev_pop(u_s) AS agg_u_s," +
		" stddev_pop(u_t) AS agg_u_t," +
		" stddev_pop(f) as agg_f";
	public static final String QUERY_READINGS_MIN = "SELECT device_id," +
		" time_bucket(CAST(:step AS INTERVAL), time) AS agg_time," +
		" min(a_minus) AS agg_a_minus," +
		" min(a_plus) AS agg_a_plus," +
		" min(r_minus) AS agg_r_minus," +
		" min(r_plus) AS agg_r_plus," +
		" min(p_total) AS agg_p_total," +
		" min(p_r) AS agg_p_r," +
		" min(p_s) AS agg_p_s," +
		" min(p_t) AS agg_p_t," +
		" min(q_total) AS agg_q_total," +
		" min(q_r) AS agg_q_r," +
		" min(q_s) AS agg_q_s," +
		" min(q_t) AS agg_q_t," +
		" min(s_total) AS agg_s_total," +
		" min(s_r) AS agg_s_r," +
		" min(s_s) AS agg_s_s," +
		" min(s_t) AS agg_s_t," +
		" min(i_avg) AS agg_i_avg," +
		" min(i_r) AS agg_i_r," +
		" min(i_s) AS agg_i_s," +
		" min(i_t) AS agg_i_t," +
		" min(u_avg) AS agg_u_avg," +
		" min(u_r) AS agg_u_r," +
		" min(u_s) AS agg_u_s," +
		" min(u_t) AS agg_u_t," +
		" min(f) as agg_f";
	public static final String QUERY_READINGS_MAX = "SELECT device_id," +
		" time_bucket(CAST(:step AS INTERVAL), time) AS agg_time," +
		" max(a_minus) AS agg_a_minus," +
		" max(a_plus) AS agg_a_plus," +
		" max(r_minus) AS agg_r_minus," +
		" max(r_plus) AS agg_r_plus," +
		" max(p_total) AS agg_p_total," +
		" max(p_r) AS agg_p_r," +
		" max(p_s) AS agg_p_s," +
		" max(p_t) AS agg_p_t," +
		" max(q_total) AS agg_q_total," +
		" max(q_r) AS agg_q_r," +
		" max(q_s) AS agg_q_s," +
		" max(q_t) AS agg_q_t," +
		" max(s_total) AS agg_s_total," +
		" max(s_r) AS agg_s_r," +
		" max(s_s) AS agg_s_s," +
		" max(s_t) AS agg_s_t," +
		" max(i_avg) AS agg_i_avg," +
		" max(i_r) AS agg_i_r," +
		" max(i_s) AS agg_i_s," +
		" max(i_t) AS agg_i_t," +
		" max(u_avg) AS agg_u_avg," +
		" max(u_r) AS agg_u_r," +
		" max(u_s) AS agg_u_s," +
		" max(u_t) AS agg_u_t," +
		" max(f) as agg_f";
}
